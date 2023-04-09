package com.j3mall.user.decorator;

// import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.j3mall.framework.datasource.annotation.DataSourceChange;
import com.j3mall.j3.framework.constants.DataSourceEnum;
import com.j3mall.user.mybatis.domain.User;
import java.util.Random;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class MasterTransaction {

    @Resource
    private UserDecorator userDecorator;
    @Resource
    private SlaveTransaction slaveTransaction;

    @DataSourceChange("master")
    // @DSTransactional
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void masterTransaction(Integer[] userIds) {
        User user = new User();
        user.setId(userIds[0]);
        user.setName(user.getId() + "master11" + new Random().nextInt(20));
        userDecorator.updateUserById(user, DataSourceEnum.MASTER.getValue());

        User user2 = new User();
        user2.setId(userIds[1]);
        user2.setName(user2.getId() + "master22" + new Random().nextInt(20));
        userDecorator.updateUserById(user2, DataSourceEnum.MASTER.getValue());

        slaveTransaction.slaveTransaction(userIds);
    }



}
