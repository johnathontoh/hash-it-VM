package sg.com.paloit.hashit.service.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Crypto {

    private String cipher;

    private String ciphertext;

    private Cipherparams cipherparams;

    private String kdf;

    private Kdfparams kdfparams;

    private String mac;

}
