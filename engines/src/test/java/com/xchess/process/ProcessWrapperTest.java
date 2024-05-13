package com.xchess.process;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class ProcessWrapperTest {
    private ProcessWrapper subject;
    private Process process;
    private BufferedReader stdoutReader;
    private StdoutReaderThread stdoutReaderThread;
    private BufferedWriter writer;

    @Before
    public void setUp() {
        this.subject = new ProcessWrapper();

        this.process = mock(Process.class);
        this.subject.setProcess(this.process);

        this.stdoutReader = mock(BufferedReader.class);
        this.subject.setStdoutReader(stdoutReader);
        this.stdoutReaderThread =
                new StdoutReaderThread(stdoutReader, process);
        this.subject.setStdoutReaderThread(stdoutReaderThread);

        this.writer = mock(BufferedWriter.class);
        this.subject.setWriter(this.writer);
    }

    @Test
    public void testShouldCloseStreamOnStop() throws IOException {
        this.subject.stop();

        verify(this.stdoutReader, times(1)).close();
        verify(this.writer, times(1)).close();
    }

    @Test
    public void testShouldDestroyProcessOnStop() throws IOException {
        this.subject.stop();

        verify(this.process, times(1)).destroy();
    }

    @Test
    public void shouldReturnInputMessagesListWhenReadUntilPattern() throws IOException, TimeoutException {
        String breakMessage = "STOP";
        List<String> expected = prepareLinesRead(breakMessage);
        stdoutReaderThread.start();
        when(this.process.isAlive()).thenReturn(true);
        assertEquals(expected, this.subject.readLinesUntil(Pattern.compile(
                "^" + breakMessage + "$"), 5000));
    }

    @Test
    public void shouldReturnInputMessagesListWhenReadUntilString() throws IOException, TimeoutException {
        String breakMessage = "STOP";
        List<String> expected = prepareLinesRead(breakMessage);
        stdoutReaderThread.start();
        when(this.process.isAlive()).thenReturn(true);
        assertEquals(expected, this.subject.readLinesUntil(breakMessage, 5000));
    }

    @Test
    public void shouldThrowExceptionWhenReadReachesTimeout() {
        String breakMessage = "STOP";
        when(this.process.isAlive()).thenReturn(true);
        assertThrows(TimeoutException.class,
                () -> this.subject.readLinesUntil(breakMessage, 1));
    }

    @Test
    public void shouldThrowExceptionIfReadTimeoutIsLowerEqualsThan0() {
        String breakMessage = "STOP";
        assertThrows(IllegalArgumentException.class,
                () -> this.subject.readLinesUntil(breakMessage, 0));
    }

    @Test
    public void shouldThrowExceptionIfReadlineThrowsException() throws IOException {
        String breakMessage = "STOP";
        when(this.stdoutReader.readLine()).thenThrow(IOException.class);
        assertThrows(IOException.class,
                () -> this.subject.readLinesUntil(breakMessage, 5000));
    }

    @Test
    public void shouldFlushAfterWritingCommand() throws IOException {
        this.subject.writeCommand("test");

        verify(this.writer, times(1)).write("test");
        verify(this.writer, times(1)).flush();
    }

    private List<String> prepareLinesRead(String breakMessage) throws IOException {
        AtomicInteger readCount = new AtomicInteger();
        List<String> expected = new ArrayList<>();
        when(this.stdoutReader.readLine()).thenAnswer(invocation -> {
            String response;
            if (readCount.get() < 4) {
                response = "READ " + readCount.getAndIncrement();
                expected.add(response);
            } else if (readCount.get() == 4) {
                response = breakMessage;
                readCount.getAndIncrement();
                expected.add(response);
            } else {
                response = "";
            }
            return response;
        });
        return expected;
    }
}
