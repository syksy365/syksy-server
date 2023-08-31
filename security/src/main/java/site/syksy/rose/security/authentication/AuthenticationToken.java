package site.syksy.rose.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author Raspberry
 * 默认身份令牌
 */
public class AuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 主要数据（用户名、用户ID等）
     */
    private Map<String, Object> principal;

    /**
     * 证书数据（例如密码、密钥、认证码等）
     */
    private Map<String, Object> credentials;

    public AuthenticationToken(Map<String, Object> principal, Map<String, Object> credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, Map<String, Object> principal, Map<String, Object> credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
