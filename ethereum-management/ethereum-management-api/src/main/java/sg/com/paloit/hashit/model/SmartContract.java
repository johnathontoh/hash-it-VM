package sg.com.paloit.hashit.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

import static sg.com.paloit.hashit.validation.ValidationMessages.MandatoryTypes.SMART_CONTRACT_NAME_IS_MANDATORY;

public class SmartContract implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    @NotNull(message = SMART_CONTRACT_NAME_IS_MANDATORY)
    private String name;
    private String body;
    private String address;
    private Boolean deployed;
    private String networkId;

    public SmartContract() {
    }

    public SmartContract(UUID id, String name, String body, String address, Boolean deployed, String networkId) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.address = address;
        this.deployed = deployed;
        this.networkId = networkId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public static class SmartContractBuilder {
        private UUID id;
        private String name;
        private String body;
        private String address;
        private Boolean deployed;
        private String networkId;

        public SmartContractBuilder() {
        }

        public SmartContractBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public SmartContractBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SmartContractBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public SmartContractBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public SmartContractBuilder setDeployed(Boolean deployed) {
            this.deployed = deployed;
            return this;
        }

        public void setNetworkId(String networkId) {
            this.networkId = networkId;
        }

        public SmartContract build() {
            return new SmartContract(id, name, body, address, deployed, networkId);
        }
    }

    public static SmartContractBuilder newBuilder() {
        return new SmartContractBuilder();
    }

    @Override
    public String toString() {
        return "SmartContract{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", address='" + address + '\'' +
                ", deployed=" + deployed +
                ", networkId='" + networkId + '\'' +
                '}';
    }
}
