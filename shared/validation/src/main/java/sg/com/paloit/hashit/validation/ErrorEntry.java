package sg.com.paloit.hashit.validation;

public class ErrorEntry {

    private String code;
    private String systemMessage;

    public ErrorEntry() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    @Override
    public String toString() { return "[[" + code + ":" + systemMessage + "]]"; }
}
