package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by brian on 16/12/8.
 */
public class BaseAssembler<IN, OUT> {
    private static final Logger logger = LoggerFactory.getLogger(BaseAssembler.class);

    protected ExecutorService threadPool;

    protected int threadNum = 1;

    protected RawInput<IN> rawInput;

    protected List<DataFlow<OUT>> outQueues = new ArrayList<>();

    protected DataProcessor<IN, OUT> dataProcessor;

    /**
     * 工厂方法
     *
     * @param <IN> 输入队列的类型参数
     * @param <OUT> 输出队列的类型参数
     * @return 组装类的实例
     */
    public static <IN, OUT> BaseAssembler<IN, OUT> create() {
        return new BaseAssembler<>();
    }

    protected void initComponent() {
        if (rawInput == null) {
            throw new RuntimeException("must set input");
        }

        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = Executors.newFixedThreadPool(threadNum);
        }
    }

    public void run() {
        initComponent();
        while (!Thread.currentThread().isInterrupted()) {
            final IN inItem = rawInput.poll();
            if (inItem == null) {
                break;
            }
            final DataFlow<OUT> outQueue = outQueues.get(0);
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataProcessor.process(inItem, outQueue);
                    } catch (Exception e) {
                        logger.error("error: " + inItem, e);
                    }
                }
            });
        }
    }

    public BaseAssembler<IN, OUT> setRawInput(RawInput<IN> rawInput) {
        this.rawInput = rawInput;
        return this;
    }

    public BaseAssembler<IN, OUT> thread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public BaseAssembler<IN, OUT> addOutQueue(DataFlow<OUT> outQueue) {
        this.outQueues.add(outQueue);
        return this;
    }

    public BaseAssembler<IN, OUT> setDataProcessor(DataProcessor<IN, OUT> dataProcessor) {
        this.dataProcessor = dataProcessor;
        return this;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String folder = "/Users/brian/todo/data/webmagic/www.zhihu.com";
        DataFlow<String> outQueue = new DataFlow<>();
        BaseAssembler.<File, String>create()
                .setRawInput(new FileRawInput(folder))
                .addOutQueue(outQueue)
                .setDataProcessor(new DemoDataProcessor())
                .thread(10)
                .run();

        long count = 0;
        try {
            String outItem = null;
            outItem = outQueue.take();
            while (outItem != null) {
                if (count % 100000 == 0) {
                    System.out.println(count);
                }
                if (count >= 5600000) {
                    break;
                }
                count++;
                //System.out.println(count + "  " + outItem);
                outItem = outQueue.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}

