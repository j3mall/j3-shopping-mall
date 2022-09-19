package com.j3mall.user.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.user.decorator.UserDecorator;
import com.j3mall.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Api(tags = "用户信息相关接口")
public class UsersController {

    @Autowired
    private UserDecorator userDecorator;

    @GetMapping("/{userId}")
    @ApiOperation("根据用户ID获取用户信息")
    public JsonResult<UserVO> queryUserById(@PathVariable(KeyConstants.KEY_USERID) Integer userId) {
        UserVO userVO = userDecorator.queryById(userId);
        return JsonResult.success(userVO);
    }

}
