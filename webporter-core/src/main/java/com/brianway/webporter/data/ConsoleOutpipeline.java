package com.brianway.webporter.data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by brian on 16/12/14.
 */
public class ConsoleOutpipeline<T> implements OutPipeline<T> {

    private AtomicLong count = new AtomicLong(0);

    @Override
    public void process(T outItem) {
        count.incrementAndGet();
        System.out.println(outItem);
    }

    public AtomicLong getCount() {
        return count;
    }
}
