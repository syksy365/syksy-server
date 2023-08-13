package site.syksy.qingzhou.file.domain;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author wuguipeng
 * @date 2023/8/6 15:58
 */
public class FileDO {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件路径
     */
    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
