package io;

import java.io.*;

public abstract class AbstractFileReader {

    protected final File file;
    protected final String[] linesBatch;


    protected AbstractFileReader(File file, int batchSize) {
        this.linesBatch = new String[batchSize];
        this.file = file;
    }

    public abstract boolean hasNext();
    public abstract String getLine() throws IOException;

    public abstract String[] getLinesBatch();
}
