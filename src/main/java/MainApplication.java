import io.*;

import java.io.*;

public class MainApplication {

    private static String fileLocation;

    public static void main(String[] args) throws IOException {
        if(args != null && args[0] != null)
            fileLocation = args[0];

        int batchSize = TemperatureCalculator.MAX_BATCH;
        long starTime = System.currentTimeMillis();
        File file = new File(fileLocation);
        //RandomAccessFileReader fileReader = new RandomAccessFileReader(file, batchSize);
        AbstractFileReader fileReader = new BufferedReaderFileReader(file, batchSize);
        //AbstractFileReader fileReader = new ScannerFileReader(file, batchSize);
        TemperatureCalculator temperatureCalculator = new TemperatureCalculator(fileReader);
        try {
            temperatureCalculator.calculateV2();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("elapsed time: "+((endTime-starTime)*0.001));
    }

}
