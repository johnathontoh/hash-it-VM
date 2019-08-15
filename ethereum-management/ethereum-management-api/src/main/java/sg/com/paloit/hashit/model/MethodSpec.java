package sg.com.paloit.hashit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MethodSpec {

    private List<FunctionInput>  input;
    private String name;

}
