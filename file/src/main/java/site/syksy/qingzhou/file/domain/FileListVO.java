package site.syksy.qingzhou.file.domain;

import java.util.List;

/**
 * @author wuguipeng
 * @date 2023/8/6 20:33
 */
public class FileListVO {

    /**
     * 文件列表
     */
    private List<FileVO> fileList;

    /**
     * 父文件
     */
    private FileVO parentReference;


    public List<FileVO> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileVO> fileList) {
        this.fileList = fileList;
    }

    public FileVO getParentReference() {
        return parentReference;
    }

    public void setParentReference(FileVO parentReference) {
        this.parentReference = parentReference;
    }
}
