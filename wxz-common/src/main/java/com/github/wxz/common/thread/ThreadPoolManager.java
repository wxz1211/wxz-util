package com.github.wxz.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * @author xianzhi.wang
 * @date 2018/4/20 -10:36
 */
public class ThreadPoolManager {
    public static HashMap<String, ExecutorService> threadPoolMap = new HashMap<String, ExecutorService>();
    public static Logger LOG = LoggerFactory.getLogger(ThreadPoolManager.class);
    public static final int N_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 获取线程池
     *
     * @param name     线程池名称
     * @param nThreads 线程数
     * @param capacity 任务上限数
     * @return
     */
    public static ExecutorService getThreadPool(String name, int nThreads, int capacity) {
        ExecutorService executorService = null;
        synchronized (threadPoolMap) {
            executorService = threadPoolMap.get(name);
            if (executorService == null) {
                executorService = new ThreadPoolExecutor(nThreads, nThreads,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(capacity));
                threadPoolMap.put(name, executorService);
                LOG.info("Start ThreadPool " + name + " size " + nThreads);
            }
        }
        return executorService;
    }

    /**
     * 获取线程池
     * @param name 线程池名称
     * @param nThreads 线程数
     * @return
     */
    public static ExecutorService getThreadPool(String name, int nThreads) {
        ExecutorService executorService = null;
        synchronized (threadPoolMap) {
            executorService = threadPoolMap.get(name);
            if (executorService == null) {
                executorService = Executors.newFixedThreadPool(nThreads);
                threadPoolMap.put(name, executorService);
                LOG.info("Start ThreadPool " + name + " size " + nThreads);
            }
        }
        return executorService;
    }

    /**
     * 关闭线程池
     * @param name 线程池名称
     * @return
     */
    public static boolean shutdownThreadPool(String name){
        boolean result =false;
        try {
            ExecutorService executorService=null;
            synchronized (threadPoolMap){
                executorService=threadPoolMap.get(name);
                if(executorService!=null){
                    executorService.shutdown();
                    executorService=null;
                    LOG.info("Stop ThreadPool "+name );
                }
                threadPoolMap.remove(name);
            }
            result =true;
        } catch (Exception e) {
            LOG.error("ThreadPoolManager", e);
        }
        return result;
    }
}
