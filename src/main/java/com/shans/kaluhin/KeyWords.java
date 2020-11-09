package com.shans.kaluhin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyWords {
    private Map<String, Integer> words;

    public KeyWords(){
      initialize();
    }

    public void incrementWord(String word){
        int count = words.get(word);
        words.put(word, count + 1);
    }

    public boolean isKeyWord(String word){
        return words.containsKey(word);
    }

    public Map<String, Integer> getWords() {
        return words;
    }

    public void setWords(Map<String, Integer> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        String mapAsString = words.keySet().stream()
                .map(key -> key + "=" + words.get(key))
                .collect(Collectors.joining("\n", "{", "}"));
        return mapAsString;
    }

    private void initialize(){
        words = new HashMap<>();
        words.put("abstract", 0);
        words.put("assert", 0);
        words.put("boolean", 0);
        words.put("break", 0);
        words.put("byte", 0);
        words.put("case", 0);
        words.put("catch", 0);
        words.put("char", 0);
        words.put("class", 0);
        words.put("const", 0);
        words.put("continue", 0);
        words.put("default", 0);
        words.put("do", 0);
        words.put("double", 0);
        words.put("else", 0);
        words.put("enum", 0);
        words.put("extends", 0);
        words.put("false", 0);
        words.put("final", 0);
        words.put("finally", 0);
        words.put("float", 0);
        words.put("for", 0);
        words.put("goto", 0);
        words.put("if", 0);
        words.put("implements", 0);
        words.put("import", 0);
        words.put("instaceof", 0);
        words.put("int", 0);
        words.put("interface", 0);
        words.put("long", 0);
        words.put("native", 0);
        words.put("new", 0);
        words.put("null", 0);
        words.put("package", 0);
        words.put("private", 0);
        words.put("protected", 0);
        words.put("public", 0);
        words.put("return", 0);
        words.put("short", 0);
        words.put("static", 0);
        words.put("strictfp", 0);
        words.put("super", 0);
        words.put("switch", 0);
        words.put("synchronized", 0);
        words.put("this", 0);
        words.put("throw", 0);
        words.put("throws", 0);
        words.put("transient", 0);
        words.put("true", 0);
        words.put("try", 0);
        words.put("void", 0);
        words.put("volatile", 0);
        words.put("while", 0);
    }
}
