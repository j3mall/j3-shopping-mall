package com.j3mall.user.decorator;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.j3mall.framework.datasource.annotation.DataSourceChange;
import com.j3mall.j3.framework.constants.DataSourceEnum;
import com.j3mall.user.mybatis.domain.User;
import java.util.Random;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class TransactionExecutor {

    @Resource
    private UserDecorator userDecorator;

    @DataSourceChange("master")
    // @DSTransactional
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void doInTransaction(Integer[] userIds) {
        User user = new User();
        user.setId(userIds[0]);
        user.setName(user.getId() + "master11" + new Random().nextInt(20));
        userDecorator.updateUserById(user, DataSourceEnum.MASTER.getValue());

        User user2 = new User();
        user2.setId(userIds[1]);
        user2.setName(user2.getId() + "master22" + new Random().nextInt(20));
        userDecorator.updateUserById(user2, DataSourceEnum.MASTER.getValue());

        TransactionExecutor proxy = (TransactionExecutor) AopContext.currentProxy();
        proxy.slaveTransaction(userIds);

        TransactionExecutor proxy2 = (TransactionExecutor) AopContext.currentProxy();
        proxy2.slaveTransaction2(userIds);
    }

    @DataSourceChange("slave")
    // @DSTransactional
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void slaveTransaction(Integer[] userIds) {
        User user = new User();
        user.setId(userIds[0]);
        user.setName(user.getId() + "slave33" + new Random().nextInt(20));
        userDecorator.updateUserById(user, DataSourceEnum.SLAVE.getValue());

        User user2 = new User();
        user2.setId(userIds[1]);
        user2.setName(user2.getId() + "slave44" + new Random().nextInt(20));
        userDecorator.updateUserById(user2, DataSourceEnum.SLAVE.getValue());
    }

    @DataSourceChange("slave")
    // @DSTransactional
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void slaveTransaction2(Integer[] userIds) {
        User user = new User();
        user.setId(userIds[0]);
        user.setName(user.getId() + "slave55" + new Random().nextInt(20));
        userDecorator.updateUserById(user, DataSourceEnum.SLAVE.getValue());
        // TransactionAspectSupport.currentTransactionStatus().createSavepoint();

        User user2 = new User();
        user2.setId(userIds[1]);
        user2.setName(user2.getId() + "slave66" + new Random().nextInt(20) + "this is too long too long too long");
        userDecorator.updateUserById(user2, DataSourceEnum.SLAVE.getValue());
    }

}
