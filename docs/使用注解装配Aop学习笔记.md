1. 编写注解类
    ```java
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MetricTime {
       String value();
    }
    ```
2. 编写Aspect类
    ```java
    @Aspect
    @Component
    // 不需要获取注解类（注意@annotation参数为注解类全名称）
    public class MetricAspect {
        @Around("@annotation(com.yicj.study.mq.aspect.MetricTime)")
        public Object metric(ProceedingJoinPoint joinPointm)throws Throwable{
            //String name = metricTime.value();
            long start = System.currentTimeMillis();
            try {
                return joinPointm.proceed() ;
            } finally {
                long end = System.currentTimeMillis();
                // 写入日志或发送至JMX:
                System.out.println("===> [Metrics] : " + (end -start) +"s");
            }
        } 
    }
    @Aspect
    @Component
    // 获取注解实例（注意@annotation参数方法中的MetricTime2的参数名称）
    public class MetricAspect2 {
       @Around("@annotation(metricTime)")
       public Object metric2(ProceedingJoinPoint joinPointm, MetricTime metricTime)throws Throwable{
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
    ```
3. 编写业务类并使用@MetricTime注解
    ```java
    @Service
    public class UserService {
        @MetricTime("register")
        public String register(String name, String password, String email){
            try {
                Thread.sleep(345);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "success" ;
        }   
    }
    ```
4. 参考：https://www.liaoxuefeng.com/wiki/1252599548343744/1310052317134882