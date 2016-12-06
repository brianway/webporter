package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brian on 16/11/30.
 */
public class RawInput<T> extends DataFlow<T> {
    private static final Logger logger = LoggerFactory.getLogger(RawInput.class);


    public int getLeftCount() {
        return queue.size();
    }

    public T poll() {
        return queue.poll();
    }

}
