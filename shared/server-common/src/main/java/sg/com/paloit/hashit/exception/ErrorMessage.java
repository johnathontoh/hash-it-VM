package sg.com.paloit.hashit.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {
    private String time;
    private String error;
    private String description;
    private String path;
    private String method;

    public ErrorMessage(String error, String description, String path, String method) {
        this.time = ZonedDateTime.now(ZoneOffset.UTC).toString();
        this.error = error;
        this.description = description;
        this.path = path;
        this.method = method;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "time='" + time + '\'' +
                ", error='" + error + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
