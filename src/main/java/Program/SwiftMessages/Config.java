package Program.SwiftMessages;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties properties = new Properties();
    private static boolean propertiesInitialized = false;

    public static String get(String propertyName) {
        if (!propertiesInitialized) {
            try (InputStream inputStream = new FileInputStream("SwiftDecoder.Properties")) {
                properties.load(inputStream);
                propertiesInitialized = true;
            } catch (Exception e) {
                System.out.println("Trying to load config " + propertyName + " failed");
                System.out.println(e.getMessage());
            }
        }

        return properties.getOrDefault(propertyName, "").toString();
    }

}
