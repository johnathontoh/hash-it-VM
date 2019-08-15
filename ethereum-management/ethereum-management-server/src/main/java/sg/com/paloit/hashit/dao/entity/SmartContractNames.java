package sg.com.paloit.hashit.dao.entity;

import java.util.UUID;

public interface SmartContractNames {
    UUID getId();
    String getName();
    String getAddress();
    String getWallet();
    String getNetworkId();
    Boolean getDeployed();
}
