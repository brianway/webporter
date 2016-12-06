package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by brian on 16/11/30.
 */
public abstract class DataProcessor<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    public DataProcessor() {
    }

    public void process(K inItem, DataFlow<V> out) {
        List<V> outItems = process(inItem);
        if (outItems == null) {
            logger.error("error: " + inItem);
            return;
        }
        for (V outItem : outItems) {
            out.push(outItem);
        }
    }

    abstract protected List<V> process(K inItem);
}
