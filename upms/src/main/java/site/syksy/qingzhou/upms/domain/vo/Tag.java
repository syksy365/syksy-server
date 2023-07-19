package site.syksy.qingzhou.upms.domain.vo;

import java.io.Serializable;

/**
 * @author xyj
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;
    private String label;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
