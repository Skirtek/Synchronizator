package pl.lab;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Runnable sync = () -> {
            try {
                Directories.Synchronize();
                Files.Synchronize();

                System.out.println("Zakończono skanowanie");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(sync, 0, 1, TimeUnit.SECONDS);
    }
}