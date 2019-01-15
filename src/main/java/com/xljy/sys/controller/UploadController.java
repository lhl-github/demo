package com.xljy.sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xljy.bean.Result;
import com.xljy.util.Const;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.URLUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@RequestMapping("/file")
@Controller
public class UploadController {
	Logger logger = LoggerFactory.getLogger(getClass());

	// 单文件上传
	@PostMapping("/upload")
	@ResponseBody
	public Result upload(HttpServletRequest request, HttpServletResponse response) {

		Result res = null;
		MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
		if (file.isEmpty()) {
			res = Result.resultError(400, "上传失败，文件为空文件或文件不存在");
			response.setStatus(404);
			return res;
		}

		String type = request.getParameter("type");
		String file_name = request.getParameter("file_name");
		String save_path = request.getParameter("save_path");
		if (StringUtils.isEmpty(save_path)) {//
			throw new RuntimeException("保存文件的相对路径不能空!");
		}
		String fileName_y = file.getOriginalFilename();// 原文件名

		if (StringUtils.isNotEmpty(file_name)) {// 传来了要求的文件名则保存成要的文件名
			fileName_y = file_name;
		}

		try {
			// String extensionName = Const.getExtensionName(fileName_y,
			// 0);//获取文件拓展名
			// String fileName_n = UUID.randomUUID().toString();// 新文件名
			// System.out.println(fileName_n);
			String filePath = Const.getFilePath_Real(save_path, type);

			Const.exists_LuJing(filePath);// 判目录是否存在 不存在就创建

			File dest = new File(filePath + fileName_y);// 生成内存文件

			file.transferTo(dest);
			res = Result.resultSuccess(fileName_y, "上传成功");
			logger.info(fileName_y + " ++++++单文件上传成功");
		} catch (IOException e) {
			e.printStackTrace();
			res = Result.resultError(500, "保存文件失败");
			res.setData(fileName_y);
			response.setStatus(500);
			return res;
		} catch (Exception e1) {
			e1.printStackTrace();
			res = Result.resultError(500, "保存文件失败");
			res.setData(fileName_y);
			response.setStatus(500);
			return res;
		}
		return res;
	}

	// 多文件上传
	@PostMapping("/multiUpload")
	@ResponseBody
	public Result multiUpload(HttpServletRequest request, HttpServletResponse response) {

		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

		Result res = null;
		String type = request.getParameter("type");
		String save_path = request.getParameter("save_path");
		if (StringUtils.isEmpty(save_path)) {//
			throw new RuntimeException("保存文件的相对路径不能空!");
		}
		List<String> fileNames = new ArrayList<String>();// 文件名集合
		String filePath = null;
		List err = new ArrayList<>();

		try {
			filePath = Const.getFilePath_Real(save_path, type);// 获取保存文件的路径

			Const.exists_LuJing(filePath);// 判目录是否存在 不存在就创建

			for (int i = 0; i < files.size(); i++) {
				MultipartFile file = files.get(i);
				if (file.isEmpty()) {
					// res = Result.resultError(400, "上传第" + (i++) + "个文件失败
					// 文件不存在");
					err.add(i);
					// return res;
				}
				String fileName_y = file.getOriginalFilename();
				// String extensionName = Const.getExtensionName(fileName_y,
				// 0);// 获取文件的拓展名
				// String fileName_n = UUID.randomUUID().toString();// 新文件名
				fileNames.add(fileName_y);// 留存文件名

				File dest = new File(filePath + fileName_y);// 生成内存文件

				try {
					file.transferTo(dest);
					/// throw new IllegalStateException();
				} catch (IllegalStateException e) {

					e.printStackTrace();
					res = Result.resultError(500, "保存文件失败");
					res.setData(fileName_y);
					response.setStatus(500);
					return res;
				} catch (IOException e) {

					e.printStackTrace();
					res = Result.resultError(500, "保存文件失败");
					res.setData(fileName_y);
					response.setStatus(500);
					return res;
				}
				// logger.info("第" + (i + 1) + "个文件上传成功");

			} // end for

		} catch (Exception e) {
			e.printStackTrace();
			return Result.resultError(500, e.getMessage());
		}
		// err.add(1);
		if (err.size() > 0) {
			res = Result.resultError(500, "存在失败文件");
			res.setData(err);
			return res;
		}
		return Result.resultSuccess(fileNames, files.size() + "个文件上传成功");
	}

