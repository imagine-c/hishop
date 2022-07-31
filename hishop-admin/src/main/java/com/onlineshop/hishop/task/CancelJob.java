package com.onlineshop.hishop.task;

import cn.hutool.core.date.DateUtil;
import com.onlineshop.hishop.domain.Lock;
import com.onlineshop.hishop.service.MemberService;
import com.onlineshop.hishop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CancelJob {

    final static Logger log= LoggerFactory.getLogger(CancelJob.class);

    @Autowired
    private DistributedLockHandler distributedLockHandler;

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;

    /**
     * 每5s执行一次
     */
    //    @Scheduled(cron = "*/5 * * * * ?")
    /**
     * 每1个小时判断订单是否失效
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void run() {
        Lock lock=new Lock("tasklock","taskvalue");
        if(distributedLockHandler.tryLock(lock)){
            log.info("定时任务 - " + DateUtil.now());
            orderService.cancelOrder();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void cancelUserDel() {
        Lock lock=new Lock("cancelUserDel","cancelUserDel");
        if(distributedLockHandler.tryLock(lock)){
            log.info("定时任务 - " + DateUtil.now());
            memberService.cancelUserDel();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void confirmOrder() {
        Lock lock=new Lock("confirmOrder","confirmOrder");
        if(distributedLockHandler.tryLock(lock)){
            orderService.confirmOrder();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void autoRefund() {
        Lock lock=new Lock("autoRefund","autoRefund");
        if(distributedLockHandler.tryLock(lock)){
            orderService.autoRefund();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void deliverEmail() {
        Lock lock=new Lock("deliverEmail","deliverEmail");
        if(distributedLockHandler.tryLock(lock)){
            orderService.deliverEmail();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

    @Scheduled(cron = "0 */30 * * * ?")
    public void userBlock() {
        Lock lock=new Lock("userblock","uservalue");
        if(distributedLockHandler.tryLock(lock)){
            log.info("执行了封号到时定时任务 - " + DateUtil.now());
            memberService.cancelUserBlock();
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            distributedLockHandler.releaseLock(lock);
        }
    }

}
