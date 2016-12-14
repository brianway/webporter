package com.brianway.webporter.data;

import java.util.List;

/**
 * Created by brian on 16/12/14.
 */
public interface OutPipeline<T> {
    void process(T outItem);

    default void process(List<T> outItems) {
        for (T outItem : outItems) {
            process(outItem);
        }
    }
}
