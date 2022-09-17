//package com.onlineshop.hishop.aop;
//
//import com.onlineshop.hishop.annotation.SystemControllerLog;
//import com.onlineshop.hishop.annotation.SystemServiceLog;
//import com.onlineshop.hishop.pojo.TbLog;
//import com.onlineshop.hishop.com.onlineshop.hishop.service.SystemService;
//import com.onlineshop.hishop.utils.IPInfoUtil;
//import com.onlineshop.hishop.utils.ObjectUtil;
//import com.onlineshop.hishop.utils.ThreadPoolUtil;
//import lombok.extern.log4j.Log4j2;
//import org.apache.shiro.SecurityUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.NamedThreadLocal;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.Date;
//import java.util.Map;
//
//
//@Aspect
//@Configuration
//@Log4j2
//public class SystemLogAspect {
//
//
//    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
//
//    @Autowired
//    private SystemService systemService;
//
//    @Autowired(required=false)
//    private HttpServletRequest request;
//
//    /*
//     * Controller层切点,注解方式
//     */
//
//    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
//    @Pointcut("@annotation(com.onlineshop.hishop.annotation.SystemControllerLog)")
//    public void controllerAspect() {
//        log.info("========controllerAspect===========");
//    }
//
//    /*
//     * Service层切点,注解方式
//     */
//
//    @Pointcut("@annotation(com.onlineshop.hishop.annotation.SystemServiceLog)")
//    public void serviceAspect() {
//        log.info("========ServiceAspect===========");
//    }
//
//
//    /*
//     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
//     * @param joinPoint 切点
//     * @throws InterruptedException
//     */
//
//    @Before("controllerAspect()")
//    public void doBefore(JoinPoint joinPoint) throws InterruptedException{
//
//        //线程绑定变量（该数据只有当前请求的线程可见）
//        Date beginTime=new Date();
//        beginTimeThreadLocal.set(beginTime);
//    }
//
//
//    /*
//     * 后置通知(在方法执行之后返回) 用于拦截Controller层操作
//     * @param joinPoint 切点
//     */
//
//    @After("controllerAspect()")
//    public void after(JoinPoint joinPoint){
//        try {
//            String username= SecurityUtils.getSubject().getPrincipal().toString();
//
//            if (null != username) {
//
//                TbLog tbLog=new TbLog();
//
//                //日志标题
//                tbLog.setName(getControllerMethodDescription(joinPoint));
//                //日志类型
//                tbLog.setType(1);
//                //日志请求url
//                tbLog.setUrl(request.getRequestURI());
//                //请求方式
//                tbLog.setRequestType(request.getMethod());
//                //请求参数
//                Map<String,String[]> logParams = request.getParameterMap();
//                tbLog.setMapToParams(logParams);
//                IPInfoUtil.getInfo(request, ObjectUtil.mapToStringAll(logParams));
//                //请求用户
//                tbLog.setUser(username);
//                //请求IP
//                tbLog.setIp(IPInfoUtil.getIpAddr(request));
//                //IP地址
//                tbLog.setIpInfo(IPInfoUtil.getIpCity(IPInfoUtil.getIpAddr(request)));
//                //请求开始时间
//                Date logStartTime = beginTimeThreadLocal.get();
//
//                long beginTime = beginTimeThreadLocal.get().getTime();
//                long endTime = System.currentTimeMillis();
//                //请求耗时
//                Long logElapsedTime = endTime - beginTime;
//                tbLog.setTime(Math.toIntExact(logElapsedTime));
//                tbLog.setCreateDate(logStartTime);
//
//                //调用线程保存至数据库
//                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(tbLog,systemService));
//            }
//        } catch (Exception e) {
//            log.error("AOP后置通知异常", e);
//        }
//    }
//
//    /*
//     * 异常通知 用于拦截service层记录异常日志
//     * @param joinPoint
//     * @param e
//     */
//
//    @AfterThrowing(pointcut="serviceAspect()", throwing="e")
//    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
//
//        try {
//            String username= SecurityUtils.getSubject().getPrincipal().toString();
//
//            if (null != username) {
//
//                TbLog tbLog=new TbLog();
//
//                //日志标题
//                tbLog.setName(getServiceMethodDescription(joinPoint));
//                //日志类型
//                tbLog.setType(0);
//                //日志请求url
//                tbLog.setUrl(request.getRequestURI());
//                //请求方式
//                tbLog.setRequestType(request.getMethod());
//                //请求参数
//                Map<String,String[]> logParams=request.getParameterMap();
//                tbLog.setMapToParams(logParams);
//                IPInfoUtil.getInfo(request, ObjectUtil.mapToStringAll(logParams));
//                //请求用户
//                tbLog.setUser(username);
//                //请求IP
//                tbLog.setIp(IPInfoUtil.getIpAddr(request));
//                //IP地址
//                tbLog.setIpInfo(IPInfoUtil.getIpCity(IPInfoUtil.getIpAddr(request)));
//                //请求开始时间
//                Date logStartTime = beginTimeThreadLocal.get();
//
//                long beginTime = beginTimeThreadLocal.get().getTime();
//                long endTime = System.currentTimeMillis();
//                //请求耗时
//                Long logElapsedTime = endTime - beginTime;
//                tbLog.setTime(Math.toIntExact(logElapsedTime));
//                tbLog.setCreateDate(logStartTime);
//
//                //调用线程保存至数据库
//                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(tbLog,systemService));
//            }
//        } catch (Exception e1) {
//            log.error("AOP异常通知异常", e1);
//        }
//
//    }
//
//    /*
//     * 保存日志
//     */
//
//    private static class SaveSystemLogThread implements Runnable {
//        private TbLog tbLog;
//        private SystemService systemService;
//
//        public SaveSystemLogThread(TbLog tbLog, SystemService systemService) {
//            this.tbLog = tbLog;
//            this.systemService = systemService;
//        }
//
//        @Override
//        public void run() {
//
//            systemService.addLog(tbLog);
//        }
//    }
//
//    /*
//     * 获取注解中对方法的描述信息 用于Controller层注解
//     * @param joinPoint 切点
//     * @return 方法描述
//     * @throws Exception
//     */
//
//    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception{
//        //获取目标类名
//        String targetName = joinPoint.getTarget().getClass().getName();
//        //获取方法名
//        String methodName = joinPoint.getSignature().getName();
//        //获取相关参数
//        Object[] arguments = joinPoint.getArgs();
//        //生成类对象
//        Class targetClass = Class.forName(targetName);
//        //获取该类中的方法
//        Method[] methods = targetClass.getMethods();
//
//        String description = "";
//
//        for(Method method : methods) {
//            if(!method.getName().equals(methodName)) {
//                continue;
//            }
//            Class[] clazzs = method.getParameterTypes();
//            if(clazzs.length != arguments.length) {
//                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
//                continue;
//            }
//            description = method.getAnnotation(SystemControllerLog.class).description();
//        }
//        return description;
//    }
//
//    /*
//     * 获取注解中对方法的描述信息 用于Service层注解
//     * @param joinPoint 切点
//     * @return 方法描述
//     * @throws Exception
//    */
//
//    public static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception{
//        //获取目标类名
//        String targetName = joinPoint.getTarget().getClass().getName();
//        //获取方法名
//        String methodName = joinPoint.getSignature().getName();
//        //获取相关参数
//        Object[] arguments = joinPoint.getArgs();
//        //生成类对象
//        Class targetClass = Class.forName(targetName);
//        //获取该类中的方法
//        Method[] methods = targetClass.getMethods();
//
//        String description = "";
//
//        for(Method method : methods) {
//            if(!method.getName().equals(methodName)) {
//                continue;
//            }
//            Class[] clazzs = method.getParameterTypes();
//            if(clazzs.length != arguments.length) {
//                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
//                continue;
//            }
//            description = method.getAnnotation(SystemServiceLog.class).description();
//        }
//        return description;
//    }
//}
