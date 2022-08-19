package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.User;
import com.dds.reggie.mapper.UserMapper;
import com.dds.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> getByPhone(QueryWrapper<User> qw) {
        return userMapper.selectList(qw);
    }

    @Override
    public Integer save(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
}
