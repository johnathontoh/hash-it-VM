package sg.com.paloit.hashit.service.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet implements Serializable {

        private String address;

        private String id;

        private String version;

        private Crypto crypto;

}
