package com.j3mall.user.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.j3mall.user.mybatis.domain.User;

public interface UserService extends IService<User> {

    User queryUserById(Integer id);

}
