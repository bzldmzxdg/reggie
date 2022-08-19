package com.dds.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.User;

import java.util.List;

public interface UserService {

    //根据手机号码查询用户是否存在
    public List<User> getByPhone(QueryWrapper<User> qw);

    //保存用户
    public Integer save(User user);

    //根据用户id查询用户信息
    public User getById(Long userId);
}
