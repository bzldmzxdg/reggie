package com.dds.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Employee;
import com.dds.reggie.entity.R;

public interface EmployeeService {
    //根据用户名获取用户信息
    public Employee getEmployeeByUsername(String username);

    //新增用户
    public Integer save(Employee employee);

    //分页查询
    public Page<Employee> page(Page<Employee> pageInfo, QueryWrapper<Employee> qw);

    //修改用户
    public Integer update(Employee employee);

    //根据id获取用户信息
    public Employee getById(Long id);
}
