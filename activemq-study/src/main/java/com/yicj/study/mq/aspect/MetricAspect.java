package com.yicj.study.mq.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {

    @Around("@annotation(metricTime)")
    public Object metric(ProceedingJoinPoint joinPointm, MetricTime metricTime)throws Throwable{
        String name = metricTime.value();
        long start = System.currentTimeMillis();
        try {
            return joinPointm.proceed() ;
        } finally {
            long end = System.currentTimeMillis();
            // 写入日志或发送至JMX:
            System.out.println("===> [Metrics] " + name + " : " + (end -start) +"s");
        }
    }
}
