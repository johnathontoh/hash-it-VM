package sg.com.paloit.hashit.dao.repository;

import org.springframework.data.repository.CrudRepository;
import sg.com.paloit.hashit.dao.entity.NetworkEntity;
import sg.com.paloit.hashit.model.NetworkStatus;

import java.util.List;
import java.util.UUID;

public interface NetworkJPARepository extends CrudRepository<NetworkEntity, UUID> {
    List<NetworkEntity> findAllByStatus(NetworkStatus status);
    List<NetworkEntity> findAllByWallet(String wallet);

    NetworkEntity findOneByJobId(String jobId);
    NetworkEntity findOneByDeleteJobId(String deleteJobId);
}
