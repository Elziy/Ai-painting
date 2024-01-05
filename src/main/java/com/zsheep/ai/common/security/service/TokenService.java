package com.zsheep.ai.common.security.service;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.constants.Constants;
import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.config.auth.AuthProperties;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.ServletUtils;
import com.zsheep.ai.utils.StringUtils;
import com.zsheep.ai.utils.ip.AddressUtils;
import com.zsheep.ai.utils.ip.IpUtils;
import com.zsheep.ai.utils.uuid.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author Elziy
 */
@Component
public class TokenService {
    @Resource
    private AuthProperties authProperties;
    
    @Resource
    private RedisCache redisCache;
    
    protected static final long MILLIS_SECOND = 1000;
    
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    
    private static final Long MILLIS_MINUTE_TEN = 10 * 60 * 1000L;
    
    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getLoginUserFromRedis(token);
    }
    
    public List<LoginUser> getAllSessionByUserId(Long userId) {
        return redisCache.getMultiCacheObject(CacheConstants.LOGIN_TOKEN_KEY + userId + CacheConstants.SEPARATOR + "*");
    }
    
    /**
     * 从redis获取登录用户
     *
     * @param token 令牌
     * @return {@link LoginUser} 用户信息,如果没有返回null
     */
    private LoginUser getLoginUserFromRedis(String token) {
        if (StringUtils.isNotBlank(token)) {
            try {
                // 解析token
                Claims claims = parseToken(token);
                String issuer = claims.getIssuer();
                // 校验token是否有效
                if (!authProperties.getIssuer().equals(issuer)) {
                    return null;
                }
                // 解析对应的权限以及用户信息
                String session = claims.get(Constants.LOGIN_USER_KEY, String.class);
                Long userId = claims.get(Constants.LOGIN_USER_ID_KEY, Long.class);
                String redisKey = getRedisTokenKey(userId, session);
                return redisCache.getCacheObject(redisKey);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
    
    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return {@link LoginUser}
     */
    public LoginUser createToken(LoginUser loginUser) {
        String session = IdUtils.fastUUID();
        loginUser.setSession(session);
        setUserAgent(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, session);
        claims.put(Constants.LOGIN_USER_ID_KEY, loginUser.getUserId());
        String jwtToken = createToken(claims);
        loginUser.setToken(jwtToken);
        
        refreshToken(loginUser);
        // 清除密码等敏感信息
        loginUser.getUser().setPassword(null);
        loginUser.getUser().setRoles(null);
        return loginUser;
    }
    
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(authProperties.getIssuer())
                .setIssuedAt(DateUtils.now())
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
    }
    
    /**
     * 验证令牌有效期，相差不足10分钟，自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }
    
    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + authProperties.getExpiration() * MILLIS_MINUTE);
        String redisTokenKey = getRedisTokenKey(loginUser.getUserId(), loginUser.getSession());
        redisCache.setCacheObject(redisTokenKey, loginUser, authProperties.getExpiration(), TimeUnit.MINUTES);
    }
    
    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOs().getName());
    }
    
    public SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(authProperties.getSecret());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
    
    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 获取请求token
     *
     * @param request 请求信息
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(authProperties.getTokenHeaderKey());
        return getToken(token);
    }
    
    /**
     * 获取真实token(去掉前缀)
     *
     * @param token 令牌
     * @return {@link String}
     */
    private String getToken(String token) {
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }
    
    /**
     * 获取redis存储的key
     *
     * @param uuid uuid
     * @return {@link String}
     */
    private String getRedisTokenKey(Long userId, String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + userId + CacheConstants.SEPARATOR + uuid;
    }
    
    /**
     * 删除用户身份信息
     */
    public void delLoginUser(Long userId, String session) {
        if (StringUtils.isNotEmpty(session)) {
            String userKey = getRedisTokenKey(userId, session);
            redisCache.deleteObject(userKey);
        }
    }
}
