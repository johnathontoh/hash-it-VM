package sg.com.paloit.hashit.dao.repository;

import org.springframework.data.repository.CrudRepository;
import sg.com.paloit.hashit.dao.entity.WalletEntity;

public interface WalletJPARepository  extends CrudRepository<WalletEntity, String> {

    WalletEntity findByFileName(String fileName);
}
