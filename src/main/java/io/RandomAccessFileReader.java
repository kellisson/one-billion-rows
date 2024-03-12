package io;

import java.io.*;
import java.nio.channels.*;

public class RandomAccessFileReader extends AbstractFileReader{

    private RandomAccessFile randomAccessFile;
    private boolean next;

    public RandomAccessFileReader(File file, int batchSize) throws IOException {
        super(file, batchSize);
        next = true;
        randomAccessFile = new RandomAccessFile(file, "r");
        System.out.println(randomAccessFile.length());
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public String getLine() throws IOException {
        return randomAccessFile.readLine();
    }

    @Override
    public String[] getLinesBatch() {
        try {
            for (int i = 0; i < linesBatch.length; i++) {
                linesBatch[i] = randomAccessFile.readLine();
            }
        } catch (IOException e) {
            next = false;
            System.out.println("EOF!");
        }
        return this.linesBatch;
    }

}
