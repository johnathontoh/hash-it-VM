package sg.com.paloit.hashit.dao.repository.converters;

import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ContentConverter implements AttributeConverter<String, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(String s) {
        return s.getBytes();
    }

    @Override
    public String convertToEntityAttribute(byte[] bytes) {
        if (bytes.length > 0) {
            return new String(bytes);
        } return StringUtils.EMPTY;
    }

}
