package com.zsheep.ai.common.core.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.zsheep.ai.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户唯一标识
     */
    private String token;
    
    /**
     * 会话
     */
    private String session;
    
    /**
     * 登录时间
     */
    private Long loginTime;
    
    private Long expireTime;
    
    /**
     * 登录IP地址
     */
    private String ipaddr;
    
    /**
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 权限列表
     */
    private List<String> permissions;
    
    /**
     * 用户信息
     */
    private User user;
    
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.userId = user.getId();
        this.permissions = permissions;
    }
    
    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    /**
     * 账户是否未过期,过期无法验证
     *
     * @return true
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return true
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return true
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return true
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    /**
     * 存储授予Authentication对象的权限的String表示形式
     */
    @JSONField(serialize = false)// 保存redis时不进行序列化
    private Set<SimpleGrantedAuthority> authorities;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userId == null) {
            return new HashSet<>();
        }
        if (!Objects.isNull(authorities)) {
            return authorities;
        }
        if (permissions != null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        return authorities;
    }
}
