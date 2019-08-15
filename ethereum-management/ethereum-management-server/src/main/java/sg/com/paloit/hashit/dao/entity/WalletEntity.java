package sg.com.paloit.hashit.dao.entity;

import lombok.*;
import sg.com.paloit.hashit.dao.repository.converters.ContentConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Wallet")
public class WalletEntity {

    @Id
    private String fileName;

    @Convert(converter = ContentConverter.class)
    private String content;
}
