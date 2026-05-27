package com.course.util;

import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUploadUtil {
    
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    
    /**
     * 将上传的图片转换为Base64字符串存储到数据库
     * 返回格式：data:image/png;base64,xxxxx
     */
    public static String uploadFileAsBase64(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        
        // 检查文件大小
        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new IOException("文件大小超过限制(最大2MB)");
        }
        
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        // 检查文件扩展名
        String ext = getFileExtension(fileName).toLowerCase();
        boolean allowed = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (ext.equals(allowedExt)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            throw new IOException("不支持的文件格式，仅支持jpg/png/gif/webp");
        }
        
        // 读取文件内容
        InputStream inputStream = filePart.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] fileBytes = outputStream.toByteArray();
        
        // 转换为Base64
        String base64 = Base64.getEncoder().encodeToString(fileBytes);
        
        // 获取MIME类型
        String mimeType = getMimeType(ext);
        
        // 返回Data URL格式
        return "data:" + mimeType + ";base64," + base64;
    }
    
    private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
    private static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return "";
    }
    
    private static String getMimeType(String ext) {
        switch (ext.toLowerCase()) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".webp":
                return "image/webp";
            default:
                return "image/png";
        }
    }
    
    // 兼容旧方法（不再使用文件存储）
    public static String uploadFile(Part filePart, String realPath) throws IOException {
        return uploadFileAsBase64(filePart);
    }
    
    public static void deleteFile(String relativePath, String realPath) {
        // Base64存储在数据库，无需删除文件
    }
}
