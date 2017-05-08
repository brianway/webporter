package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by brian on 16/11/30.
 * 数据处理接口,根据具体业务来进行拓展和实现
 */
public interface DataProcessor<K, V> {
    Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    //TODO 是否保留?过度设计?
    default void process(K inItem, DataFlow<V> out) {
        List<V> outItems = process(inItem);
        if (outItems == null) {
            logger.error("error: " + inItem);
            return;
        }
        outItems.forEach(out::push);
    }

    List<V> process(K inItem);
}
