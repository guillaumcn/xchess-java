package com.xchess.process;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessWrapper {
    private ProcessBuilder builder;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;

    public ProcessWrapper(ProcessBuilder builder) {
        this.builder = builder;
    }

    public void start() throws IOException {
        this.process = builder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));

        this.writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.stdoutReader =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
    }

    public void stop() throws IOException {
        this.writer.close();
        this.stdoutReader.close();
        this.process.destroyForcibly();
    }

    public List<String> getLineUntil(Pattern responsePattern, int timeoutInMs) throws InterruptedException {
        List<String> results = new ArrayList<>();
        Thread t = new Thread(() -> {
            long time = System.currentTimeMillis();
            try {
                while (true) {
                    if (System.currentTimeMillis() > time + timeoutInMs) {
                        throw new RuntimeException("No line matching " + responsePattern.toString() + " received in " + timeoutInMs + " ms");
                    }
                    String line = stdoutReader.readLine();
                    if (!Objects.isNull(line) && !line.isEmpty()) {
                        results.add(line);
                        Matcher matcher = responsePattern.matcher(line);
                        if (matcher.matches()) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join();

        return results;
    }

    public void writeCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }
}
