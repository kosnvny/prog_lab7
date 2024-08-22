package managers;

import commandLine.Console;
import commandLine.Printable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class FutureManager {
    private static final Printable console = new Console();
    private static final Collection<Future<ConnectionManagerPool>> fixedThreadPoolFutures = new ArrayList<>();

    public static void addNewFixedThreadPoolFuture(Future<ConnectionManagerPool> future){
        fixedThreadPoolFutures.add(future);
    }

    public static void checkAllFutures(){
        fixedThreadPoolFutures.stream()
                .filter(Future::isDone)
                .forEach(f -> {
                    try {
                        ConnectionManager.submitNewResponse(f.get());
                    } catch (InterruptedException | ExecutionException e) {
                        console.print("<-------------------------------------------------->");
                    }
                });
        fixedThreadPoolFutures.removeIf(Future::isDone);
    }
}
