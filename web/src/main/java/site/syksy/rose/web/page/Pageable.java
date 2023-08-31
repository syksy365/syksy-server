package site.syksy.rose.web.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 对ant design pro table 查询对象
 * @author Raspberry
 */
@Schema(title = "分页查询条件")
public class Pageable {

    private static final String UNDERSCORE = "_";

    @Schema(title = "当前页码")
    private Integer current;

    @Schema(title = "分页大小")
    private Integer pageSize;

    @Schema(title = "排序")
    private Sorter sorter;

    @Schema(title = "过滤")
    private Map<String, List<String>> filter;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Sorter getSorter() {
        if (this.sorter == null) {
            this.sorter = new Sorter();
            sorter.setCondition(false);
            sorter.setAsc(false);
        }
        return sorter;
    }

    public void setSorter(String sorterStr) {
        this.sorter = new Sorter();
        JsonNode sorterNode;
        try {
            if (sorterStr != null) {
                ObjectMapper mapper = new ObjectMapper();
                sorterNode = mapper.readTree(sorterStr);
                if (sorterNode == null || sorterNode.isEmpty()) {
                    sorter.setCondition(false);
                    sorter.setAsc(false);
                } else {
                    JsonNode jsonNode = sorterNode.elements().next();
                    String createTimeSorter = jsonNode.asText();
                    sorter.setCondition(true);
                    sorter.setValue(humpToUnderline(sorterNode.fieldNames().next()));
                    sorter.setAsc("ascend".equals(createTimeSorter));
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("排序参数JSON解析错误");
        }
    }

    public Map<String, List<String>> getFilter() {
        if (this.filter == null) {
            this.filter = new HashMap<>(1);
        }
        return filter;
    }

    public void setFilter(String filterStr) {
        this.filter = new HashMap<>(1);
        try {
            if (filterStr != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode sorterNode = mapper.readTree(filterStr);
                if (sorterNode != null && !sorterNode.isEmpty() && sorterNode.isObject()) {
                    Iterator<Map.Entry<String, JsonNode>> it = sorterNode.fields();
                    while (it.hasNext()) {
                        Map.Entry<String, JsonNode> entry = it.next();
                        JsonNode valuesNode = entry.getValue();
                        if (valuesNode != null && valuesNode.isArray()) {
                            List<String> valueList = new ArrayList<>();
                            for (JsonNode jsonNode : valuesNode) {
                                String text = jsonNode.asText();
                                if("true".equals(text)){
                                    valueList.add("1");
                                }else if("false".equals(text)){
                                    valueList.add("0");
                                }else{
                                    valueList.add(jsonNode.asText());
                                }
                            }
                            filter.put(humpToUnderline(entry.getKey()), valueList);
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("过滤参数JSON解析错误");
        }
    }

    public static class Sorter {
        private Boolean condition;

        private Boolean asc;

        private String value;

        public Boolean getCondition() {
            return condition;
        }

        public void setCondition(Boolean condition) {
            this.condition = condition;
        }

        public Boolean getAsc() {
            return asc;
        }

        public void setAsc(Boolean asc) {
            this.asc = asc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 驼峰命名转为下划线命名
     *
     * @param para 驼峰命名参数
     * @return 下划线命名
     */
    private String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        if (!para.contains(UNDERSCORE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERSCORE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase(Locale.ROOT);
    }
}
