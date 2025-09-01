package com.shanzhu.service.system.user;

import com.shanzhu.entity.system.SysUserPost;

import java.util.List;

public interface SysUserPostService {

    void save(List<SysUserPost> sysUserPosts);

    void deleteByUserIds(List<String> userIds);


}
