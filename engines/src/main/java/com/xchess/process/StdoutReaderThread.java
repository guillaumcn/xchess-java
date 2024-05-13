package com.xchess.process;

import com.xchess.exceptions.ProcessKilledException;
import com.xchess.exceptions.StdoutReaderThreadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

/**
 * A thread reading a java Inputstream until a predicate matches
 */
public class StdoutReaderThread extends Thread {
    private final List<String> lines;
    private final BufferedReader stdoutReader;
    private final Process process;

    /**
     * @param stdoutReader The buffered reader
     */
    public StdoutReaderThread(BufferedReader stdoutReader,
                              Process process) {
        this.lines = Collections.synchronizedList(new ArrayList<>());
        this.stdoutReader = stdoutReader;
        this.process = process;
    }

    /**
     * Starts the thread
     */
    @Override
    public void run() {
        super.run();
        String line;

        try {
            while ((line = stdoutReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    this.lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new StdoutReaderThreadException(e);
        }
    }

    public List<String> getLinesUntil(Predicate<String> matchPredicate,
                                      int timeoutInMs) throws TimeoutException, ProcessKilledException {
        long start = System.currentTimeMillis();
        List<String> newLines = new ArrayList<>();
        List<String> allLines = new ArrayList<>();
        Optional<String> matchingLine;
        long now;
        do {
            if (!this.process.isAlive()) {
                throw new ProcessKilledException();
            }
            now = System.currentTimeMillis();
            if (now - start > timeoutInMs) {
                this.lines.addAll(0, newLines);
                throw new TimeoutException("Timeout while waiting for " +
                        "process" +
                        " to respond");
            }
            newLines = getLines();
            matchingLine =
                    newLines.stream().filter(matchPredicate).findFirst();

            allLines.addAll(newLines);
        } while (matchingLine.isEmpty());

        int firstMatchingLineIndex = allLines.indexOf(matchingLine.get());
        List<String> resultLines = new ArrayList<>(allLines.subList(0,
                firstMatchingLineIndex + 1));
        List<String> remainingLines = resultLines.size() == allLines.size() ?
                new ArrayList<>() :
                new ArrayList<>(allLines.subList(firstMatchingLineIndex + 1,
                        allLines.size()));
        this.lines.addAll(0, remainingLines);
        return resultLines;
    }

    public List<String> getLines() {
        List<String> result = new ArrayList<>(this.lines);
        this.lines.clear();
        return result;
    }
}
