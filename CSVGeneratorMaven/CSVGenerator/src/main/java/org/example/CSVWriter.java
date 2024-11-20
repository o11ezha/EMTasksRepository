package org.example;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CSVWriter {

    private final Map<String, Object> settings;

    public CSVWriter(Map<String, Object> settings) {
        this.settings = settings;
    }

    public <T> void writeCSV(List<T> objects, String filePath) {
        try (Writer writer = new BufferedWriter(new FileWriter(filePath))) {
            if (getSetting("withHeaders", true) && !objects.isEmpty()) {
                writeHeader(objects.get(0).getClass(), writer);
            }

            for (T object : objects) {
                writeObject(object, writer);
            }
        } catch (IOException e) {
            throw new CSVWriterException("Возникла ошибка во время записи в файл: ", e);
        } catch (IllegalAccessException e) {
            throw new CSVWriterException("Возникла ошибка во время получения доступа к полям: ", e);
        }
    }

    private void writeHeader(Class<?> clazz, Writer writer) throws IOException {
        Field[] clazzFields = clazz.getDeclaredFields();
        for (int i = 0; i < clazzFields.length; i++) {
            if (i > 0) {
                writer.write(getSetting("delimiter", ','));
            }
            if (clazzFields[i].isAnnotationPresent(CSVField.class)) {
                CSVField annotation = clazzFields[i].getAnnotation(CSVField.class);
                writer.append(annotation.value().isEmpty() ? clazzFields[i].getName() : annotation.value());
            }
        }
        writer.write(getSetting("lineSeparator", System.lineSeparator()));
    }

    private <T> void writeObject(T object, Writer writer) throws IllegalAccessException, IOException {
        Field[] objectFields = object.getClass().getDeclaredFields();

        for (int i = 0; i < objectFields.length; i++) {
            objectFields[i].setAccessible(true);
            Object value = objectFields[i].get(object);

            if (i > 0) {
                writer.write(getSetting("delimiter", ','));
            }

            writer.write(value == null ? getSetting("nullValue", "") : value.toString());
        }
        writer.write(getSetting("lineSeparator", System.lineSeparator()));
    }

    private <T> T getSetting(String key, T defaultValue) {
        Object value = settings.getOrDefault(key, defaultValue);
        if (value != null && value.getClass().isInstance(defaultValue))
            return (T) value;
        return defaultValue;
    }
}
