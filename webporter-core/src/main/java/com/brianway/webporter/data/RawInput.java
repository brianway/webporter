package com.brianway.webporter.data;

/**
 * Created by brian on 16/11/30.
 */
public class RawInput<T> extends DataFlow<T> {

    public int getLeftCount() {
        return queue.size();
    }

    public T poll() {
        return queue.poll();
    }

}
