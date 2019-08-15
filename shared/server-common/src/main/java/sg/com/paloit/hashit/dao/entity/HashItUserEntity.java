package sg.com.paloit.hashit.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "USERS")
public class HashItUserEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "RAW(16)")
    private UUID id;

    @Column(name = "USER_ID")
    private String userId;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "WALLET")
    private String wallet;

    public HashItUserEntity() {
    }

    public HashItUserEntity(UUID id, String userId, String email, String wallet) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.wallet = wallet;
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getWallet() {
        return wallet;
    }

    public static class HashItUserEntityBuilder {
        private UUID id;
        private String userId;
        private String password;
        private String wallet;

        public HashItUserEntityBuilder() {
        }

        public HashItUserEntityBuilder(HashItUserEntity hashItUser) {
            ofNullable(hashItUser.getId()).ifPresent(this::setId);
            ofNullable(hashItUser.getUserId()).ifPresent(this::setUserId);
            ofNullable(hashItUser.getEmail()).ifPresent(this::setPassword);
            ofNullable(hashItUser.getWallet()).ifPresent(this::setWallet);
        }

        public HashItUserEntityBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public HashItUserEntityBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public HashItUserEntityBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public HashItUserEntityBuilder setWallet(String wallet) {
            this.wallet = wallet;
            return this;
        }

        public HashItUserEntity build() {
            return new HashItUserEntity(id, userId, password, wallet);
        }
    }

    public static HashItUserEntityBuilder newBuilder() {
        return new HashItUserEntityBuilder();
    }

    public static HashItUserEntityBuilder newBuilder(HashItUserEntity hashItUser) {
        return new HashItUserEntityBuilder(hashItUser);
    }
}
