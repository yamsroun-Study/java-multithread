package thread.metrics.practice;

public class MinMaxMetrics {

    private volatile long minValue;
    private volatile long maxValue;

    public MinMaxMetrics() {
        this.minValue = Long.MIN_VALUE;
        this.maxValue = Long.MAX_VALUE;
    }

    public void addSample(long newSample) {
        synchronized (this) {
            minValue = Math.min(minValue, newSample);
            maxValue = Math.max(maxValue, newSample);
        }
    }

    public long getMin() {
        return minValue;
    }

    public long getMax() {
        return maxValue;
    }
}