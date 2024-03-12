package io;

import java.io.*;
import java.util.*;

public class ScannerFileReader extends AbstractFileReader{

    private final Scanner scanner;

    public ScannerFileReader(File file, int batchSize) {
        super(file, batchSize);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public String getLine() throws IOException {
        return scanner.nextLine();
    }

    @Override
    public String[] getLinesBatch() {
        return this.linesBatch;
    }
}
