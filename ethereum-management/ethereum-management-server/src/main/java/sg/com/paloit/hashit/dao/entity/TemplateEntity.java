package sg.com.paloit.hashit.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "TEMPLATES")
public class TemplateEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "RAW(16)")
    private UUID id;
    @Column(name = "NAME")
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "BODY")
    private byte[] body;

    @Column(name = "JSON_SCHEMA", length = 3000)
    private String jsonSchema;

    public TemplateEntity() {
    }

    public TemplateEntity(UUID id, String name, byte[] body, String jsonSchema) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.jsonSchema = jsonSchema;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getBody() {
        return body;
    }

    public String getJsonSchema() {
        return jsonSchema;
    }

    public static class TemplateEntityBuilder {
        private UUID id;
        private String name;
        private byte[] body;
        private String jsonSchema;

        public TemplateEntityBuilder() {
        }

        public TemplateEntityBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public TemplateEntityBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TemplateEntityBuilder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public TemplateEntityBuilder setJsonSchema(String jsonSchema) {
            this.jsonSchema = jsonSchema;
            return this;
        }

        public TemplateEntity build() {
            return new TemplateEntity(id, name, body, jsonSchema);
        }
    }

    public static TemplateEntityBuilder newBuilder() {
        return new TemplateEntityBuilder();
    }

}
