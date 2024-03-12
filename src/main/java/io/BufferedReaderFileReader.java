package io;

import java.io.*;
import java.io.FileReader;

public class BufferedReaderFileReader extends AbstractFileReader{

    BufferedReader bufferedReader;

    public BufferedReaderFileReader(File file, int batchSize) {
        super(file, batchSize);
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public String getLine() throws IOException {
        return bufferedReader.readLine();
    }

    @Override
    public String[] getLinesBatch() {
        return this.linesBatch;
    }
}
