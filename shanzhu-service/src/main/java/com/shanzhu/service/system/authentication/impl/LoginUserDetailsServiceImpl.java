package com.shanzhu.service.system.authentication.impl;

import com.shanzhu.config.LihuaConfig;
import com.shanzhu.model.security.CurrentUser;
import com.shanzhu.model.security.LoginUser;
import com.shanzhu.mapper.system.SysUserMapper;
import com.shanzhu.utils.date.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CurrentUser user = sysUserMapper.loginSelect(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        // 创建 LoginUser 包含登陆的用户信息 和 过期时间
        return new LoginUser(user, DateUtils.now().plusMinutes(lihuaConfig.getTokenExpireTime()));
    }
}

