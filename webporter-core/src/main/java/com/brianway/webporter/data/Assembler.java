package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by brian on 16/12/2.
 */
public class Assembler {
    private static final Logger logger = LoggerFactory.getLogger(Assembler.class);

    protected ExecutorService threadPool;

    protected int threadNum = 1;

    protected RawInput<File> rawInput;

    protected String inPath;

    protected List<DataFlow<String>> outQueues = new ArrayList<>();

    protected DataProcessor<File, String> dataProcessor;

    public static Assembler create() {
        return new Assembler();
    }

    protected void initComponent() {
        if (inPath == null) {
            throw new RuntimeException("null inPath");
        }
        rawInput = new FileRawInput(inPath);
        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = Executors.newFixedThreadPool(threadNum);
        }
    }

    public void run() {
        initComponent();
        while (!Thread.currentThread().isInterrupted()) {
            final File inItem = rawInput.poll();
            if (inItem == null) {
                break;
            }
            final DataFlow<String> outQueue = outQueues.get(0);
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

    public Assembler thread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public Assembler setInPath(String inPath) {
        this.inPath = inPath;
        return this;
    }

    public Assembler addOutQueue(DataFlow<String> outQueue) {
        this.outQueues.add(outQueue);
        return this;
    }

    public Assembler setDataProcessor(DataProcessor<File, String> dataProcessor) {
        this.dataProcessor = dataProcessor;
        return this;
    }

    public static void main(String[] args) {
        //String folder = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";

        long startTime = System.currentTimeMillis();
        String folder = "/Users/brian/todo/data/webmagic/www.zhihu.com";
        DataFlow<String> outQueue = new DataFlow<>();
        Assembler.create().setInPath(folder)
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
                //System.out.println(++count + "  " + outItem);
                outItem = outQueue.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
