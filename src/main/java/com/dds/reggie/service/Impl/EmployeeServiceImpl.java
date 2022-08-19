package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Employee;
import com.dds.reggie.entity.R;
import com.dds.reggie.mapper.EmployeeMapper;
import com.dds.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public Employee getEmployeeByUsername(String username) {

        QueryWrapper<Employee> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(username),"username",username);
        Employee employee = employeeMapper.selectOne(qw);

        return employee;
    }

    @Override

    public Integer save(Employee employee) {

        int insert = employeeMapper.insert(employee);

        return insert;
    }

    @Override
    public Page<Employee> page(Page<Employee> pageInfo, QueryWrapper<Employee> qw) {

        Page<Employee> employeePage = employeeMapper.selectPage(pageInfo, qw);
        return employeePage;

    }

    @Override
    public Integer update(Employee employee) {
        return employeeMapper.updateById(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }
}
