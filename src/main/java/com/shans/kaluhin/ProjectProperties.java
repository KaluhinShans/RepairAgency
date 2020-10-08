package com.shans.kaluhin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ProjectProperties {
    private static String fileName = "app.properties";
    private static Properties props = setProperties();


    public static String getProperty(String property){
        return props.getProperty(property);
    }

    public static Properties gerEmailProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", getProperty("mail.host"));
        props.put("mail.smtp.port", getProperty("mail.port"));
        props.put("mail.transport.protocol", getProperty("mail.protocol"));

        return props;
    }

    private static Properties setProperties(){
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(fileName))) {
            props.load(in);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return props;
    }
}
