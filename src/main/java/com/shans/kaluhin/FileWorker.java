package com.shans.kaluhin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWorker {
    private String projectPath;
    private final KeyWords keyWords = new KeyWords();

    public void startCount() throws IOException {
        List<String> files = readDirectoryFiles();
        for (String file: files) {
            readWords(Util.getInput(file));
        }
    }

    private List<String> readDirectoryFiles() throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(projectPath + "\\src"))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".java")).collect(Collectors.toList());
            return result;
        } catch (IOException e) {
            throw new IOException();
        }
    }

    private void readWords(String input) {
        Pattern pattern;
        pattern = Pattern.compile("(\\w+)");

        Matcher m = pattern.matcher(input);

        while (m.find()) {
            char[] a = m.group().trim().toCharArray();
            if (a.length > 0) {
                String word = m.group();
                if (keyWords.isKeyWord(word)) {
                    keyWords.incrementWord(word);
                }
            }
        }
    }

    public void setProjectPath(String path){
        projectPath = path;
    }

    public KeyWords getKeyWords() {
        return keyWords;
    }
}
