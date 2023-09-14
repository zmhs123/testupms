package cn.tech.wings.cloud.res.core.util;

import cn.hutool.core.date.DateUtil;
import cn.tech.wings.cloud.res.config.FilesConfig;
import com.alibaba.cloud.commons.lang.StringUtils;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Minio模板类
 *
 * @author 胡汉三
 * @time 2020/5/12 11:28
 */
@Component
public class MinioTemplate {
	//系统使用的文件桶名
	public static final String LIBRARY = "library";
	//企业数据接入子系统使用的文件桶名
	public static final String BUCKET_QYSJJR = "qysjjr";
	@Autowired
	public FilesConfig minIo;
	private MinioClient instance;

	public MinioTemplate(FilesConfig filesConfig) {
		this.minIo = filesConfig;
	}

	/**
	 * 初始化minio配置
	 *
	 * @param :
	 * @return: void
	 * @date : 2020/8/16 20:56
	 */
	@PostConstruct
	public void init() {
		try {
			instance = MinioClient.builder().endpoint(minIo.getEndpoint())
					.credentials(minIo.getAccessKey(), minIo.getSecretKey()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断 bucket是否存在
	 *
	 * @param bucketName
	 * @return
	 */
	public boolean bucketExists(String bucketName) {
		try {
			return instance.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建 bucket
	 *
	 * @param bucketName
	 */
	public boolean makeBucket(String bucketName) {
		try {
			boolean isExist = instance.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!isExist) {
				instance.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
				// 设置只读策略
				String policy = "{\n" + "  \"Statement\": [\n" + "        {\n" + "            \"Action\": [\n"
						+ "                \"s3:GetBucketLocation\",\n" + "                \"s3:ListBucket\"\n"
						+ "            ],\n" + "            \"Effect\": \"Allow\",\n"
						+ "            \"Principal\": \"*\",\n" + "            \"Resource\": \"arn:aws:s3:::"
						+ bucketName + "\"\n" + "        },\n" + "        {\n"
						+ "            \"Action\": \"s3:GetObject\",\n" + "            \"Effect\": \"Allow\",\n"
						+ "            \"Principal\": \"*\",\n" + "            \"Resource\": \"arn:aws:s3:::"
						+ bucketName + "/*\"\n" + "        }\n" + "    ],\n" + "    \"Version\": \"2012-10-17\"\n"
						+ "}";
				instance.setBucketPolicy(SetBucketPolicyArgs.builder().config(policy).bucket(bucketName).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 文件上传
	 *
	 * @param bucketName
	 * @param newName     存到服务器中的名字
	 * @param fileAddress
	 */
	public void uploadObject(String bucketName, String newName, String fileAddress) {
		try {
			instance.uploadObject(
					UploadObjectArgs.builder().bucket(bucketName).object(newName).filename(fileAddress).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件流上传
	 *
	 * @param bucketName
	 * @param file       文件
	 * @return 存到服务器中的路径
	 */
	public String uploadObject(String bucketName, MultipartFile file) {
		try {
			// 判断文件桶是否存在如果不存在则创建
			if (checkBucketExists(bucketName) == false) {
				return "";
			}
			// 上传文件夹的路径
			String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// 文件夹路径
			String folder = DateUtil.date().toString("yyyy/MM/dd").substring(0, 7);
			fileName = suffix.substring(1) + "/" + folder + "/" + fileName + "_" + System.currentTimeMillis() + suffix;
			instance.putObject(PutObjectArgs.builder().contentType(file.getContentType())
					.bucket(bucketName).object(fileName).stream(file.getInputStream(), file.getSize(), 1024 * 1024 * 5)
					.build());
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 文件流上传
	 *
	 * @param bucketName
	 * @param inputStream
	 * @param fileSuffix  文件后缀 例如 zip
	 *                    文件
	 * @return 存到服务器中的路径
	 */
	public String uploadObjectByInputStream(String bucketName, ByteArrayInputStream inputStream, String fileSuffix, String fileName) {
		// 判断桶是否存在如果不存在则创建
		if (checkBucketExists(bucketName) == false) {
			return "";
		}
		String contentType = "";
		if ("zip".equals(fileSuffix)) {
			contentType = "application/x-zip-compressed";
		} else if ("pdf".equals(fileSuffix)) {
			contentType = "application/pdf";
		} else {
			throw new RuntimeException("文件的后缀名没有定义");
		}
		try {
			// 文件夹路径
			String folder = DateUtil.date().toString("yyyy/MM/dd").substring(0, 7);
			fileName = fileSuffix + "/" + folder + "/" + fileName + "_" + System.currentTimeMillis() + "." + fileSuffix;
			instance.putObject(PutObjectArgs.builder().contentType(contentType)
					.bucket(bucketName).object(fileName).stream(inputStream, inputStream.available(), 1024 * 1024 * 5)
					.build());
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String uploadObject(String bucketName, File file) {
		try {
			// 上传文件夹的路径
			String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
			String suffix = file.getName().substring(file.getName().lastIndexOf("."));
			// 文件夹路径
			String folder = DateUtil.date().toString("yyyy/MM/dd").substring(0, 7);
			fileName = suffix.substring(1) + "/" + folder + "/" + fileName + "_" + System.currentTimeMillis() + suffix;
			InputStream fis = new FileInputStream(file);
			instance.putObject(PutObjectArgs.builder()
					// .contentType("application/json")
					// .bucket(bucketName)
					.bucket(bucketName).object(fileName).stream(fis, file.length(), 1024 * 1024 * 5).build());
			fis.close();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 下载文件
	 *
	 * @param bucketName
	 * @param fileAllName    存到服务器中的路径及名字
	 */
	public InputStream getFileInputStream(String bucketName, String fileAllName) {
		try {
			InputStream fileInputStream = instance
					.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileAllName).build());
			return fileInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("读取文件异常");
	}

	/**
	 * 下载文件
	 *
	 * @param filePath
	 */
	public void download(String filePath,HttpServletResponse response) {
		InputStream in = null;
		OutputStream out = null;
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		try {
			in = getFileInputStream(LIBRARY,filePath);
			int len = 0;
			byte[] buffer = new byte[1024];
			out = response.getOutputStream();
			response.reset();
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件服务器文件
	 *
	 * @param bucketName
	 * @param objectName 文件名
	 */
	public void delObject(String bucketName, String objectName) {
		try {
			if (StringUtils.isNotBlank(objectName)) {
				instance.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件流上传到system文件夹
	 *
	 * @param bucketName
	 * @param file       文件
	 * @return 存到服务器中的路径
	 */
	public String uploadSystemObject(String bucketName, MultipartFile file) {
		try {
			// 上传文件夹的路径
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// 文件路径:system/
			String fileName = "system/填报说明" + suffix;
			instance.putObject(PutObjectArgs.builder().contentType(file.getContentType())
					.bucket(bucketName).object(fileName).stream(file.getInputStream(), file.getSize(), 1024 * 1024 * 5)
					.build());
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据前缀查询文件
	 *
	 * @param bucketName bucket名称
	 * @param prefix     前缀
	 */
	public List<Item> getPrefix(String bucketName, String prefix) throws Exception {
		List<Item> objectList = new ArrayList<>();
		ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
				.bucket(bucketName)
				.prefix(prefix)
				.build();

		Iterable<Result<Item>> list = instance.listObjects(listObjectsArgs);
		for (Result<Item> item : list) {
			objectList.add(item.get());
		}
		return objectList;
	}

	/**
	 * 根据前缀删除文件
	 *
	 * @param bucketName bucket名称
	 * @param prefix     前缀
	 */
	public void delPrefix(String bucketName, String prefix) throws Exception {
		List<Item> objectList = getPrefix(bucketName, prefix);

		for (Item item : objectList) {
			delObject(MinioTemplate.LIBRARY, item.objectName());
		}
	}

	/**
	 * 判断桶是否存在不存在则创建
	 *
	 * @param bucketName
	 * @return
	 */
	private boolean checkBucketExists(String bucketName) {
		try {
			if (bucketExists(bucketName) == false) {
				// 创建桶
				return makeBucket(bucketName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
