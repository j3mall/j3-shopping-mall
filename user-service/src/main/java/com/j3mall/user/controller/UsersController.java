package com.j3mall.user.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.user.decorator.UserDecorator;
import com.j3mall.user.mybatis.domain.User;
import com.j3mall.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户信息相关接口")
public class UsersController {

    @Autowired
    private UserDecorator userDecorator;

    @PostMapping("/")
    @ApiOperation("创建新用户")
    public JsonResult<Boolean> createUser(@RequestBody User user) {
        return JsonResult.success(userDecorator.saveUser(user));
    }

    @PutMapping("/{userId}")
    @ApiOperation("根据用户ID更新用户信息")
    public JsonResult<Boolean> updateUserById(@ApiParam(value = "用户ID") @PathVariable(KeyConstants.KEY_USERID) Integer userId,
        @RequestBody User user) {
        user.setId(userId);
        return JsonResult.success(userDecorator.updateUserById(user));
    }

    @GetMapping("/{userId}")
    @ApiOperation("根据用户ID获取用户信息")
    public JsonResult<UserVO> queryUserById(@ApiParam(value = "用户ID") @PathVariable(KeyConstants.KEY_USERID) Integer userId,
        @ApiParam(value = "指定数据源") @RequestParam(required = false, value = "dsName") String dsName) {
        UserVO userVO = Objects.nonNull(dsName) ?
            userDecorator.queryByIdByDs(userId, dsName.toLowerCase()) : userDecorator.queryByIdDefault(userId);
        log.debug("查询到用户信息 [{}, {}]，数据源为{}", userId, userVO.getName(), dsName);
        return JsonResult.success(userVO);
    }

}
