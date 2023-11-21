package com.xchess.process;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ProcessWrapperTest {
    private ProcessWrapper subject;
    private Process process;
    private BufferedReader stdoutReader;
    private BufferedWriter writer;

    @Before
    public void setUp() {
        this.subject = new ProcessWrapper();

        this.process = Mockito.mock(Process.class);
        this.subject.setProcess(this.process);

        this.stdoutReader = Mockito.mock(BufferedReader.class);
        this.subject.setStdoutReader(this.stdoutReader);

        this.writer = Mockito.mock(BufferedWriter.class);
        this.subject.setWriter(this.writer);
    }

    @Test
    public void testShouldCloseStreamOnStop() throws IOException {
        this.subject.stop();

        Mockito.verify(this.stdoutReader, Mockito.times(1)).close();
        Mockito.verify(this.writer, Mockito.times(1)).close();
    }

    @Test
    public void testShouldDestroyProcessOnStop() throws IOException {
        this.subject.stop();

        Mockito.verify(this.process, Mockito.times(1)).destroy();
    }
}
