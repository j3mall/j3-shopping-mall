package com.j3mall.user.decorator;

import com.j3mall.user.mybatis.domain.User;
import com.j3mall.user.mybatis.service.UserService;
import com.j3mall.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDecorator {

    @Autowired
    private UserService userService;

    @Cacheable(cacheNames = "micro:user", key="#id", condition = "#id ge 1", unless="#result == null")
    public UserVO queryById(int id) {
        UserVO userVO = new UserVO();
        User user = userService.getById(id);
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
