package backend.academy.logsAnalyzer.services.sketch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataHolder {
    private List<Double> data;

    private boolean changed;

    public DataHolder() {
        changed = false;

        data = new ArrayList<Double>();
    }

    public void add(double value) {
        changed = true;

        data.add(value);
    }

    public long getCount() {
        return data.size();
    }

    /**
     * returning a quantile of added doubles
     * @param quantile must be between 0 and 1
     * @return 0 - returns min, 1 - returns max
     */
    @SuppressWarnings("MagicNumber")
    public double getQuantileValue(double quantile) {
        if (changed) {
            changed = false;

            Collections.sort(data);
        }

        if (quantile < 0 || quantile > 1) {
            throw new IllegalArgumentException("quantile must be between 0 and 1");
        }

        if (data.isEmpty()) {
            throw new IllegalArgumentException("data is empty");
        }

        if (quantile == 0) {
            return data.getFirst();
        } else if (quantile == 1) {
            return data.getLast();
        }

        for (int i = 0; i < data.size(); i++) {
            double percentile = i * 1.0 / data.size();
            if (percentile > quantile) {
                return data.get(i) - data.get(i) * (percentile - quantile);
            }
        }
        return data.getLast();
    }

    public double getAverage() {
        return data.stream().mapToDouble(d -> d).average().getAsDouble();
    }
}
