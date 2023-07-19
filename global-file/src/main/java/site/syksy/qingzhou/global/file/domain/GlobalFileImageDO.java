package site.syksy.qingzhou.global.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import site.syksy.qingzhou.global.file.constant.ImageSizeType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Raspberry
 */
@TableName("s_global_file_image")
@Schema(title = "图片文件")
public class GlobalFileImageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Size(max = 45)
    @NotBlank
    @Schema(title = "文件ID")
    private Long fileId;

    @Size(max = 45)
    @NotBlank
    @Schema(title = "大小类型")
    private ImageSizeType sizeType;

    @Size(max = 128)
    @NotBlank
    @Schema(title = "文件uuid编号")
    private String uuid;

    @Size(max = 128)
    @NotBlank
    @Schema(title = "文件名")
    private String fileName;

    @Size(max = 255)
    @NotBlank
    @Schema(title = "文件路径")
    private String filePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public ImageSizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(ImageSizeType sizeType) {
        this.sizeType = sizeType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
