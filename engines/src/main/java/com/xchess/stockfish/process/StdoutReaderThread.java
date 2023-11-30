package com.xchess.stockfish.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class StdoutReaderThread extends Thread {
    private boolean keepRunning;
    private final List<String> resultLines;
    private final BufferedReader stdoutReader;
    private final Predicate<String> endPredicate;
    private IOException exceptionThrown;
    private final int timeoutInMs;

    public StdoutReaderThread(BufferedReader stdoutReader,
                              Predicate<String> endPredicate, int timeoutInMs) {
        this.keepRunning = true;
        this.resultLines = new ArrayList<>();
        this.stdoutReader = stdoutReader;
        this.endPredicate = endPredicate;
        this.exceptionThrown = null;
        this.timeoutInMs = timeoutInMs;
    }

    public List<String> getLines() throws TimeoutException, IOException {
        this.start();

        try {
            this.join(timeoutInMs);
            if (!Objects.isNull(exceptionThrown)) {
                throw exceptionThrown;
            }
            // Timeout do not stop thread, ensure it will be stopped by
            // setting keepRunning to false
            if (this.keepRunning) {
                keepRunning = false;
                throw new TimeoutException("Timeout while waiting for " +
                        "process" +
                        " to respond");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return this.resultLines;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (keepRunning) {
                String line = stdoutReader.readLine();
                if (!Objects.isNull(line) && !line.isEmpty()) {
                    this.resultLines.add(line);
                    if (endPredicate.test(line)) {
                        keepRunning = false;
                    }
                }
            }
        } catch (IOException e) {
            exceptionThrown = e;
            throw new RuntimeException(e);
        } finally {
            keepRunning = false;
        }
    }
}
