package com.j3mall.user.decorator;

import com.j3mall.j3.framework.constants.DataSourceEnum;
import com.j3mall.user.vo.UserVO;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class UserDecoratorTest {

    @Resource
    private UserDecorator userDecorator;
    @Resource
    private TransactionExecutor transactionExecutor;

    /**
     * 测试通过的事务组合有 [REQUIRED, REQUIRED_NEW]
     */
    @Test
    void testTransitional() {
        Integer[] userIds = new Integer[]{2, 3};
        transactionExecutor.doInTransaction(userIds);
        log.info("事务执行完毕，确认数据");
        queryByIdWithAssert(userIds[0], DataSourceEnum.MASTER.getValue());
        queryByIdWithAssert(userIds[0], DataSourceEnum.SLAVE.getValue());

        queryByIdWithAssert(userIds[1], DataSourceEnum.MASTER.getValue());
        queryByIdWithAssert(userIds[1], DataSourceEnum.SLAVE.getValue());
    }

    private void queryByIdWithAssert(int userId, String dsName) {
        UserVO userVO = userDecorator.queryByIdByDs(userId, dsName);
        Assertions.assertTrue(userVO.getName().contains(userId + dsName),
            String.format("数据源%s用户ID%s数据异常为 [%s]", dsName, userId, userVO.getName()));
    }

}
