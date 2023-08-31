package site.syksy.rose.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.common.utils.BCrypt;
import site.syksy.rose.upms.domain.RoleDO;
import site.syksy.rose.upms.domain.UserDO;
import site.syksy.rose.upms.domain.UserRoleDO;
import site.syksy.rose.upms.domain.vo.CurrentUserVO;
import site.syksy.rose.upms.domain.vo.MiniProgramCodeVO;
import site.syksy.rose.upms.domain.vo.PasswordVO;
import site.syksy.rose.upms.domain.vo.UserVO;
import site.syksy.rose.upms.service.OrgService;
import site.syksy.rose.upms.service.RoleService;
import site.syksy.rose.upms.service.UserRoleService;
import site.syksy.rose.upms.service.UserService;
import site.syksy.rose.web.exception.ForbiddenException;
import site.syksy.rose.web.exception.NotFoundException;
import site.syksy.rose.web.exception.PreconditionFailedException;
import site.syksy.rose.web.page.MyPage;
import site.syksy.rose.web.page.Pageable;
import site.syksy.rose.web.page.PageableAsQueryParam;
import site.syksy.rose.web.resolver.UserId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "用户")
@RequestMapping("upms/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    OrgService orgService;

    @Autowired
    RoleService roleService;

    final static String PASSWORD = "qz";

    @PostMapping
    @Operation(summary = "新建")
    @Transactional(rollbackFor = Exception.class)
    public void post(@Validated @RequestBody UserVO userVO) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userVO, userDO);
        userDO.setOrgId(userVO.getOrgIds().get(userVO.getOrgIds().size() - 1));
        userDO.setPassword(PASSWORD);
        userService.save(userDO);
        userRoleService.saveUserRole(userDO.getId(), userVO.getRoleIds());
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id,@UserId String userId) {
        if(id.equals(userId)){
            throw new ForbiddenException("不允许删除当前用户本身");
        }
        if (!userService.removeById(id)) {
            throw new NotFoundException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @PutMapping("{id}")
    @Operation(summary = "用户信息修改")
    public void put(@PathVariable String id,@Validated @RequestBody UserVO userVO) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userVO, userDO);
        userDO.setOrgId(userVO.getOrgIds().get(userVO.getOrgIds().size() - 1));
        userDO.setId(id);
        userService.updateById(userDO);
        userRoleService.saveUserRole(id, userVO.getRoleIds());
    }

    @PutMapping("password")
    @Operation(summary = "修改密码")
    public void editPassword(@Parameter(hidden = true) @UserId String userId,@Validated @RequestBody PasswordVO passwordVO) {
        UserDO userDO = userService.getById(userId);
        if (BCrypt.checkpw(passwordVO.getOldPassword(),userDO.getPassword())) {
            userService.updatePassword(userId, passwordVO.getNewPassword());
        } else {
            throw new PreconditionFailedException("原密码错误");
        }
    }

    @PutMapping("/password/{id}")
    @Operation(summary = "密码重置")
    public void resetPassword(@PathVariable String id) {
        userService.updatePassword(id, PASSWORD);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public UserVO get(@PathVariable String id) {
        UserDO userDO = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        userVO.setOrgIds(orgService.findLink(userDO.getOrgId()));
        userVO.setRoleIds(userRoleService.selectRoleIdsByUserId(id));
        return userVO;
    }

    @GetMapping("current")
    @Operation(summary = "获取当前用户信息")
    public CurrentUserVO getCurrentUser(@Parameter(hidden = true) @UserId String userId) {
        UserDO userDO = userService.getById(userId);
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setUserid(userDO.getId());
        currentUserVO.setName(userDO.getUsername());
        currentUserVO.setAccess("admin");
        currentUserVO.setGroup("科技组");
        currentUserVO.setTitle("员工");
        currentUserVO.setUnreadCount(10);
        currentUserVO.setSignature("凛冬将至");
        currentUserVO.setAvatar("https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        List<site.syksy.rose.upms.domain.vo.Tag> tags = new ArrayList<>();
        site.syksy.rose.upms.domain.vo.Tag tag = new site.syksy.rose.upms.domain.vo.Tag();
        tag.setKey("1");
        tag.setLabel("机智");
        tags.add(tag);
        currentUserVO.setTags(tags);
        currentUserVO.setEmail("antdesign@alipay.com");
        currentUserVO.setCountry("China");
        currentUserVO.setAddress("西湖区工专路 77 号");
        currentUserVO.setPhone("0752-268888888");

        return currentUserVO;
    }

    @GetMapping("list")
    @Operation(summary = "分页")
    @PageableAsQueryParam
    public MyPage listToList(
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String orgId,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String notRoleId
            ) {
        MyPage userDOPage = new MyPage<>(pageable);
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(username), UserDO::getUsername, username)
                .like(StringUtils.isNotBlank(email), UserDO::getEmail, email)
                .like(StringUtils.isNotBlank(phone), UserDO::getPhone, phone);

        if (StringUtils.isNotBlank(orgId)) {
            List<String> orgIdList = orgService.findToLast(orgId);
            queryWrapper.lambda().in(orgIdList != null && orgIdList.size() > 0, UserDO::getOrgId, orgIdList);
        }

        queryWrapper.lambda().inSql(StringUtils.isNotBlank(roleId), UserDO::getId, "SELECT user_id FROM s_upms_user_role where role_id = " + roleId);

        queryWrapper.lambda().notInSql(StringUtils.isNotBlank(notRoleId), UserDO::getId, "SELECT user_id FROM s_upms_user_role where role_id = " + notRoleId);

        for (Map.Entry<String, List<String>> entry : pageable.getFilter().entrySet()) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        }
        queryWrapper.orderBy(pageable.getSorter().getCondition(), pageable.getSorter().getAsc(), pageable.getSorter().getValue());
        userDOPage = userService.page(userDOPage, queryWrapper);
        userDOPage.convert(i -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(i, userVO);
            List<String> roleIds = new ArrayList<>();
            Map<String, String> roles = new HashMap<>(3);
            List<UserRoleDO> userRoleDOList = userRoleService.selectRoleByUserId(userVO.getId());
            for (UserRoleDO userRoleDO : userRoleDOList) {
                RoleDO roleDO = roleService.getById(userRoleDO.getRoleId());
                roleIds.add(roleDO.getId());
                roles.put(roleDO.getId(), roleDO.getName());
            }

            userVO.setRoleIds(roleIds);
            userVO.setRoles(roles);

            List<String> orgIds = new ArrayList<>();
            Map<String, String> orgs = new HashMap<>(3);
            orgService.findLink(userVO.getOrgId(), orgIds, orgs);
            userVO.setOrgIds(orgIds);
            userVO.setOrgs(orgs);
            return userVO;
        });
        return userDOPage;
    }

    @PostMapping("we-chat/mini-program")
    @Operation(summary = "微信小程序绑定账户")
    public void miniProgramBinding(@Validated @RequestBody MiniProgramCodeVO miniProgramCodeVO) {
        userService.miniProgramBinding(miniProgramCodeVO);
    }

}