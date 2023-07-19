package site.syksy.qingzhou.common.properties;

/**
 * @author Raspberry
 * 上传文件目录配置
 */
public class FileDirectoryProperties {

    /**
     * tus块文件目录
     */
    private String tusUploadDirectory = "tus-upload-directory";

    /**
     * 应用文件目录
     */
    private String appUploadDirectory = "app-upload-directory";

    public String getTusUploadDirectory() {
        return tusUploadDirectory;
    }

    public void setTusUploadDirectory(String tusUploadDirectory) {
        this.tusUploadDirectory = tusUploadDirectory;
    }

    public String getAppUploadDirectory() {
        return appUploadDirectory;
    }

    public void setAppUploadDirectory(String appUploadDirectory) {
        this.appUploadDirectory = appUploadDirectory;
    }
}
