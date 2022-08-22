package com.dds.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Setmeal;
import com.dds.reggie.entity.SetmealDto;

import java.util.List;

public interface SetmealService {

    //根据category_id查询条目总数
    public Long selectCountByCategoryId(Long category_id);

    //保存套餐信息及其套餐内菜品信息
    public void save(SetmealDto setmealDto);

    //修改套餐信息及其套餐内菜品信息
    public void update(SetmealDto setmealDto);

    //查询套餐信息并分页
    public void page(Page<Setmeal> pageInfo, QueryWrapper<Setmeal> qw);

    //根据套餐id删除套餐及其套餐内菜品（可能多个id）
    public void delete(List<Long> ids);

    //根据套餐分类id获取所有启用的套餐信息
    public List<Setmeal> getAllByCategoryId(Setmeal setmeal);

    //根据套餐id修改套餐售卖状态
    public void updateStatus(Integer codeStatus,List<Long> setmealIds);

    //根据套餐id获取套餐及其套餐内菜品信息
    public SetmealDto getById(Long id);
}
