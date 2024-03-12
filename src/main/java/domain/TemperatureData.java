package domain;

public class TemperatureData {

    private int count;
    private int maximum;
    private int minimum;
    private int total;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void addTotal(int register){
        this.total += register;
    }

    public void addCount() {
        this.count++;
    }

    @Override
    public String toString() {
        return "TemperatureData{" +
                "count=" + count +
                ", maximum=" + maximum +
                ", minimum=" + minimum +
                ", total=" + total +
                '}';
    }
}
