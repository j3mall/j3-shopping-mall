package com.j3mall.user.decorator;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.j3mall.framework.datasource.annotation.DataSourceChange;
import com.j3mall.j3.framework.constants.DataSourceEnum;
import com.j3mall.user.mybatis.domain.User;
import com.j3mall.user.mybatis.service.UserService;
import com.j3mall.user.vo.UserVO;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDecorator {

    @Autowired
    private final UserService userService;

    public Boolean saveUser(User user) {
        log.info("用户信息保存ID{}, {}", user.getId(), JSON.toJSONString(user));
        return userService.saveOrUpdate(user);
    }

    /** 未使用注解，默认为 master数据源 **/
    // @DS("#dsName")
    @DataSourceChange("#dsName")
    public Boolean updateUserById(User user, String dsName) {
        log.info("-->{}用户信息更新ID{}, {}", dsName, user.getId(), JSON.toJSONString(user));
        return userService.updateById(user);
    }

    // @Cacheable(cacheNames = "micro:user", key="#id", condition = "#id ge 1", unless="#result == null")
    // @DS("master")
    // @DataSourceChange
    public UserVO queryByIdDefault(int id) {
        UserVO user = queryById(id);
        log.debug("默认数据源查询用户ID [{}, {}]", id, user.getName());
        return user;
    }

    // @DS("#dsName")
    @DataSourceChange("#dsName")
    public UserVO queryByIdByDs(int id, String dsName) {
        UserVO user = queryById(id);
        log.debug("-->{}数据源查询用户ID [{}, {}]", dsName, id, user.getName());
        return user;
    }

    public UserVO queryById(int id) {
        UserVO userVO = new UserVO();
        User user = userService.getById(id);
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
