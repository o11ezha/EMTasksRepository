package org.example.job.interfaces;

import org.example.CustomSet;

import java.util.List;

public interface Mapper {
    List<CustomSet> map(String filename, String content);
}
