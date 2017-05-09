package com.brianway.webporter.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by brian on 16/11/30.
 */
public class DataFlow<T> {
    protected BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    protected void push(T item) {
        queue.add(item);
    }

    protected T poll() {
        return queue.poll();
    }

    protected T take() throws InterruptedException {
        return queue.take();
    }

}
