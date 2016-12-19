package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by brian on 16/11/30.
 */
public interface DataProcessor<K, V> {
    Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    default void process(K inItem, DataFlow<V> out) {
        List<V> outItems = process(inItem);
        if (outItems == null) {
            logger.error("error: " + inItem);
            return;
        }
        for (V outItem : outItems) {
            out.push(outItem);
        }
    }

    List<V> process(K inItem);
}
