package site.syksy.qingzhou.global.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Raspberry
 */
public class FileTool {

    public static final String source = System.getProperty("user.home") + File.separator + "syksy";

    public static final String DOT = ".";

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * 获取文件md5摘要
     *
     * @param file 文件
     * @return 文件md5摘要
     */
    public static String calcMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] bytes = md.digest();
            StringBuilder buf = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                buf.append(String.format("%02x", new Integer(b & 0xff)));
            }
            return buf.toString();
        } catch (IOException ex) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String calcMD5(InputStream stream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    /**
     * 判断文件夹是否存在
     *
     * @param dirPath
     * @return
     */
    public static boolean isPath(String dirPath) {
        Path path = Paths.get(dirPath);
        if (Files.isReadable(path)) {
            return Files.isDirectory(path);
        }
        return false;
    }

    public static boolean checkFileExists(String filePath) {
        return checkFileExists(new File(filePath));
    }

    public static boolean checkFileExists(Path filePath) {
        return checkFileExists(new File(filePath.toUri()));
    }

    public static boolean checkFileExists(File file) {
        return file.exists();
    }

    public static boolean checkPath(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 判断文件是否有读权限
     *
     * @param file
     * @return
     */
    public static Boolean canRead(File file) {
        if (file.isDirectory()) {
            try {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        } else if (!file.exists()) {
            return false;
        }
        return checkRead(file);
    }

    /**
     * 检测文件/文件夹是否有读权限
     *
     * @param file
     * @return
     */
    private static boolean checkRead(File file) {
        try (FileReader fd = new FileReader(file)) {
            while ((fd.read()) != -1) {
                break;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断文件/文件夹是否有写权限
     *
     * @param file
     * @return
     */
    public static Boolean canWrite(File file) {
        if (file.isDirectory()) {
            try {
                file = new File(file, "canWriteTestDeleteOnExit.temp");
                if (file.exists()) {
                    boolean checkWrite = checkWrite(file);
                    if (!deleteFile(file)) {
                        file.deleteOnExit();
                    }
                    return checkWrite;
                } else if (file.createNewFile()) {
                    if (!deleteFile(file)) {
                        file.deleteOnExit();
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return checkWrite(file);
    }

    /**
     * 检测文件是否有写权限
     *
     * @param file
     * @return
     */
    private static boolean checkWrite(File file) {
        boolean delete = !file.exists();
        boolean result = false;
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write("");
            fw.flush();
            result = true;
            return result;
        } catch (IOException e) {
            return false;
        } finally {
            if (delete && result) {
                deleteFile(file);
            }
        }
    }

    /**
     * 删除文件，如果要删除的对象是文件夹，先删除所有子文件(夹)，再删除该文件
     *
     * @param file 要删除的文件对象
     * @return 删除是否成功
     */
    public static boolean deleteFile(File file) {
        return deleteFile(file, true);
    }

    /**
     * 删除文件，如果要删除的对象是文件夹，则根据delDir判断是否同时删除文件夹
     *
     * @param file   要删除的文件对象
     * @param delDir 是否删除目录
     * @return 删除是否成功
     */
    public static boolean deleteFile(File file, boolean delDir) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else {
            boolean result = true;
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                result = deleteFile(children[i], delDir);
                if (!result) {
                    return false;
                }
            }
            if (delDir) {
                result = file.delete();
            }
            return result;
        }
    }

    public static String getFilePath(String fileNewName) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dailyFolder = formatter.format(LocalDateTime.now());
        String path = source + File.separator + dailyFolder;
        if (!FileTool.checkPath(path)) {
            throw new Exception("每日文件夹初始化失败：" + dailyFolder);
        }
        String filePath = source + File.separator + dailyFolder + File.separator + fileNewName;

        return filePath;
    }

}
