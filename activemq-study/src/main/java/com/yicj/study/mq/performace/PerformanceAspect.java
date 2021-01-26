package com.yicj.study.mq.performace;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {


    @Around("@annotation(com.yicj.study.mq.performace.PfLog)")
    public void performance(final ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch() ;
        PerformanceHelper helper = new PerformanceHelper(stopWatch) ;
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        try {
            helper.execute(() -> {
                joinPoint.proceed() ;
                return null;
            },"hello") ;
        }finally {
            log.info("====> method ["+methodName+"]  execute time : " + stopWatch.getTotalTimeSeconds() +" seconds");
        }
    }
}
