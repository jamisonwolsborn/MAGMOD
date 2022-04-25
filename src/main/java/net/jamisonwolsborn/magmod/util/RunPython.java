package net.jamisonwolsborn.magmod.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RunPython {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Runnable runPythonFile () throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","data_analysis.py");
        pb.start();
        return null;
    }

    public void updateDatabase() throws IOException {
        Runnable runPythonFile = runPythonFile();
        ScheduledFuture<?> python = scheduler.scheduleAtFixedRate(runPythonFile, 5, 5, TimeUnit.MINUTES);

    }
}
