package site.syksy.qingzhou.file.util;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;

public class ZipUtil {
    public static void zipToOutputStream(List<File> files, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, zipOut, file.getName()+"/");
                } else {
                    zipFile(file, zipOut, "");
                }
            }
        }
    }

    private static void zipDirectory(File folder, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, zipOut, baseDir + file.getName() + "/");
                } else {
                    zipFile(file, zipOut, baseDir);
                }
            }
        }
    }

    private static void zipFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }
}
