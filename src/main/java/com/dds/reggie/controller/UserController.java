package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.R;
import com.dds.reggie.entity.User;
import com.dds.reggie.service.UserService;
import com.dds.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    //发送验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        //获得手机号
        String phone = user.getPhone();
        //生成4为随机验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        //发送短信(略)
        log.info("此次验证码为 : {}",code);

        //将验证码存入session中
        //request.getSession().setAttribute(phone,code);

        //优化：将验证码存入redis数据库中
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        return R.success("短信发送成功！");
    }

    //用户登录
    @PostMapping("/login")
    public R<String> login(@RequestBody Map<Object,Object> map,HttpServletRequest request){
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

        ValueOperations vos = redisTemplate.opsForValue();

        //对比验证码:优化为从redis数据库中取验证码
        if(code.equals(vos.get(phone))){
            //查询该phone用户是否存在
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("phone",phone);
            List<User> byPhone = userService.getByPhone(qw);
            Long userId = null;
            if(byPhone.size() == 0){
                //新用户保存
                User user = new User();
                user.setPhone(phone);
                user.setName("新用户的名字一律为丁东升");
                userService.save(user);
                userId = user.getId();
            }else{
                userId = byPhone.get(0).getId();
            }

            request.getSession().setAttribute("user",userId);

            //优化：登录成功后需要将验证码立即删除
            redisTemplate.delete(phone);

            return R.success("登录成功");

        }
        return R.error("登录失败");
    }


    //用户注销
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出登录成功!");
    }

}