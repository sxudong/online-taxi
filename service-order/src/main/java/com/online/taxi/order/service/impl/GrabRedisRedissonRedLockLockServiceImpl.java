package com.online.taxi.order.service.impl;

import com.online.taxi.common.constant.RedisKeyConstant;
import com.online.taxi.common.dto.ResponseResult;
import com.online.taxi.order.service.GrabService;
import com.online.taxi.order.service.OrderService;

import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author yueyi2019
 */
@Service("grabRedisRedissonRedLockLockService")
public class GrabRedisRedissonRedLockLockServiceImpl implements GrabService {

    @Autowired
    private RedissonClient redissonRed1;
    @Autowired
    private RedissonClient redissonRed2;
    @Autowired
    private RedissonClient redissonRed3;
    
    @Autowired
	OrderService orderService;

    @Override
    public ResponseResult grabOrder(int orderId , int driverId){
        //生成key
        String lockKey = (RedisKeyConstant.GRAB_LOCK_ORDER_KEY_PRE + orderId).intern();
        //redisson锁 哨兵
//        RLock rLock = redisson.getLock(lockKey);
//        rLock.lock();

        //redisson锁 单节点
//        RLock rLock = redissonRed1.getLock(lockKey);

        //红锁, 获取多个 RLock 对象
        RLock rLock1 = redissonRed1.getLock(lockKey);
        RLock rLock2 = redissonRed2.getLock(lockKey);
        RLock rLock3 = redissonRed3.getLock(lockKey);
        //据多个 RLock 对象构建 RedissonRedLock （最核心的差别就在这里）
        RedissonRedLock rLock = new RedissonRedLock(rLock1,rLock2,rLock3);

        rLock.lock();
        /**
         * 尝试获取锁
         * waitTimeout 尝试获取锁的最大等待时间，超过这个值，则认为获取锁失败
         * leaseTime   锁的持有时间,超过这个时间锁会自动失效（值应设置为大于业务处理的时间，确保在锁有效期内业务能处理完）
         */
//        boolean res = rLock.tryLock((long) waitTimeout, (long) leaseTime, TimeUnit.SECONDS);

        try {
//            if (res) {
//                //成功获得锁，在这里处理业务
//            }
    		// 此代码默认 设置key 超时时间30秒，过10秒，再延时
			System.out.println("司机:"+driverId+" 执行抢单逻辑");
			
            boolean b = orderService.grab(orderId, driverId);
            if(b) {
            	System.out.println("司机:"+driverId+" 抢单成功");
            }else {
            	System.out.println("司机:"+driverId+" 抢单失败");
            }
            
        } finally {
            //无论如何, 最后都要解锁
        	rLock.unlock();
        }
        return null;
    }
}
