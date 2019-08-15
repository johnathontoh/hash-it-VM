package sg.com.paloit.hashit.service.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Kdfparams {

    private String dklen;

    private String n;

    private String p;

    private String r;

    private String salt;


}
