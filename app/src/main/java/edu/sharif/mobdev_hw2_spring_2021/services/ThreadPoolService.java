package edu.sharif.mobdev_hw2_spring_2021.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadPoolService {

    private static final ThreadPoolService THREAD_POOL_SERVICE_INSTANCE = new ThreadPoolService();
    private final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(5);


    public static ThreadPoolService getInstance() {
        return THREAD_POOL_SERVICE_INSTANCE;
    }

    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
