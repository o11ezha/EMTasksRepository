package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileManager {
    private final Object fileLock = new Object();

    static {
        Path path = Path.of("result.txt");
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException("Error creating result.txt", e);
        }
    }

    public String readFile(String filename) throws IOException {
        return Files.readString(Path.of(filename));
    }

    public void writeTempFile(int mapTaskID, List<CustomSet> customSets, int reduceCount) {
        Map<Integer, List<CustomSet>> buckets = new HashMap<>();

        for (CustomSet set : customSets) {
            int indexBucket = Math.abs(set.getKey().hashCode() % reduceCount);
            buckets.computeIfAbsent(indexBucket, k -> new ArrayList<>()).add(set);
        }

        for (Map.Entry<Integer, List<CustomSet>> entry : buckets.entrySet()) {
            int reduceTaskID = entry.getKey();
            String path = String.format("mr-%d-%d", mapTaskID, reduceTaskID);
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
                for (CustomSet set : entry.getValue()) {
                    bw.write(set.getKey() + "\t" + set.getValue());
                    bw.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<String, List<String>> readTempFile(List<String> filenames) {
        Map<String, List<String>> customSetMap = new HashMap<>();
        for (String filename : filenames) {
            try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tempRes = line.split("\t");
                    if (tempRes.length < 2) continue;
                    String key = tempRes[0];
                    String value = tempRes[1];
                    customSetMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return customSetMap;
    }

    public void deleteTempFiles() {
        Pattern pattern = Pattern.compile("^mr-\\d+-\\d+$");
        try (var stream = Files.newDirectoryStream(Path.of("."), path -> pattern.matcher(path.getFileName().toString()).matches())) {
            stream.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFinalFile(String key, String value) {
        synchronized (fileLock) {
            try (BufferedWriter bw = Files.newBufferedWriter(Path.of("result.txt"), StandardOpenOption.APPEND)) {
                bw.write(key + "\t" + value);
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
