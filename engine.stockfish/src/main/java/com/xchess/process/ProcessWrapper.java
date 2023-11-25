package com.xchess.process;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessWrapper {
    private final ProcessBuilder processBuilder;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;
    private final String[] command;

    public ProcessWrapper(String... command) {
        this.processBuilder = new ProcessBuilder(command);
        this.command = command;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void setStdoutReader(BufferedReader stdoutReader) {
        this.stdoutReader = stdoutReader;
    }

    public String[] getCommand() {
        return command;
    }

    public void start() throws IOException {
        this.process = this.processBuilder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));

        this.writer =
                new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.stdoutReader =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
    }

    public void stop() throws IOException {
        this.writer.close();
        this.stdoutReader.close();
        this.process.destroy();
    }

    public List<String> readLinesUntil(Pattern responsePattern,
                                       int timeoutInMs) {
        return readLinesUntil(line -> {
            Matcher matcher = responsePattern.matcher(line);
            return matcher.matches();
        }, timeoutInMs);
    }

    public List<String> readLinesUntil(String expectedResponse,
                                       int timeoutInMs) {
        return readLinesUntil(line -> line.equals(expectedResponse),
                timeoutInMs);
    }

    private List<String> readLinesUntil(Predicate<String> matchPredicate,
                                        int timeoutInMs) {
        if (timeoutInMs <= 0) {
            throw new IllegalArgumentException("Read timeout should be " +
                    "greater than 0");
        }

        List<String> results = new ArrayList<>();
        AtomicBoolean keepRunning = new AtomicBoolean(true);
        Thread t = new Thread(() -> {
            try {
                while (keepRunning.get()) {
                    String line = stdoutReader.readLine();
                    if (!Objects.isNull(line) && !line.isEmpty()) {
                        results.add(line);
                        if (matchPredicate.test(line)) {
                            keepRunning.set(false);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        try {
            t.join(timeoutInMs);
            // Timeout do not stop thread, ensure it will be stopped by
            // setting keepRunning to false
            keepRunning.set(false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return results;
    }

    public void writeCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }
}