	// 文件下载
	@RequestMapping(value = { "/download" })
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
		String sf_OnLine = request.getParameter("sf_OnLine");
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		String save_path = request.getParameter("Relative_path");// 相对路径
		
		fileName = URLUtil.decode(fileName);
		if (StringUtils.isEmpty(save_path)) {//
			throw new RuntimeException("读取文件的相对路径不能空!");
		} else {
			if (!save_path.endsWith("/")) {
				save_path += "/";
			}
		}
		String filePath;
		ResponseEntity<byte[]> response = null;
		try {
			filePath = Const.getFilePath_Real(save_path, type);

			File file = new File(filePath + fileName);

			File file2 = File.createTempFile("temp_tupian_YS", ".jpg");

			InputStream in = null;
			// InputStream in =new FileInputStream(file);// 将该文件加入到输入流之中
			if (StringUtils.equals(type, "1") && file.exists() && file.isFile() && file.length() > 220000) {

				long currentTimeMillis = System.currentTimeMillis();
				float bl_zlys = 0.4f;// 图片质量 压缩比例
				if (file.length() > 2048000) {
					bl_zlys = 0.1f;
				} else if (file.length() > 1024000) {
					bl_zlys = 0.2f;
				} else if (file.length() > 800000) {
					bl_zlys = 0.2f;
				}else if (file.length() > 600000) {
					bl_zlys = 0.3f;
				}
				Thumbnails.of(file).scale(1f)// 压缩后比例不变
						.outputQuality(bl_zlys)// 压缩后图片质量
						.toFile(file2);
				System.out.println("文件大小" + file.length());
				// 将该文件加入到输入流之中
				System.out.println("压缩后文件大小" + file2.length());
				in = new FileInputStream(file2);
				System.out.println("压缩时间:"+(System.currentTimeMillis() - currentTimeMillis));

			} else {
				in = new FileInputStream(file);// 将该文件加入到输入流之中
			}

			byte[] body = null;
			body = new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
			in.read(body);// 读入到输入流里面

			fileName = new String(fileName.getBytes("gbk"), "iso8859-1");// 防止中文乱码
			HttpHeaders headers = new HttpHeaders();// 设置响应头

			String mimeType = request.getServletContext().getMimeType(filePath + fileName);// 通过文件名获取文件的content-type
			headers.add("content-type", mimeType);// 1.设置文件ContentType类型

			if (StringUtils.equals("1", sf_OnLine)) {// 在线播的
				headers.add("Content-Disposition", "inline;filename=" + fileName);// 2.设置文件头：
			} else {// 直接下载的
				headers.add("Content-Disposition", "attachment;filename=" + fileName);// 2.设置文件头：
			}

			HttpStatus statusCode = HttpStatus.OK;// 设置响应吗
			response = new ResponseEntity<byte[]>(body, headers, statusCode);
			// in.close();
		} catch (Exception e) {

			e.printStackTrace();
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		return response;
	}

