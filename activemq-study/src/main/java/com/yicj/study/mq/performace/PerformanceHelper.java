package com.yicj.study.mq.performace;

import org.springframework.util.StopWatch;

public class PerformanceHelper {

    private StopWatch stopWatch ;

    public PerformanceHelper(StopWatch stopWatch){
       this.stopWatch = stopWatch ;
    }

    public Object execute(Callback callback, String tag) {
        stopWatch.start(tag);
        Object ret = null ;
        try {
            ret = callback.doExecute() ;
        }catch (Throwable e){
            throw new RuntimeException(e) ;
        }
        stopWatch.stop();
        return ret ;
    }

    public interface Callback{
        Object doExecute() throws Throwable ;
    }

}
