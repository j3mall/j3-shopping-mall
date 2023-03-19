package com.j3mall.product.decorator;

import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.user.UserFeignService;
import com.j3mall.modules.feign.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDecorator {

    @Autowired
    private final UserFeignService userFeignService;

    public UserVO queryUserById(Integer userId, String dsName) {
        JsonResult<UserVO> jsonResult = userFeignService.queryUserById(userId, dsName);
        return jsonResult.getBody();
    }

}
