package site.syksy.qingzhou.upms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raspberry
 */
@Schema(title = "前端适配菜单")
public class MenuFrontVO implements Serializable {

    private String key;

    @Schema(title = "路径")
    private String path;

    @Schema(title = "菜单名")
    private String name;

    @Schema(title = "图标")
    private String icon;

    @Schema(title = "在菜单中隐藏自己和子节点")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hideInMenu;

    @Schema(title = "操作指定高亮菜单，指定菜单的key或path")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> parentKeys;

    /**
     * @name false 时不展示顶栏
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean headerRender;

    /**
     * @name false 时不展示页脚
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean footerRender;

    /**
     * @name false 时不展示菜单
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean menuRender;

    /**
     * @name false 时不展示菜单顶栏
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean menuHeaderRender;

    /**
     * @name 固定顶栏
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean fixedHeader;

    /**
     * @name 固定菜单
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean fixSiderbar;

    /**
     * @name theme for nav menu
     * @name 导航菜单的主题
     * 'dark' | 'light' | 'realDark' | undefined
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String navTheme;

    /**
     * @name nav menu position: `side` or `top`
     * @name 导航菜单的位置
     * @description side 为正常模式，top菜单显示在顶部，mix 两种兼有
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String layout;

    /**
     * @name 顶部导航的主题，mix 模式生效
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String headerTheme;

    @Schema(title = "下级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuFrontVO> routes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(Boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public List<String> getParentKeys() {
        return parentKeys;
    }

    public void setParentKeys(List<String> parentKeys) {
        this.parentKeys = parentKeys;
    }

    public Boolean getHeaderRender() {
        return headerRender;
    }

    public void setHeaderRender(Boolean headerRender) {
        this.headerRender = headerRender;
    }

    public Boolean getFooterRender() {
        return footerRender;
    }

    public void setFooterRender(Boolean footerRender) {
        this.footerRender = footerRender;
    }

    public Boolean getMenuRender() {
        return menuRender;
    }

    public void setMenuRender(Boolean menuRender) {
        this.menuRender = menuRender;
    }

    public Boolean getMenuHeaderRender() {
        return menuHeaderRender;
    }

    public void setMenuHeaderRender(Boolean menuHeaderRender) {
        this.menuHeaderRender = menuHeaderRender;
    }

    public Boolean getFixedHeader() {
        return fixedHeader;
    }

    public void setFixedHeader(Boolean fixedHeader) {
        this.fixedHeader = fixedHeader;
    }

    public Boolean getFixSiderbar() {
        return fixSiderbar;
    }

    public void setFixSiderbar(Boolean fixSiderbar) {
        this.fixSiderbar = fixSiderbar;
    }

    public String getNavTheme() {
        return navTheme;
    }

    public void setNavTheme(String navTheme) {
        this.navTheme = navTheme;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getHeaderTheme() {
        return headerTheme;
    }

    public void setHeaderTheme(String headerTheme) {
        this.headerTheme = headerTheme;
    }

    public List<MenuFrontVO> getRoutes() {
        return routes;
    }

    public void setRoutes(List<MenuFrontVO> routes) {
        this.routes = routes;
    }
}
