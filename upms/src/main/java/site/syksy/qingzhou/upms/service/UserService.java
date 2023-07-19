package site.syksy.qingzhou.upms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syksy.qingzhou.common.event.RemoveUserEvent;
import site.syksy.qingzhou.common.utils.BCrypt;
import site.syksy.qingzhou.upms.domain.UserDO;
import site.syksy.qingzhou.upms.domain.dto.MiniProgramSessionDTO;
import site.syksy.qingzhou.upms.domain.vo.MiniProgramCodeVO;
import site.syksy.qingzhou.upms.mapper.UserMapper;
import site.syksy.qingzhou.web.exception.ForbiddenException;
import site.syksy.qingzhou.web.exception.NotFoundException;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_user", keyGenerator = "cacheKeyGenerator")
public class UserService extends ServiceImpl<UserMapper, UserDO> implements ApplicationContextAware {

    @Resource
    UserRoleService userRoleService;

    @Resource
    MiniProgramService miniProgramService;

    @Lazy
    @Autowired
    OrgService orgService;

    private ApplicationContext applicationContext;

    @CacheEvict(allEntries = true)
    @Override
    public boolean save(UserDO entity) {
        if (getByUsername(entity.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (orgService.getById(entity.getOrgId()) == null) {
            throw new RuntimeException("组织不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        String password = BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt());
        entity.setPassword(password);
        return super.save(entity);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(UserDO entity) {
        if (orgService.getById(entity.getOrgId()) == null) {
            throw new RuntimeException("组织不存在");
        }
        UserDO oldUser = getByUsername(entity.getUsername());
        if (oldUser != null && !oldUser.getId().equals(entity.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        return super.updateById(entity);
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        userRoleService.removeByUserId(id);
        publisherEvent(new RemoveUserEvent(id));
        return super.removeById(id);
    }

    /**
     * 根据组织ID统计用户数
     *
     * @param orgId
     * @return
     */
    @Cacheable
    public long countUserByOrgId(String orgId) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getOrgId, orgId);
        return count(queryWrapper);
    }

    /**
     * 根据用户名查找用户信息
     *
     * @param username
     * @return
     */
    @Cacheable
    public UserDO getByUsername(String username) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        return userDO;
    }

    /**
     * 根据用户名查找用户密码
     *
     * @param username
     * @return
     */
    @Cacheable
    public Map<String, String> getUserMapByUsername(String username) {
        UserDO userDO = getByUsername(username);
        Map<String, String> map = new HashMap<>(3);
        if (userDO != null) {
            map.put("userId", userDO.getId());
            map.put("username", userDO.getUsername());
            map.put("password", userDO.getPassword());
        }
        return map;
    }

    /**
     * 使用微信小程序临时登录凭证code，换取系统中用户信息以及微信服务器端的会话key等信息
     *
     * @param code
     * @return
     */
    public Map<String, String> getUserMapByCode(String code) {
        MiniProgramSessionDTO miniProgramSessionDTO = miniProgramService.code2Session(code);
        UserDO userDO = getByOpenid(miniProgramSessionDTO.getOpenid());
        Map<String, String> map = new HashMap<>(4);
        if (userDO != null) {
            map.put("userId", userDO.getId());
            map.put("username", userDO.getUsername());
            map.put("openid", miniProgramSessionDTO.getOpenid());
            map.put("sessionKey", miniProgramSessionDTO.getSessionKey());
        }
        return map;
    }

    public UserDO getByOpenid(String openid) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getOpenid, openid);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        return userDO;
    }

    /**
     * 微信小程序绑定账户
     *
     * @param miniProgramCodeVO
     */
    public void miniProgramBinding(MiniProgramCodeVO miniProgramCodeVO) {
        MiniProgramSessionDTO miniProgramSessionDTO = miniProgramService.code2Session(miniProgramCodeVO.getCode());
        UserDO userDO = getByOpenid(miniProgramSessionDTO.getOpenid());
        if (userDO != null) {
            throw new ForbiddenException("此微信小程序已绑定账户，如需绑定新账户，请先解绑。");
        }

        userDO = getByUsername(miniProgramCodeVO.getUsername());
        if (userDO == null) {
            throw new ForbiddenException("要绑定的账户不存在。");
        }
        if (BCrypt.checkpw(miniProgramCodeVO.getPassword(), userDO.getPassword())) {
            UpdateWrapper<UserDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(UserDO::getOpenid, miniProgramSessionDTO.getOpenid())
                    .eq(UserDO::getId, userDO.getId());
            update(updateWrapper);
        } else {
            throw new ForbiddenException("密码错误。");
        }
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(Wrapper<UserDO> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Cacheable
    @Override
    public UserDO getById(Serializable id) {
        return super.getById(id);
    }

    @Cacheable
    @Override
    public <E extends IPage<UserDO>> E page(E page, Wrapper<UserDO> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param password
     */
    @CacheEvict(allEntries = true)
    public void updatePassword(String userId, String password) {
        UpdateWrapper<UserDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(UserDO::getPassword, BCrypt.hashpw(password, BCrypt.gensalt()))
                .eq(UserDO::getId, userId);
        if (!update(updateWrapper)) {
            throw new NotFoundException();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 发布事件
     * 监听该事件的监听者都可以获取消息
     *
     * @param removeUserEvent
     */
    public void publisherEvent(RemoveUserEvent removeUserEvent) {
        applicationContext.publishEvent(removeUserEvent);
    }
}
