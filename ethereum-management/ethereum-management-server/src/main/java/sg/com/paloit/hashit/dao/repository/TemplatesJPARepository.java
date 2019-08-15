package sg.com.paloit.hashit.dao.repository;

import org.springframework.data.repository.CrudRepository;
import sg.com.paloit.hashit.dao.entity.TemplateEntity;
import sg.com.paloit.hashit.dao.entity.TemplateNames;

import java.util.List;
import java.util.UUID;

public interface TemplatesJPARepository extends CrudRepository<TemplateEntity, UUID> {
    List<TemplateNames> findBy();
    TemplateEntity findByName(String name);
}
