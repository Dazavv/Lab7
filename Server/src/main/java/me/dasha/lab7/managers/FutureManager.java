package me.dasha.lab7.managers;


import me.dasha.lab7.MainServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Класс хранящий коллекцию fixedThreadPool для их проверки на готовность и выполнения
 */
public class FutureManager {
    private static final Collection<Future<ConnectionManagerPool>> FixedThreadPoolFutures = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(FutureManager.class.getName());

    public static void addNewFixedThreadPoolFuture(Future<ConnectionManagerPool> future){
        FixedThreadPoolFutures.add(future);
    }

    public static void checkAllFutures(){
        if(!FixedThreadPoolFutures.isEmpty()) {
            FixedThreadPoolFutures.forEach(s -> logger.info(s.toString()));
        }
        FixedThreadPoolFutures.stream()
                .filter(Future::isDone)
                .forEach(f -> {
                    try {
                        ConnectionManager.submitNewResponse(f.get());

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
        FixedThreadPoolFutures.removeIf(Future::isDone);
        //logger.info("поток удален");
    }
}
