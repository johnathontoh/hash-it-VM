package sg.com.paloit.hashit.dao.repository;

import org.springframework.data.repository.CrudRepository;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.dao.entity.SmartContractNames;

import java.util.List;
import java.util.UUID;

public interface SmartContractJPARepository extends CrudRepository<SmartContractEntity, UUID> {
    List<SmartContractNames> findAllSmartContractNamesByWallet(String wallet);
    List<SmartContractEntity> findAllByNetworkId(String networkId);
}
