package com.j3mall.user.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.j3mall.user.mybatis.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    User queryUserById(@Param("id") Integer id);

}
