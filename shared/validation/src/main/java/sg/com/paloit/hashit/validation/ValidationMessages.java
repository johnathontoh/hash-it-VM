package sg.com.paloit.hashit.validation;

public enum ValidationMessages implements ErrorResponseEnum {
    USER_NOT_AUTHORIZED("USER_NOT_AUTHORIZED", "User not Authorized"),
    USER_ID_IS_MANDATORY(MandatoryTypes.USER_ID_IS_MANDATORY, "User Id is mandatory"),
    WALLET_CREATION_FAILED("WALLET_CREATION_FAILED", "Failed to create wallet"),
    ADDRESS_IS_MANDATORY(MandatoryTypes.ADDRESS_IS_MANDATORY, "Address is mandatory"),
    NETWORK_NAME_IS_MANDATORY(MandatoryTypes.NETWORK_NAME_IS_MANDATORY, "Network name is mandatory"),
    SMART_CONTRACT_NAME_IS_MANDATORY(MandatoryTypes.SMART_CONTRACT_NAME_IS_MANDATORY, "Smart Contract name is mandatory"),
    NUMBER_OF_NODES_IS_MANDATORY(MandatoryTypes.NUMBER_OF_NODES_IS_MANDATORY, "Number of nodes is mandatory"),
    NUMBER_OF_NODES_CANNOT_BE_ZERO("NUMBER_OF_NODES_CANNOT_BE_ZERO", "Number of nodes cannot be zero"),
    FAILED_TO_RETRIEVE_SMART_CONTRACT("FAILED_TO_RETRIEVE_SMART_CONTRACT", "Failed to retrieve Smart Contract"),
    FAILED_TO_WRITE_SMART_CONTRACT("FAILED_TO_WRITE_SMART_CONTRACT", "Unable to write Smart Contract to a file"),
    FAILED_TO_COMPILE_SMART_CONTRACT("FAILED_TO_COMPILE_SMART_CONTRACT", "Unable to compile Smart Contract"),
    FAILED_TO_RETRIEVE_TEMPLATE("FAILED_TO_RETRIEVE_TEMPLATE", "Failed to retrieve Template"),
    FAILED_TO_READ_WALLET_DATA("FAILED_TO_READ_WALLET_DATA", "Failed to read wallet data"),
    FAILED_TO_DEPLOY_CONTRACT("FAILED_TO_DEPLOY_CONTRACT", "Failed to deploy contract"),
    FAILED_TO_EXECUTE_METHOD_IN_SMART_CONTRACT("FAILED_TO_EXECUTE_METHOD_IN_SMART_CONTRACT", "Failed to execute method in smart contract"),
    FAILED_TO_LOAD_CLASS("FAILED_TO_LOAD_CLASS", "Failed to load class"),
    FAILED_TO_GET_TRANSACTION_RECEIPT("FAILED_TO_GET_TRANSACTION_RECEIPT", "Failed to get Transaction Receipt"),
    FAILED_TO_GET_NOUNCE("FAILED_TO_GET_NOUNCE", "Failed to get Nounce"),
    FAILED_TO_READ_ABIDEFINITION("FAILED_TO_READ_ABIDEFINITION", "Failed to read ABI"),
    FAILED_TO_READ_FILE("FAILED_TO_READ_FILE", "Failed to read file"),
    FAILED_TO_PARSE_JSON_RESPONSE("FAILED_TO_PARSE_JSON_RESPONSE", "Failed to parse json response"),
    SMART_CONTRACT_NOT_FOUND("SMART_CONTRACT_NOT_FOUND", "Smart contract not found"),
    NETWORK_NOT_FOUND("NETWORK_NOT_FOUND", "Network not found for the given network id");

    private final String systemMessage;

    ValidationMessages(final String type, final String systemMessage) {
        if(!this.toString().equals(type)){
            throw new RuntimeException("String and Type Do Not Match");
        }
        this.systemMessage = systemMessage;
    }

    @Override
    public String getSystemMessage() {
        return systemMessage;
    }

    public static class MandatoryTypes {
        public static final String NUMBER_OF_NODES_IS_MANDATORY = "NUMBER_OF_NODES_IS_MANDATORY";
        public static final String NETWORK_NAME_IS_MANDATORY = "NETWORK_NAME_IS_MANDATORY";
        public static final String SMART_CONTRACT_NAME_IS_MANDATORY = "SMART_CONTRACT_NAME_IS_MANDATORY";
        public static final String ADDRESS_IS_MANDATORY = "ADDRESS_IS_MANDATORY";
        public static final String USER_ID_IS_MANDATORY = "USER_ID_IS_MANDATORY";
        public static final String EMAIL_IS_MANDATORY = "EMAIL_IS_MANDATORY";
    }
}

