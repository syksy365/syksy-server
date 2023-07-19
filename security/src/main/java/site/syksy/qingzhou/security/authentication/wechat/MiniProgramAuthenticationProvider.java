package site.syksy.qingzhou.security.authentication.wechat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import site.syksy.qingzhou.security.authentication.AuthenticationToken;
import site.syksy.qingzhou.security.constant.MiniProgramConstant;
import site.syksy.qingzhou.upms.service.UserService;

import java.util.Map;

/**
 * @author Raspberry
 * 微信小程序身份认证供应器
 */
public class MiniProgramAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public MiniProgramAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Map<String, Object> credentials = (Map<String, Object>) authentication.getCredentials();

        Map<String, String> userMap = userService.getUserMapByCode(credentials.get(MiniProgramConstant.CODE).toString());

        String username = userMap.get(MiniProgramConstant.USERNAME);
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationServiceException("账户不存在。");
        }

        principal.put(MiniProgramConstant.USER_ID, userMap.get(MiniProgramConstant.USER_ID));
        principal.put(MiniProgramConstant.USERNAME, userMap.get(MiniProgramConstant.USERNAME));
        principal.put(MiniProgramConstant.OPENID, userMap.get(MiniProgramConstant.OPENID));
        principal.put(MiniProgramConstant.UNIONID, userMap.get(MiniProgramConstant.UNIONID));
        principal.put(MiniProgramConstant.SESSION_KEY, userMap.get(MiniProgramConstant.SESSION_KEY));

        return new AuthenticationToken(null, principal, credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.equals(AuthenticationToken.class)) {
            return true;
        } else {
            return false;
        }
    }
}
