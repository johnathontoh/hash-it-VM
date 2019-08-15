package sg.com.paloit.hashit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static sg.com.paloit.hashit.validation.ValidationMessages.MandatoryTypes.*;

public class CreateNetworkRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = NETWORK_NAME_IS_MANDATORY)
    private String networkName;
    @NotNull(message = ADDRESS_IS_MANDATORY)
    private String address;
    @NotNull(message = NUMBER_OF_NODES_IS_MANDATORY)
    private Integer nodes;

    @JsonProperty("extra_vars")
    private ExtraVars extraVars = new ExtraVars();

    public CreateNetworkRequest() {
    }

    public CreateNetworkRequest(String networkName, String address, Integer nodes) {
        this.networkName = networkName;
        this.address = address;
        this.nodes = nodes;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
        this.extraVars.setResourceGroupName(networkName);
        this.extraVars.setDeploymentName(networkName+"-deploy");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
        this.extraVars.setNumVLNodesRegion(nodes);
    }

    public void setExtraVars(ExtraVars extraVars) {
        this.extraVars = extraVars;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getNodes() {
        return nodes;
    }

    public ExtraVars getExtraVars() {
        return extraVars;
    }

    public class ExtraVars implements Serializable {

        private String emailAddress;
        private Integer numVLNodesRegion;
        private String resourceGroupName;
        private String deploymentName;
        private String ethereumAdminPublicKey;
        @JsonProperty("isJoiningExistingNetwork") private Boolean isJoiningExistingNetwork = false;
        private String subscriptionID;
        private String resourceGroupLocation;

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public void setNumVLNodesRegion(Integer numVLNodesRegion) {
            this.numVLNodesRegion = numVLNodesRegion;
        }

        public void setResourceGroupName(String resourceGroupName) {
            this.resourceGroupName = resourceGroupName;
        }

        public void setDeploymentName(String deploymentName) {
            this.deploymentName = deploymentName;
        }

        public void setEthereumAdminPublicKey(String ethereumAdminPublicKey) {
            this.ethereumAdminPublicKey = ethereumAdminPublicKey;
        }

        public void setJoiningExistingNetwork(Boolean joiningExistingNetwork) {
            isJoiningExistingNetwork = joiningExistingNetwork;
        }

        public void setSubscriptionID(String subscriptionID) {
            this.subscriptionID = subscriptionID;
        }

        public void setResourceGroupLocation(String resourceGroupLocation) {
            this.resourceGroupLocation = resourceGroupLocation;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public Integer getNumVLNodesRegion() {
            return numVLNodesRegion;
        }

        public String getResourceGroupName() {
            return resourceGroupName;
        }

        public String getDeploymentName() {
            return deploymentName;
        }

        public String getEthereumAdminPublicKey() {
            return ethereumAdminPublicKey;
        }

        public Boolean getJoiningExistingNetwork() {
            return isJoiningExistingNetwork;
        }

        public String getSubscriptionID() {
            return subscriptionID;
        }

        public String getResourceGroupLocation() {
            return resourceGroupLocation;
        }
    }

}