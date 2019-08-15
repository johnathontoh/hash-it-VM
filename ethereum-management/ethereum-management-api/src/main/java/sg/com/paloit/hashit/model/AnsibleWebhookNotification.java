package sg.com.paloit.hashit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AnsibleWebhookNotification implements Serializable {
    private Integer id;
    private String name;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public final static String STATUS_SUCCESS = "successful";

}
