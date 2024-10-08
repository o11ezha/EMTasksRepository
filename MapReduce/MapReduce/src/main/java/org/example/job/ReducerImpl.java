package org.example.job;

import org.example.job.interfaces.Reducer;

import java.util.List;

public class ReducerImpl implements Reducer {
    @Override
    public String reduce(String key, List<String> values) {
        return String.valueOf(values.size());
    }
}
