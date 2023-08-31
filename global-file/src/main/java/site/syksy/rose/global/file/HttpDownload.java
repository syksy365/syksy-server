package site.syksy.rose.global.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * http 文件下载工具类
 *
 * @author Raspberry
 */
public class HttpDownload {

    private static final Logger log = LoggerFactory.getLogger(HttpDownload.class);

    /**
     * 自定义缓存区大小，一次读取磁盘文件到内存，根据实际情况优化调整
     */
    private static final int BUFFER = 1024 * 8;

    /**
     * RandomAccessFile工作模式,此处设置只读即可
     */
    private static final String MODE = "r";

    /**
     * 文件下载，支持普通下载，断点继续下载，断点区间下载
     *
     * @param request    请求
     * @param response   响应
     * @param sourceFile 文件路径
     * @param fileName   文件名称
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String sourceFile, String fileName) {
        response.reset();
        File file = new File(sourceFile);
        long fileLength = file.length();
        try {
            RandomAccessFile raf = new RandomAccessFile(file, MODE);
            ServletOutputStream sos = response.getOutputStream();
            String rangeBytes = null;
            RangeSwitchEnum rangeSwitch = RangeSwitchEnum.ORDINARY;
            long pastLength = 0;
            long toLength;
            long contentLength;
            if (null != request.getHeader("Range")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
                String trim = rangeBytes.substring(0, rangeBytes.indexOf("-")).trim();
                if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {
                    rangeSwitch = RangeSwitchEnum.BEGIN;
                    rangeBytes = trim;
                    pastLength = Long.parseLong(rangeBytes);
                    contentLength = fileLength - pastLength;
                } else {
                    rangeSwitch = RangeSwitchEnum.BLOCK;
                    pastLength = Long.parseLong(trim);
                    toLength = Long.parseLong(rangeBytes.substring(rangeBytes.indexOf("-") + 1).trim());
                    contentLength = toLength - pastLength + 1;
                }
            } else {
                contentLength = fileLength;
            }
            setResponse(response, contentLength, fileName);
            switch (rangeSwitch) {
                case BEGIN: {
                    String contentRange = "bytes " + pastLength + "-" + (fileLength - 1) + "/" + fileLength;
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                case BLOCK: {
                    String contentRange = "bytes " + rangeBytes + "/" + fileLength;
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                default: {
                    break;
                }
            }
            inputStreamToOutputStream(raf, sos, pastLength, contentLength);
        } catch (Exception e) {
            log.error("文件下载发生错误！原因：" + e.getMessage());
        }
    }

    /**
     * 设置响应对象信息
     *
     * @param response      响应对象
     * @param contentLength 需要下载的文件长度
     * @param fileName      文件名称
     */
    private static void setResponse(HttpServletResponse response, Long contentLength, String fileName) {
        response.setContentType("application/x-download");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment().filename(fileName, StandardCharsets.UTF_8).build().toString());
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(contentLength));
    }

    /**
     * 从输入流中读取内容写入到输出流中
     *
     * @param raf           随机读取流
     * @param sos           输出流
     * @param pastLength    开始读取位置
     * @param contentLength 读取的长度
     */
    private static void inputStreamToOutputStream(RandomAccessFile raf, ServletOutputStream sos, long pastLength, long contentLength) {
        try {
            if (pastLength != 0) {
                raf.seek(pastLength);
            }
            byte[] buffer = new byte[BUFFER];
            int length;
            int readLength = 0;
            while (readLength <= contentLength - BUFFER) {
                length = raf.read(buffer, 0, BUFFER);
                sos.write(buffer, 0, length);
                readLength += length;
            }
            if (readLength < contentLength) {
                length = raf.read(buffer, 0, (int) (contentLength - readLength));
                sos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            log.info("文件下载时发生错误！无需担心，可能是客户端取消了下载：" + e.getMessage());
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    log.error("文件下载关闭输出流发生错误！原因：" + e.getMessage());
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    log.error("文件下载关闭输入流发生错误！原因：" + e.getMessage());
                }
            }
        }
    }
}
