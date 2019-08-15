package sg.com.paloit.hashit.dao.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SMART_CONTRACT")
public class SmartContractEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "RAW(16)")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "WALLET")
    private String wallet;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "BODY")
    private byte[] body;

    @Column(name = "DEPLOYED")
    private Boolean deployed = false;

    @Column(name = "NETWORK_ID")
    private String networkId;

}
