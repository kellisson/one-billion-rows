import io.*;
import processor.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TemperatureCalculator {

    private final AbstractFileReader fileReader;

    ThreadFactory virtualThreadFactory;

    public static int MAX_BATCH = 2000;

    private Queue<String[]> fifo;

    TemperatureProcessor temperatureProcessor = new TemperatureProcessor();

    private final Map<String, Integer> temperatureMapSum;

    private final Map<String, Integer> temperatureMapCount;

    public TemperatureCalculator(AbstractFileReader fileReader) {
        this.fileReader = fileReader;
        temperatureMapSum = new HashMap<>();
        temperatureMapCount = new HashMap<>();
        virtualThreadFactory = Thread.ofVirtual().factory();
    }

    public void calculateV2() throws IOException {
        fifo = new ArrayDeque<>();
        String[] lineBatch = new String[MAX_BATCH];
        int i = 0;
        while(fileReader.hasNext()) {
            String line = fileReader.getLine();
            if(line == null)
                break;
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
        checkProcess(fifo);
        temperatureProcessor.printMap();
    }

    public void calculateV3() throws IOException {
        fifo = new ArrayDeque<>();
        while(fileReader.hasNext()) {
            String[] lineBatch = fileReader.getLinesBatch();
            fifo.add(lineBatch);
            if (!fifo.isEmpty()) {
                processTemperature(fifo.poll(), temperatureProcessor);
            }
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
        //POOL OF RUNNABLES?
        temperatureProcessor.setLineBatch(lineBatch);
        Thread.startVirtualThread(temperatureProcessor);
    }

    private void calculateAverage() {
        Set<String> keySet = temperatureMapSum.keySet();
        System.out.println("BEGIN PRINT MAP _---------------------------");
        for(String key : keySet){
            Integer stringTemperature = temperatureMapSum.get(key);
            double doubleTemperature = stringTemperature / 0.1f;
            System.out.println(key+" "+ Math.ceil(doubleTemperature/temperatureMapCount.get(key)));
        }
    }
}
