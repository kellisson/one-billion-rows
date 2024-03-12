package io;

import processor.*;

import java.io.*;
import java.util.*;

public class FileReader {

    //TODO: READ -> https://stackoverflow.com/questions/9093888/fastest-way-of-reading-relatively-huge-byte-files-in-java
    // http://www.java2s.com/Tutorials/Java/Java_io/1050__Java_nio_Asynchronous.htm

    private int MAX_BATCH = 2000;

    private Queue<String[]> fifo;

    TemperatureProcessor temperatureProcessor = new TemperatureProcessor();

    TemperatureProcessor[] temperatureProcessors = new TemperatureProcessor[10];

    public void fileScannerRead(File file) {
        fifo = new ArrayDeque<>();
        String[] lineBatch = new String[MAX_BATCH];
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                if (i == lineBatch.length) {
                    fifo.add(lineBatch);
                    i = 0;
                } else {
                    lineBatch[i++] = line;
                }
                if(!fifo.isEmpty()){
                    processTemperature(fifo.poll(), temperatureProcessor);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        checkProcess(fifo);
        temperatureProcessor.printMap();
    }

    private void checkProcess(Queue<String[]> fifo) {
        while(!fifo.isEmpty()) {
            processTemperature(fifo.poll(), temperatureProcessor);
        }
    }

    private static void processTemperature(String[] lineBatch, TemperatureProcessor temperatureProcessor) {
        temperatureProcessor.setLineBatch(lineBatch);
        Thread.startVirtualThread(temperatureProcessor);
    }
}
