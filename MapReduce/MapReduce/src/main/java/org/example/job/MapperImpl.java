package org.example.job;

import org.example.CustomSet;
import org.example.job.interfaces.Mapper;

import java.util.ArrayList;
import java.util.List;

public class MapperImpl implements Mapper {
    @Override
    public List<CustomSet> map(String filename, String content) {
        List<CustomSet> set = new ArrayList<>();
        String[] words = content.split("\\W+");
        for (String word : words) {
            set.add(new CustomSet(word, "1"));
        }
        return set;
    }
}
