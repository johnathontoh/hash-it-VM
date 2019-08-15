package sg.com.paloit.hashit.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.com.paloit.hashit.dao.entity.HashItUserEntity;

import java.util.UUID;

public interface HashItUserJPARepository extends JpaRepository<HashItUserEntity, UUID> {

    HashItUserEntity findOneByUserId(String userId);
    HashItUserEntity findOneByEmail(String email);
    HashItUserEntity findOneByWallet(String walletFileName);
}
