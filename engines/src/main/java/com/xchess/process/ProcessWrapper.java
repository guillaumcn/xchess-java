package com.xchess.process;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class wrapping process and providing utils to read and write standard
 * input and output
 */
public class ProcessWrapper {
    private final ProcessBuilder processBuilder;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;

    /**
     * @param command The process commands
     */
    public ProcessWrapper(String... command) {
        this.processBuilder = new ProcessBuilder(command);
    }

    /**
     * Used for test purposes
     *
     * @param process The process to set
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     * Used for test purposes
     *
     * @param writer The process stdin writer
     */
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * Used for test purposes
     *
     * @param stdoutReader The process stdout reader
     */
    public void setStdoutReader(BufferedReader stdoutReader) {
        this.stdoutReader = stdoutReader;
    }

    /**
     * Start the process
     *
     * @throws IOException If any error occurs during communicating with process
     */
    public void start() throws IOException {
        this.process = this.processBuilder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));

        this.writer =
                new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.stdoutReader =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
    }

    /**
     * Stop the process
     *
     * @throws IOException If any error occurs during communicating with process
     */
    public void stop() throws IOException {
        this.writer.close();
        this.stdoutReader.close();
        this.process.destroy();
    }

    /**
     * Read process stdout until a Pattern is read
     *
     * @param responsePattern Awaited response pattern
     * @param timeoutInMs     Maximum timeout for reading
     * @return A list of read messages including the pattern matching message
     * @throws TimeoutException if timeout is reached
     * @throws IOException      If any error occurs during communicating with
     *                          process
     */
    public List<String> readLinesUntil(Pattern responsePattern,
                                       int timeoutInMs) throws TimeoutException, IOException {
        return readLinesUntil(line -> {
            Matcher matcher = responsePattern.matcher(line);
            return matcher.matches();
        }, timeoutInMs);
    }

    /**
     * Read process stdout until a String is read
     *
     * @param expectedResponse Awaited response
     * @param timeoutInMs      Maximum timeout for reading
     * @return A list of read messages including the string matching message
     * @throws TimeoutException if timeout is reached
     * @throws IOException      If any error occurs during communicating with
     *                          process
     */
    public List<String> readLinesUntil(String expectedResponse,
                                       int timeoutInMs) throws TimeoutException, IOException {
        return readLinesUntil(line -> line.equals(expectedResponse),
                timeoutInMs);
    }

    /**
     * Read process stdout until a predicate matches
     *
     * @param matchPredicate Awaited predicate
     * @param timeoutInMs    Maximum timeout for reading
     * @return A list of read messages including the matching message
     * @throws TimeoutException if timeout is reached
     * @throws IOException      If any error occurs during communicating with
     *                          process
     */
    private synchronized List<String> readLinesUntil(Predicate<String> matchPredicate,
                                                     int timeoutInMs) throws TimeoutException, IOException {
        if (timeoutInMs <= 0) {
            throw new IllegalArgumentException("Read timeout should be " +
                    "greater than 0");
        }

        StdoutReaderThread stdoutReaderThread =
                new StdoutReaderThread(this.stdoutReader,
                        matchPredicate, timeoutInMs);
        return stdoutReaderThread.getLines();
    }

    /**
     * Write a command to the process stdin
     *
     * @param command The command
     * @throws IOException If any error occurs during communicating with process
     */
    public void writeCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }
}
