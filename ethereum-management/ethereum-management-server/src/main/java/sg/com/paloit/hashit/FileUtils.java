package sg.com.paloit.hashit;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;


public class FileUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static Function<String, String> getFileContent = filename -> {
        try {
            FileInputStream fis = new FileInputStream(filename);
            return IOUtils.toString(fis, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("Error {}", e);
            throw new FormatException(ValidationMessages.FAILED_TO_READ_FILE);
        }
    };

    public static Function<String, InputStream> getFileInputStream = filename -> {
        try {
            FileInputStream fis = new FileInputStream(filename);
            return fis;
        } catch (IOException e) {
            LOGGER.error("Error {}", e);
            throw new FormatException(ValidationMessages.FAILED_TO_READ_FILE);
        }
    };
}
