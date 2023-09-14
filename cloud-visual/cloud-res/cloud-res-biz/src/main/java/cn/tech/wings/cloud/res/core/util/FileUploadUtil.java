package cn.tech.wings.cloud.res.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.tech.wings.cloud.common.file.oss.OssProperties;
import cn.tech.wings.cloud.res.config.ConfigReader;
import cn.tech.wings.cloud.res.core.constant.ResConstants;
import com.aliyun.oss.*;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class FileUploadUtil {

	private final OssProperties ossProperties;

	private final ConfigReader configReader;

	private MinioTemplate minioTemplate;


	/**
	 * 文件上传
	 *
	 * @param file
	 * @param ossKey
	 * @return
	 */
	public String uploadFile(File file, String ossKey) {
		String key = "";
		if ("minio".equals(configReader.getImplementation())) {
			//minio上传
			key = minioTemplate.uploadObject(MinioTemplate.LIBRARY, file);
		} else {
			key = uploadFileOss(file, ossKey);
		}

		return key;
	}


	/**
	 * 上传文件到oss
	 *
	 * @param file
	 * @param key
	 * @return
	 */
	public String uploadFileOss(File file, String key) {
		OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setObjectAcl(CannedAccessControlList.PublicRead);
			setCache(metadata);
			PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketPublicName(), key, file, metadata);
			//上传设置pdf格式的设置contentType
			if (key.contains("pdf")) {
				putObjectRequest.getMetadata().setContentType("application/pdf");
			}
			long timeMillis = System.currentTimeMillis();
			ossClient.putObject(putObjectRequest);
			long timeMillis1 = System.currentTimeMillis();
			System.out.println("上传方法oss耗时:" + (timeMillis1 - timeMillis));

		} catch (OSSException oe) {
			System.out.println("Error Message:" + oe.getErrorMessage());
			System.out.println("Error Code:" + oe.getErrorCode());
			System.out.println("Request ID:" + oe.getRequestId());
			System.out.println("Host ID:" + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Error Message:" + ce.getMessage());
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
			}
		}
		return key;
	}


	/**
	 * 设置缓存30天时间
	 *
	 * @param metadata
	 */
	private void setCache(ObjectMetadata metadata) {
		metadata.setHeader("Cache-Control", "max-age=2592000");
	}

	public boolean isImg(String extend) {
		boolean ret = false;
		List<String> list = new ArrayList<String>();
		list.add("jpg");
		list.add("jpeg");
		list.add("bmp");
		list.add("gif");
		list.add("png");
		list.add("tif");
		for (String s : list) {
			if (s.equals(extend)) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 获取上传文件路径、辅助方法
	 *
	 * @return
	 */
	public String getFilePath() {
		String path = "";
		String uploadFilePath = configReader.getUploadFilePath();
		Date now = new Date();
		if (configReader.getUploadFilePathType().equals(ResConstants.FILE_PATH_TYPE_S)) {
			path = uploadFilePath + "/";
		}

		if (configReader.getUploadFilePathType().equals(ResConstants.FILE_PATH_TYPE_SY)) {
			path = uploadFilePath + "/" + DateUtil.format(now, "yyyy");
		}
		if (configReader.getUploadFilePathType().equals(ResConstants.FILE_PATH_TYPE_SYM)) {
			path = uploadFilePath + "/"
					+ "/" + DateUtil.format(now, "yyyy")
					+ "/" + DateUtil.format(now, "MM");
		}
		if (configReader.getUploadFilePathType().equals(ResConstants.FILE_PATH_TYPE_SYMD)) {
			path = uploadFilePath + "/"
					+ "/" + DateUtil.format(now, "yyyy")
					+ "/" + DateUtil.format(now, "MM") + "/"
					+ DateUtil.format(now, "dd");
		}
		return path;
	}

	/**
	 * 获取上传相关参数
	 *
	 * @param resType
	 * @return
	 */
	public Map getUploadParams(String resType) {
		Map data = new HashMap();
		data.put("resType", resType);
		if (ResConstants.FILE_TYPE_PIC.equals(resType)) {
			data.put("exts", configReader.getImageSuffix());
			data.put("fileSize", Convert.toLong(configReader.getImageFileSize()) * 1024 * 1024);
		} else if (ResConstants.FILE_TYPE_VIDEO.equals(resType)) {
			data.put("exts", configReader.getVideoSuffix());
			data.put("fileSize", Convert.toLong(configReader.getVideoFileSize()) * 1024 * 1024);//100m
		} else {
			data.put("exts", configReader.getFileSuffix());
			data.put("fileSize", Convert.toLong(configReader.getFileFileSize()) * 1024 * 1024);//10m
		}
		return data;
	}


	public Map saveFileToServer(MultipartFile file, String saveFilePathName, String saveFileName,
								String[] extendes) throws IOException {

		Map map = new HashMap();
		if ((file != null) && (!file.isEmpty())) {
			String extend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
			if ((saveFileName == null) || (saveFileName.trim().equals(""))) {
				saveFileName = UUID.randomUUID().toString() + "." + extend;
			}
			if (saveFileName.lastIndexOf(".") < 0) {
				saveFileName = saveFileName + "." + extend;
			}
			float fileSize = Float.valueOf((float) file.getSize()).floatValue();
			List<String> errors = new ArrayList<String>();

			File path = new File(saveFilePathName);
			if (!path.exists()) {
				path.mkdir();
				path.setWritable(true);
			}
			DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFilePathName + File.separator + saveFileName));
			InputStream is = null;
			try {
				is = file.getInputStream();
				int size = (int) fileSize;
				byte[] buffer = new byte[size];
				while (is.read(buffer) > 0) {
					out.write(buffer);
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			}
			if (isImg(extend)) {
				File img = new File(saveFilePathName + File.separator + saveFileName);
				try {
					BufferedImage bis = ImageIO.read(img);
					int w = bis.getWidth();
					int h = bis.getHeight();
					map.put("width", Integer.valueOf(w));
					map.put("height", Integer.valueOf(h));
				} catch (Exception localException) {
					localException.printStackTrace();
				}
			}

			map.put("mime", extend);
			map.put("fileName", saveFileName);
			map.put("fileSize", Float.valueOf(fileSize));
			map.put("error", errors);
			map.put("oldName", file.getOriginalFilename());

		} else {
			map.put("width", Integer.valueOf(0));
			map.put("height", Integer.valueOf(0));
			map.put("mime", "");
			map.put("fileName", "");
			map.put("fileSize", Float.valueOf(0.0F));
			map.put("oldName", "");
		}
		return map;
	}


	/**
	 * 删除文件
	 * @param key
	 */
	public void deleteFile(String key) {
		if ("minio".equals(configReader.getImplementation())) {
			minioTemplate.delObject(MinioTemplate.LIBRARY, key);
		} else {
			this.deleteOssFile(key);
		}
	}


	/**
	 * 删除阿里云文件
	 *
	 * @param key
	 */
	public void deleteOssFile(String key) {
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
		client.deleteObject(ossProperties.getBucketPublicName(), key);
	}

	public static boolean waterMarkWithTextW(HttpServletRequest request, String filePath, String outPath, String text, String markContentColor, Font font, int pos,
											 float qualNum, int width, int height) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		/*
		 * int width = theImg.getWidth(null); int height =
		 * theImg.getHeight(null);
		 */
		/*
		 * int width = 120; int height = 120;
		 */
		BufferedImage bimage = new BufferedImage(width, height, 1);
		Graphics2D g = bimage.createGraphics();
		if (font == null) {
			font = new Font("Garamond", 1, 50);
//			font = new Font(SpringContextUtil.getBundlesKey(ConstantsUtils.I18_JAVA_MSG_BLACKSTYLE, request), 1, 30);
			g.setFont(font);
		} else {
			g.setFont(font);
		}
		g.setColor(getColor(markContentColor));
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		FontMetrics metrics = new FontMetrics(font) {
		};
		Rectangle2D bounds = metrics.getStringBounds(text, null);
		int widthInPixels = (int) bounds.getWidth();
		int heightInPixels = (int) bounds.getHeight();
		int left = 0;
		int top = heightInPixels;

		if (pos == 2) {
			left = width / 2;
			top = heightInPixels;
		}
		if (pos == 3) {
			left = width - widthInPixels;
			top = heightInPixels;
		}
		if (pos == 4) {
			left = width - widthInPixels;
			top = height / 2;
		}
		if (pos == 5) {
			left = width - widthInPixels;
			top = height - heightInPixels;
		}
		if (pos == 6) {
			left = width / 2;
			top = height - heightInPixels;
		}
		if (pos == 7) {
			left = 0;
			top = height - heightInPixels;
		}
		if (pos == 8) {
			left = 0;
			top = height / 2;
		}
		if (pos == 9) {
			left = width / 2;
			top = height / 2;
		}
		g.drawString(text, left, top);
		g.dispose();
		try {
			saveImage(bimage, outPath);
			/*FileOutputStream out = new FileOutputStream(outPath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(qualNum, true);
			encoder.encode(bimage, param);
			out.close();*/
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Color getColor(String color) {
		if (color.charAt(0) == '#') {
			color = color.substring(1);
		}
		if (color.length() != 6) {
			return null;
		}
		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException nfe) {
		}
		return null;
	}

	public static void saveImage(BufferedImage dstImage, String dstName) throws IOException {
		String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
		ImageIO.write(dstImage, /*"GIF"*/ formatName /* format desired */, new File(dstName) /* target */);
	}

	public static final void waterMarkWithImageW(String s3Img, String tmpPath, String targetImg, int pos, float alpha, int width, int height) {

		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			/*
			 * int width = src.getWidth(null); int height = src.getHeight(null);
			 */
			BufferedImage image = new BufferedImage(width, height, 1);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			// 从S3服务器把水印图片下载到临时文件
			s3Img = s3Img.replace("\\", "/");
			URL url = new URL(s3Img);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());


			File path = new File(tmpPath);
			if (!path.exists()) {
				path.mkdirs();
			}

			fos = new FileOutputStream(tmpPath + s3Img.substring(s3Img.lastIndexOf("/")));

			int BUFFER_SIZE = 1024;
			byte[] buf = new byte[BUFFER_SIZE];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
			File _filebiao = new File(tmpPath + s3Img.substring(s3Img.lastIndexOf("/")));
			Image src_biao = ImageIO.read(_filebiao);
			g.setComposite(AlphaComposite.getInstance(10, alpha / 100.0F));
			int width_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			int x = 0;
			int y = 0;

			if (pos == 2) {
				x = (width - width_biao) / 2;
				y = 0;
			}
			if (pos == 3) {
				x = width - width_biao;
				y = 0;
			}
			if (pos == 4) {
				x = width - width_biao;
				y = (height - height_biao) / 2;
			}
			if (pos == 5) {
				x = width - width_biao;
				y = height - height_biao;
			}
			if (pos == 6) {
				x = (width - width_biao) / 2;
				y = height - height_biao;
			}
			if (pos == 7) {
				x = 0;
				y = height - height_biao;
			}
			if (pos == 8) {
				x = 0;
				y = (height - height_biao) / 2;
			}
			if (pos == 9) {
				x = (width - width_biao) / 2;
				y = (height - height_biao) / 2;
			}
			g.drawImage(src_biao, x, y, width_biao, height_biao, null);

			g.dispose();
			saveImage(image, targetImg);
//			FileOutputStream out = new FileOutputStream(targetImg);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(image);
//			out.close();
//			_filebiao.deleteOnExit();
			if (_filebiao.exists()) {
				_filebiao.delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (IOException e) {

			}
		}
	}

}
