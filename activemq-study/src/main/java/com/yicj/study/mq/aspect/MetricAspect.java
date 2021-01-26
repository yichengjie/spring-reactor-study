package com.yicj.study.mq.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {
    // 不需要获取注解类（注意@annotation参数为注解类全名称）
    @Around("@annotation(com.yicj.study.mq.aspect.MetricTime)")
    public Object metric(ProceedingJoinPoint joinPoint)throws Throwable{
        //String name = metricTime.value();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed() ;
        } finally {
            long end = System.currentTimeMillis();
            // 写入日志或发送至JMX:
            System.out.println("===> [Metrics] : " + (end -start) +"s");
        }
    }

    // 获取注解实例（注意@annotation参数方法中的MetricTime2的参数名称）
    @Around("@annotation(metricTime2)")
    public Object metric2(ProceedingJoinPoint joinPoint, MetricTime2 metricTime2)throws Throwable{
        String name = metricTime2.value();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed() ;
        } finally {
            long end = System.currentTimeMillis();
            // 写入日志或发送至JMX:
            System.out.println("===> [Metrics] " + name + " : " + (end -start) +"s");
        }
    }
}
