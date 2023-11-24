package com.xchess.process;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessWrapper {
    private final ProcessBuilder processBuilder;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;
    private String[] command;

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
                                       int timeoutInMs) throws InterruptedException {
        return readLinesUntil((line) -> {
            Matcher matcher = responsePattern.matcher(line);
            return matcher.matches();
        }, timeoutInMs);
    }

    public List<String> readLinesUntil(String expectedResponse,
                                       int timeoutInMs) throws InterruptedException {
        return readLinesUntil((line) -> line.equals(expectedResponse),
                timeoutInMs);
    }

    private List<String> readLinesUntil(Function<String, Boolean> matchFunction, int timeoutInMs) throws InterruptedException {
        if (timeoutInMs <= 0) {
            throw new IllegalArgumentException("Read timeout should be " +
                    "greater than 0");
        }

        List<String> results = new ArrayList<>();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String line = stdoutReader.readLine();
                    if (!Objects.isNull(line) && !line.isEmpty()) {
                        results.add(line);
                        if (matchFunction.apply(line)) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join(timeoutInMs);

        return results;
    }

    public void writeCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }
}
