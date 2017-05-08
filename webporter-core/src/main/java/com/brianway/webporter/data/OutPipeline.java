package com.brianway.webporter.data;

import java.util.List;

/**
 * Created by brian on 16/12/14.
 */
public interface OutPipeline<T> {
    void process(T outItem);

    default void process(List<T> outItems) {
        outItems.forEach(this::process);
    }
}
