package sg.com.paloit.hashit;

import io.swagger.annotations.*;
import sg.com.paloit.hashit.model.*;
import sg.com.paloit.hashit.validation.ErrorResponse;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

@Api(
        value = "ethereum-management-api",
        description = "Interface for Ethereum Management Server",
        tags = {"ethereum-management"}
)
public interface EthereumManagementApi {
    @ApiOperation(value = "Create a new Ethereum Account",
            notes = "Create a new Ethereum Account")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Ethereum Account Created"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    EthereumWallet createEthereumAccount(@ApiParam(value = "Application User To Register", required = true)
                      final HashItUser hashItUser);

    @ApiOperation(value = "Create a Network",
            notes = "Create a Network")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Ethereum Network Creation Triggered"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    EthereumNetwork createNetwork(@ApiParam(value = "Parameters to Pass", required = true)
                                         final CreateNetworkRequest createNetworkRequest,
                                        @ApiParam(value = "Authorization", required = false) final String token);

    @ApiOperation(value = "Get List of Networks",
            notes = "Get List of Networks")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Networks Retrieved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    List<EthereumNetwork> getNetworks(@ApiParam(value = "Authorization", required = false) final String token);

    @ApiOperation(value = "Get List of Contracts",
            notes = "Get List of Contracts")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Smart Contracts Retrieved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    List<SmartContract> getSmartContracts(@ApiParam(value = "Authorization", required = false) final String token);

    @ApiOperation(value = "Get Smart Contract",
            notes = "Get Smart Contract")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Smart Contract Retrieved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    SmartContract getSmartContract(@ApiParam(value = "Contract Id", required = true) final UUID id,
                                   @ApiParam(value = "Authorization", required = false) final String token);

    @ApiOperation(value = "Save Smart Contract",
            notes = "Save Smart Contract")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Smart Contract Saved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    SmartContract saveSmartContract(@ApiParam(value = "Smart Contract", required = true)
                                    final SmartContract smartContract,
                                    @ApiParam(value = "Authorization", required = false) final String token);

    @ApiOperation(value = "Get List of Templates",
            notes = "Get List of Templates")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Templates Retrieved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    List<Template> getTemplates();

    @ApiOperation(value = "Get Template",
            notes = "Get Template")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Template Retrieved"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    Template getTemplate(@ApiParam(value = "Template Id", required = true) final UUID id);

    @ApiOperation(value = "Deploy Smart Contract",
            notes = "Deploy Smart Contract")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Smart Contract Deployed"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    SmartContract deploySmartContract(@ApiParam(value = "Contract Id", required = true) final UUID id,
                                      @ApiParam(value = "Constructor inputs", required = false) final List<FunctionInput> inputs,
                                   @ApiParam(value = "Authorization", required = false) final String token);


    @ApiOperation(value = "Get methods available in smart contract",
            notes = "Return list of methods available in smart contract")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "list of methods"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    String getSmartContractMethodSpecification(@ApiParam(value = "Contract Id", required = true) final UUID id,
                                   @ApiParam(value = "Authorization", required = false) final String token);


    @ApiOperation(value = "Execute Method in smart contract",
            notes = "call a method in smart contract and return the result")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "To execute method in smart contract"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    Object executeMethodInSmartContract(@ApiParam(value = "Contract Id", required = true) final UUID id,
                                        @ApiParam(value = "Method spec", required = true)
                                         final MethodSpec methodSpec, @ApiParam(value = "Authorization", required = false) final String token);

}
