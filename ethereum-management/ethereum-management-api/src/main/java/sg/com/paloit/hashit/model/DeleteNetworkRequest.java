package sg.com.paloit.hashit.model;

import java.io.Serializable;

public class DeleteNetworkRequest implements Serializable {
    private String subscriptionID;
    private String resourceGroupName;

    public DeleteNetworkRequest(String subscriptionID, String name) {
        this.subscriptionID = subscriptionID;
        this.resourceGroupName = name;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getResourceGroupName() {
        return resourceGroupName;
    }

    public void setResourceGroupName(String name) {
        this.resourceGroupName = name;
    }
}
