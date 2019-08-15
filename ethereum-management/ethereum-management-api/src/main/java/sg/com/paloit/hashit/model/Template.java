package sg.com.paloit.hashit.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String body;
    private List<TemplateParam> params;

    public Template() {
    }

    public Template(UUID id, String name, String body, List<TemplateParam> params) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.params = params;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<TemplateParam> getParams() {
        return params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static class TemplateBuilder {
        private UUID id;
        private String name;
        private String body;
        private List<TemplateParam> params;

        public TemplateBuilder() {
        }

        public void setParams(String params) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.params = mapper.readValue(params, new TypeReference<List<TemplateParam>>(){});
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public TemplateBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public TemplateBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TemplateBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public Template build() {
            return new Template(id, name, body, params);
        }
    }

    public static TemplateBuilder newBuilder() {
        return new TemplateBuilder();
    }

    public static class TemplateParam implements Serializable {
        private String name;
        private String type;
        private String description;

        public TemplateParam() {
        }

        public TemplateParam(String name, String type, String description) {
            this.name = name;
            this.type = type;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
