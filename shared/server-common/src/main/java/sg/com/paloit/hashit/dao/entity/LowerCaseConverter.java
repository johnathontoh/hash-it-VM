package sg.com.paloit.hashit.dao.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LowerCaseConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String s) {
        return getString(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return getString(s);
    }

    private String getString(String s) {
        return StringUtils.isBlank(s) ? s : s.toLowerCase();
    }
}
