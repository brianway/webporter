package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.thread.CountableThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by brian on 16/12/8.
 */
public class BaseAssembler<IN, OUT> {
    private static final Logger logger = LoggerFactory.getLogger(BaseAssembler.class);

    protected int threadNum = 1;

    protected RawInput<IN> rawInput;

    protected DataProcessor<IN, OUT> dataProcessor;

    protected List<OutPipeline<OUT>> outPipelines = new ArrayList<>();

    protected ExecutorService executorService;

    protected CountableThreadPool threadPool;

    protected AtomicLong outItemCount = new AtomicLong(0);

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
            //threadPool = Executors.newFixedThreadPool(threadNum);
            threadPool = new CountableThreadPool(threadNum);
        }

        if (outPipelines.isEmpty()) {
            outPipelines.add(new ConsoleOutpipeline<>());
        }
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        initComponent();
        while (!Thread.currentThread().isInterrupted()) {
            final IN inItem = rawInput.poll();
            if (inItem == null) {
                if (threadPool.getThreadAlive() == 0) {
                    break;
                }
            } else {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            processInItem(inItem);
                        } catch (Exception e) {
                            logger.error("error: " + inItem, e);
                        } finally {

                        }
                    }
                });
            }
        }
        logger.info("Process end");
        threadPool.shutdown();
        long endTime = System.currentTimeMillis();
        //logger.info("Total time: {}", endTime - startTime);
        System.out.println("Total time: " + (endTime - startTime));
        System.out.println("Total outItemCount: " + outItemCount);
    }

    protected void processInItem(IN inItem) {
        List<OUT> outItems = dataProcessor.process(inItem);
        if (outItems == null) {
            return;
        }
        outItemCount.addAndGet(outItems.size());
        for (OutPipeline<OUT> outPipeline : outPipelines) {
            outPipeline.process(outItems);
        }
    }
//    protected DataFlow<OUT> route(IN inItem) {
//        int h;
//        int hash = (inItem == null) ? 0 : (h = inItem.hashCode()) ^ (h >>> 16);
//        int index = (outQueues.size() - 1) & hash;
//        logger.debug("index: {}", index);
//        return outQueues.get(index);
//    }

    public BaseAssembler<IN, OUT> setRawInput(RawInput<IN> rawInput) {
        this.rawInput = rawInput;
        return this;
    }

    public BaseAssembler<IN, OUT> thread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public BaseAssembler<IN, OUT> setDataProcessor(DataProcessor<IN, OUT> dataProcessor) {
        this.dataProcessor = dataProcessor;
        return this;
    }

    public BaseAssembler<IN, OUT> addOutPipeline(OutPipeline<OUT> outPipeline) {
        this.outPipelines.add(outPipeline);
        return this;
    }

    public static void main(String[] args) {

        //String folder = "/Users/brian/todo/data/webmagic/www.zhihu.com";
        String folder = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";

        OutPipeline<String> outPipeline = new ConsoleOutpipeline<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                BaseAssembler.<File, String>create()
                        .setRawInput(new FileRawInput(folder))
                        .addOutPipeline(outPipeline)
                        .setDataProcessor(new DemoDataProcessor())
                        .thread(10)
                        .run();
            }
        }).start();

    }
}

