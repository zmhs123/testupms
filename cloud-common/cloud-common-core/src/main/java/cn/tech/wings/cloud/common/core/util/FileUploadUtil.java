package cn.tech.wings.cloud.common.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.*;

public class FileUploadUtil {
	private static boolean isImg(String extend) {
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

	public static Map saveFileToServer(MultipartFile file, String saveFilePathName, String saveFileName) throws IOException {
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
			File oldFile = new File(saveFilePathName + File.separator + saveFileName);
			if (oldFile.exists()) {
				String[] names = saveFileName.split("\\.");
				String newFileName = names[0] + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_FORMAT) + "." + names[1];
				File newFile = new File(saveFilePathName + File.separator + newFileName);
				oldFile.renameTo(newFile);
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