	// 断点上传文件
	@RequestMapping(value = { "/webuploader/uploadFile" })
	@ResponseBody
	public Map<String, String> webuploader_uploadFile(HttpServletRequest request) throws IOException {
		Map<String, String> sMap = new HashMap<String, String>();
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory); // 得到所有的提交的表单域，也就是formData
				List<FileItem> fileItems = upload.parseRequest((RequestContext) request);// 文件集
				String id = "";
				String fileName = "";
				// 如果大于1说明是分片处理
				int chunks = 1;
				int chunk = 0;
				long fileSize = 0;
				long lastModiDate = 0;
				String tableName = "";
				FileItem tempFileItem = null;
				for (FileItem fileItem : fileItems) {
					if (fileItem.getFieldName().equals("id")) {
						id = fileItem.getString();
					} else if (fileItem.getFieldName().equals("name")) {
						fileName = new String(fileItem.getString().getBytes("ISO-8859-1"), "UTF-8");
					} else if (fileItem.getFieldName().equals("chunks")) {
						chunks = NumberUtils.toInt(fileItem.getString());
					} else if (fileItem.getFieldName().equals("chunk")) {
						chunk = NumberUtils.toInt(fileItem.getString());
					} else if (fileItem.getFieldName().equals("multiFile")) {// 获取的文件集
						tempFileItem = fileItem;
					} else if (fileItem.getFieldName().equals("fileSize")) {
						fileSize = NumberUtils.toLong(fileItem.getString());
					} else if (fileItem.getFieldName().equals("lastModiDate")) {
						lastModiDate = NumberUtils.toLong(fileItem.getString());
					} else if (fileItem.getFieldName().equals("tableName")) {
						tableName = fileItem.getString();
					}
				}
				System.out.println(tableName);
				String fileSysName = tempFileItem.getName();
				String realname = lastModiDate + fileSysName.substring(fileSysName.lastIndexOf("."));// 转化后的文件名
				sMap.put("fileSysName", fileSysName);
				sMap.put("realname", realname); // 真实上传的地址，是我底层的地址，这里就不多说了
				String realPath = Const.getFilePath_Real("", "");
				realPath = realPath + File.separator + tableName.toUpperCase() + File.separator;
				String filePath = realPath;// 文件上传路径 // 临时目录用来存放所有分片文件
				String tempFileDir = filePath + id + "_" + fileSize;
				File parentFileDir = new File(tempFileDir);
				if (!parentFileDir.exists()) {
					parentFileDir.mkdirs();
				}
				// 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
				File tempPartFile = new File(parentFileDir, realname + "_" + chunk + ".part");
				// FileUtils.copyInputStreamToFile(tempFileItem.getInputStream(),
				// tempPartFile);

				// 是否全部上传完成 // 所有分片都存在才说明整个文件上传完成
				boolean uploadDone = true;
				for (int i = 0; i < chunks; i++) {
					File partFile = new File(parentFileDir, realname + "_" + i + ".part");
					if (!partFile.exists()) {
						uploadDone = false;
					}
				}
				// 所有分片文件都上传完成 // 将所有分片文件合并到一个文件中
				if (uploadDone) { // 得到 destTempFile 就是最终的文件
					File destTempFile = new File(filePath, realname);
					for (int i = 0; i < chunks; i++) {
						File partFile = new File(parentFileDir, realname + "_" + i + ".part");
						FileOutputStream destTempfos = new FileOutputStream(destTempFile, true); // 遍历"所有分片文件"到"最终文件"中
						// FileUtils.copyFile(partFile, destTempfos);
						destTempfos.close();
					}
					// 删除临时目录中的分片文件
					FileUtils.deleteDirectory(parentFileDir);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sMap;
	}

	@RequestMapping("/uploader")
	public void upload222(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("收到图片!");
		MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = Murequest.getFileMap();// 得到文件map对象
		String upaloadUrl = request.getSession().getServletContext().getRealPath("/") + "upload/";// 得到当前工程路径拼接上文件名
		File dir = new File(upaloadUrl);
		System.out.println(upaloadUrl);
		if (!dir.exists())// 目录不存在则创建 dir.mkdirs();
			for (MultipartFile file : files.values()) {
				int counter = 0;
				counter++;
				String fileName = file.getOriginalFilename();
				File tagetFile = new File(upaloadUrl + fileName);// 创建文件对象
				if (!tagetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
					try {
						tagetFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						file.transferTo(tagetFile);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		System.out.println("接收完毕");
	}

}
