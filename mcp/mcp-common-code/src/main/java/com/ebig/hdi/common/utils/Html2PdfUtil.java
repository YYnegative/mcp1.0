package com.ebig.hdi.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Html2PdfUtil {

	// 临时文件夹获取java system变量中的临时路径，在web项目中是容器的temp文件夹,如果直接运行是系统临时文件夹.
	private static final String FILE_PATH_TEMPLATE = System.getProperty("java.io.tmpdir") + "/%s";

	public static String getPdfUrl(String html, Document document) throws Exception {
		InputStream is = new ByteArrayInputStream(html.toString().getBytes("utf-8"));
		// 生成临时文件位置，存储到 java.io.tmpdir 下
		String pdfFilePath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".pdf");
		File files = new File(pdfFilePath);
		// 如果不存在临时文件夹，则创建文件夹
		if (!files.getParentFile().exists()) {
			files.getParentFile().mkdirs();
		}
		// 打印查看上传路径
		System.out.println("临时文件所在位置：" + files.getPath());
		OutputStream os = new FileOutputStream(pdfFilePath);
		PdfWriter writer = PdfWriter.getInstance(document, os);
		document.open();
		// 将html转pdf
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, Charset.forName("UTF-8"));
		document.close();

		return pdfFilePath;
	}

	public static String getPdfUrl(String html, String css, Document document) throws Exception {
		try (InputStream htmlIS = new ByteArrayInputStream(html.toString().getBytes("utf-8"));
				InputStream cssIS = new ByteArrayInputStream(css.toString().getBytes("utf-8"));) {
			// 生成临时文件位置，存储到 java.io.tmpdir 下
			String pdfFilePath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".pdf");
			File files = new File(pdfFilePath);
			// 如果不存在临时文件夹，则创建文件夹
			if (!files.getParentFile().exists()) {
				files.getParentFile().mkdirs();
			}
			// 打印查看上传路径
			System.out.println("临时文件所在位置：" + files.getPath());
			OutputStream os = new FileOutputStream(pdfFilePath);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			// 将html转pdf
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlIS, cssIS, Charset.forName("UTF-8"));
			document.close();

			return pdfFilePath;
		}
	}

}
