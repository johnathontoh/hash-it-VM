package sg.com.paloit.hashit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static sg.com.paloit.hashit.validation.ValidationMessages.MandatoryTypes.EMAIL_IS_MANDATORY;
import static sg.com.paloit.hashit.validation.ValidationMessages.MandatoryTypes.USER_ID_IS_MANDATORY;

public class HashItUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    @NotNull(message = USER_ID_IS_MANDATORY)
    private String userId;
    @NotNull(message = EMAIL_IS_MANDATORY)
    private String email;
    @JsonIgnore
    private String wallet;

    public HashItUser() {
    }

    public HashItUser(UUID id, String userId, String email, String wallet) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.wallet = wallet;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWallet() {
        return wallet;
    }

    public static class HashItUserBuilder {
        private UUID id;
        private String userId;
        private String email;
        private String wallet;

        public HashItUserBuilder() {
        }

        public HashItUserBuilder(HashItUser hashItUser) {
            ofNullable(hashItUser.getId()).ifPresent(this::setId);
            ofNullable(hashItUser.getUserId()).ifPresent(this::setUserId);
            ofNullable(hashItUser.getEmail()).ifPresent(this::setEmail);
            ofNullable(hashItUser.getWallet()).ifPresent(this::setWallet);
        }

        public HashItUserBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public HashItUserBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public HashItUserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public HashItUserBuilder setWallet(String wallet) {
            this.wallet = wallet;
            return this;
        }

        public HashItUser build() {
            return new HashItUser(id, userId, email, wallet);
        }
    }

    public static HashItUserBuilder newBuilder() {
        return new HashItUserBuilder();
    }

    public static HashItUserBuilder newBuilder(HashItUser hashItUser) {
        return new HashItUserBuilder(hashItUser);
    }

    @Override
    public String toString() {
        return "HashItUser{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
