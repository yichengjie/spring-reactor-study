package com.yicj.study.reactor.common;


import org.springframework.util.StopWatch;

public class PerformanceHelper {

    private StopWatch stopWatch ;

    public PerformanceHelper(StopWatch stopWatch){
        this.stopWatch = stopWatch ;
    }

    public void performance(String tag, Callback callback){
        this.stopWatch.start(tag);
        try {
            callback.doExecute() ;
        }catch (Throwable e){
            throw new RuntimeException(e) ;
        }
        this.stopWatch.stop();
    }

    public interface Callback{
        Object doExecute()throws Throwable ;
    }
}
