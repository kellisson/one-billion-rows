package processor;

import domain.*;

import java.util.*;

public class TemperatureProcessor implements Runnable{

    private Map<String, TemperatureData> temperatureDataMap;
    private SortedSet<String> orderedKeys;
    private String[] lineBatch;

    public TemperatureProcessor(){
        temperatureDataMap = new HashMap<>();
        orderedKeys = new TreeSet<>();
    }

    @Override
    public void run() {
        List<TemperatureRegister> temperatureRegisterList = extractTemperatureRegister(this.lineBatch);
        for(TemperatureRegister temperatureRegister : temperatureRegisterList) {
            TemperatureData temperatureData = temperatureDataMap.get(temperatureRegister.getLocation());
            changeTemperatureData(temperatureRegister, temperatureData);
        }
    }

    //TODO: checar performance
    private synchronized void changeTemperatureData(TemperatureRegister temperatureRegister, TemperatureData temperatureData) {
        orderedKeys.add(temperatureRegister.getLocation());
        temperatureDataMap.put(temperatureRegister.getLocation(), updateTemperatureData(temperatureData, temperatureRegister));
    }

    public void printMap() {
        for(String key : orderedKeys){
            TemperatureData temperatureData = temperatureDataMap.get(key);
            double doubleTemperature = temperatureData.getTotal() * 0.1f;
            double median = doubleTemperature / (double) temperatureData.getCount();
            //TODO: ceil median one decimal
            System.out.println(key+"="+((temperatureData.getMinimum()*0.1f+"/"+median+"/"+temperatureData.getMaximum()*0.1f)));
        }
    }

    private List<TemperatureRegister> extractTemperatureRegister(String[] lineBatch) {
        List<TemperatureRegister> temperatureRegisterList = new ArrayList<>();
        for(String line: lineBatch){
            String[] splittedLine = line.split(";");
            String key = splittedLine[0];
            int value = getIntTemperature(splittedLine[1]);
            TemperatureRegister temperatureRegister = new TemperatureRegister();
            temperatureRegister.setLocation(key);
            temperatureRegister.setRegister(value);
            temperatureRegisterList.add(temperatureRegister);
        }
        return temperatureRegisterList;
    }


    private int getIntTemperature(String s) {
        return Integer.parseInt(s.replace(".",""));
    }

    private TemperatureData updateTemperatureData(TemperatureData temperatureData, TemperatureRegister temperatureRegister) {
        if(temperatureData == null){
            temperatureData = new TemperatureData();
            temperatureData.setCount(1);
            temperatureData.setMaximum(temperatureRegister.getRegister());
            temperatureData.setMinimum(temperatureRegister.getRegister());
            temperatureData.setTotal(temperatureRegister.getRegister());
        } else {
            if(temperatureRegister.getRegister() > temperatureData.getMaximum())
                temperatureData.setMaximum(temperatureRegister.getRegister());

            if(temperatureRegister.getRegister() < temperatureData.getMinimum())
                temperatureData.setMinimum(temperatureRegister.getRegister());

            temperatureData.addTotal(temperatureRegister.getRegister());
            temperatureData.addCount();
        }
        return temperatureData;
    }

    public void setLineBatch(String[] lineBatch) {
        this.lineBatch = lineBatch;
    }
}
