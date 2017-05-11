package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.thread.CountableThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by brian on 16/12/8.
 * 数据处理框架的核心组装类
 * 用于拼接输入,处理逻辑和输出,进行线程设置
 */
public class BaseAssembler<IN, OUT> {
    private static final Logger logger = LoggerFactory.getLogger(BaseAssembler.class);

    protected int threadNum = 1;

    protected RawInput<IN> rawInput;

    protected DataProcessor<IN, OUT> dataProcessor;

    protected List<OutPipeline<OUT>> outPipelines = new ArrayList<>();

//    protected ExecutorService executorService;

    protected CountableThreadPool threadPool;

    protected AtomicLong outItemCount = new AtomicLong(0);

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    private final AtomicLong inItemCount = new AtomicLong(0);

    /**
     * 工厂方法
     *
     * @param rawInput 原始输入
     * @param dataProcessor 数据处理的类
     * @param <IN> 输入队列的类型参数
     * @param <OUT> 输出数据的类型参数
     * @return 组装类的实例
     */
    public static <IN, OUT> BaseAssembler<IN, OUT> create(
            RawInput<IN> rawInput,
            DataProcessor<IN, OUT> dataProcessor) {
        return new BaseAssembler<>(rawInput, dataProcessor);
    }

    public BaseAssembler(RawInput<IN> rawInput, DataProcessor<IN, OUT> dataProcessor) {
        this.rawInput = rawInput;
        this.dataProcessor = dataProcessor;
    }

    protected void initComponent() {
        if (rawInput == null) {
            throw new RuntimeException("must set input");
        }

        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = new CountableThreadPool(threadNum);
        }

        if (outPipelines.isEmpty()) {
            outPipelines.add(new ConsoleOutpipeline<>());
        }
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        checkRunningStat();
        initComponent();
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            final IN inItem = rawInput.poll();
            if (inItem == null) {
                if (threadPool.getThreadAlive() == 0) {
                    break;
                }
            } else {
                threadPool.execute(() -> {
                    try {
                        processInItem(inItem);
                    } catch (Exception e) {
                        logger.error("error: " + inItem, e);
                    } finally {
                        inItemCount.incrementAndGet();
                    }
                });
            }
        }

        stat.set(STAT_STOPPED);
        long endTime = System.currentTimeMillis();
        logger.info("Process end. spent {} ms", (endTime - startTime));

        // release some resources
        close();

        endTime = System.currentTimeMillis();
        logger.info("Total time: {} ms", endTime - startTime);
        logger.info("Total outItemCount: {}", outItemCount);
    }

    protected void processInItem(IN inItem) {
        List<OUT> outItems = dataProcessor.process(inItem);
        if (outItems == null || outItems.isEmpty()) {
            return;
        }

        outItemCount.addAndGet(outItems.size());
        outPipelines.forEach(outPipeline -> outPipeline.process(outItems));
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Assembler is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    protected void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Assembler is already running!");
        }
    }

    public void close() {
        destroyEach(dataProcessor);
        outPipelines.forEach(this::destroyEach);
        threadPool.shutdown();
    }

    private void destroyEach(Object object) {
        if (object instanceof AutoCloseable) {
            try {
                ((AutoCloseable) object).close();
            } catch (Exception e) {
                logger.warn("destroyEach: {}", e);
            }
        }
    }

    public BaseAssembler<IN, OUT> thread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public BaseAssembler<IN, OUT> setOutPipelines(List<OutPipeline<OUT>> outPipelines) {
        checkIfRunning();
        this.outPipelines = outPipelines;
        return this;
    }

    public BaseAssembler<IN, OUT> addOutPipeline(OutPipeline<OUT> outPipeline) {
        checkIfRunning();
        this.outPipelines.add(outPipeline);
        return this;
    }

    public static void main(String[] args) {

        String folder = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";

        OutPipeline<String> outPipeline = new ConsoleOutpipeline<>();

        new Thread(() -> {
            BaseAssembler.create(
                    new FileRawInput(folder), new DemoDataProcessor())
                    .addOutPipeline(outPipeline)
                    .thread(10)
                    .run();
        }).start();

    }
}

