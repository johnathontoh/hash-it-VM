package sg.com.paloit.hashit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import sg.com.paloit.hashit.dao.entity.NetworkEntity;
import sg.com.paloit.hashit.dao.repository.NetworkJPARepository;
import sg.com.paloit.hashit.model.NetworkStatus;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.util.UUID;

@Service
public class Web3JClient {

    private static final Logger LOG = LoggerFactory.getLogger(Web3JClient.class);

    @Autowired
    private NetworkJPARepository networkJPARepository;

    public Web3j getClient(String networkId) {
        NetworkEntity networkEntity = networkJPARepository.findOne(UUID.fromString(networkId));
        if(networkEntity != null && networkEntity.getStatus() == NetworkStatus.CREATED) {
            LOG.info("RPC url {} ", networkEntity.getRpcEndpoint());
            return Web3j.build(new HttpService(networkEntity.getRpcEndpoint()));
        } else {
            LOG.error("RPC url not found for networkId {} ", networkId);
            throw new FormatException(ValidationMessages.NETWORK_NOT_FOUND);
        }
    }

}
