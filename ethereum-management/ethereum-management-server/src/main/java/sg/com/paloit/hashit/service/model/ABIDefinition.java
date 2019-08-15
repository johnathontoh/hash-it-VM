package sg.com.paloit.hashit.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ABIDefinition {

    private Boolean constant;

    private List<FunctionInputOutput> inputs;

    private String name;

    private List<FunctionInputOutput> outputs;

    private Boolean payable;

    private String stateMutability;

    private String type;
}
