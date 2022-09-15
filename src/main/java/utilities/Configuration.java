package utilities;

import exceptions.InvalidPropertyException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Configuration {
    private static Properties properties;

    private static void loadProperties() {

        properties = new Properties();
        try (InputStream inputStream = Configuration.class
                .getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties readProperties() {
        if (Objects.isNull(properties)) {
            loadProperties();
        }
        return properties;
    }
    public static String get(String key) {
        String value = readProperties().getProperty(key);

        if (Objects.isNull(value)) {
           throw new InvalidPropertyException(key);
        }
        return value;
    }

}