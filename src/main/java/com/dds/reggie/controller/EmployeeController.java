package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.MyRuntimeException;
import com.dds.reggie.entity.Employee;
import com.dds.reggie.entity.R;
import com.dds.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
//        1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        2、根据页面提交的用户名username查询数据库
        Employee employeeByUsername = employeeService.getEmployeeByUsername(employee.getUsername());

//        3、如果没有查询到则返回登录失败结果
        if(employeeByUsername == null){
            return R.error("用户名不存在！");
        }
//        4、密码比对，如果不一致则返回登录失败结果
        if(!employeeByUsername.getPassword().equals(password)){
            return R.error("密码错误！");
        }
//        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(employeeByUsername.getStatus() != 1){
            return R.error("你被封号了小伙子！");
        }
//        6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",employeeByUsername.getId());
        return R.success(employeeByUsername);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return R.success("退出成功，下次再来！");

    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        System.out.println(employeeService.save(employee));


        return R.success("新增员工成功");
    }

    /**
     * 员工查询全部并分页功能
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        //添加过滤条件
        qw.like(StringUtils.isNotEmpty(name),"name",name);
        //添加排序条件
        qw.orderByDesc("update_time");
        //执行查询
        employeeService.page(pageInfo, qw);
        return R.success(pageInfo);
    }

    /**
     * 修改用户信息通用（修改用户状态功能，自定义修改用户信息）
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){

        log.info("employee : {}",employee);

        //获取当前用户id
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //修改目标用户的更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        //修改目标用户的更改者
        //employee.setUpdateUser(empId);

        //调用service层方法执行修改
        employeeService.update(employee);

        return R.success("修改用户信息成功！");
    }

    /**
     * 根据id获取员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        //调用service层方法执行根据id获取用户信息
        Employee employee = employeeService.getById(id);

        if(employee == null){
            return R.error("没有查询到该用户信息！");
        }
        return R.success(employee);
    }
}
