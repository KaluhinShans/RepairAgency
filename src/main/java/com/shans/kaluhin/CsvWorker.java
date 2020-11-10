package com.shans.kaluhin;

import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class CsvWorker {
    private Path myPath = Paths.get("src/main/resources/keyWords.csv");
    private Sort sort = Sort.AZ;
    private CSVWriter csvWriter;

    public void writeWords(KeyWords keyWords) {
        Map<String, Integer> words = keyWords.getWords();
        try (
                Writer writer = Files.newBufferedWriter(myPath);
        ) {
            csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            switch (sort) {
                case AZ:
                    words.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(this::writeKeyAndValue);
                    break;
                case ZA:
                    words.entrySet().stream()
                            .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                            .forEach(this::writeKeyAndValue);
                    break;
                case ASC:
                    words.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .forEach(this::writeKeyAndValue);
                    break;
                case DSC:
                    words.entrySet().stream()
                            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                            .forEach(this::writeKeyAndValue);
                    break;
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void writeKeyAndValue(Map.Entry<String, Integer> entry){
        csvWriter.writeNext(new String[]{entry.getKey(), String.valueOf(entry.getValue())});
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }
}
