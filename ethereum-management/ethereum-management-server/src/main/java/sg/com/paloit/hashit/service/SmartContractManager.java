package sg.com.paloit.hashit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import sg.com.paloit.hashit.FileUtils;
import sg.com.paloit.hashit.Web3JClient;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.dao.repository.HashItUserJPARepository;
import sg.com.paloit.hashit.model.MethodSpec;
import sg.com.paloit.hashit.service.model.ABIDefinition;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SmartContractManager {

    @Value("${gas.price}")
    BigInteger gasPrice;

    @Value("${gas.limit}")
    BigInteger gasLimit;

    @Value("${contract.location}")
    String contractLocation;

    @Autowired
    Web3JClient web3JClient;

    @Autowired
    WalletService walletService;

    @Autowired
    HashItUserJPARepository hashItUserJPARepository;

    protected static final String FILE_EXTENSION_ABI = ".abi";
    protected static final String FILE_EXTENSION_BIN = ".bin";

    private Logger LOG = LoggerFactory.getLogger(ContractDeploymentManager.class);

    protected BigInteger getNonce(Credentials credentials, String networkId) {
        BigInteger nonce = null;
        try {
            EthGetTransactionCount ethGetTransactionCount = web3JClient.getClient(networkId).ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.PENDING).sendAsync().get();

            return ethGetTransactionCount.getTransactionCount();
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
            LOG.debug("Error: {}", e);
            throw new FormatException(ValidationMessages.FAILED_TO_GET_NOUNCE);
        }
    }

    protected TransactionReceipt pollForTransactionReceipt(String transactionHash, String netoworkId) {
        try {
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3JClient.getClient(netoworkId), 2000, 3);
            TransactionReceipt transactionReceipt = processor.waitForTransactionReceipt(transactionHash);
            LOG.info("********************** Polling for Transaction Receipt **********************");
            return transactionReceipt;
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
            throw new FormatException(ValidationMessages.FAILED_TO_GET_TRANSACTION_RECEIPT);
        }
    }

    protected boolean isView(Optional<ABIDefinition> abiDefinitionForMethod) {
        return abiDefinitionForMethod.isPresent() && abiDefinitionForMethod.get().getStateMutability().equalsIgnoreCase("view");
    }

    protected Optional<ABIDefinition> getAbiDefinitionForMethod(MethodSpec methodSpec, ABIDefinition[] abiDefinitions) {
        return Arrays.asList(abiDefinitions).stream().filter(r ->
                r.getName().equalsIgnoreCase(methodSpec.getName()) && r.getType().equalsIgnoreCase("function")).findAny();
    }

    protected ABIDefinition[] getAbiDefinitions(final SmartContractEntity smartContract) {
        String abiContent = getCompiledContract(smartContract, FILE_EXTENSION_ABI);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(abiContent, ABIDefinition[].class);
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
            throw new FormatException(ValidationMessages.FAILED_TO_READ_ABIDEFINITION);
        }
    }


    /***
     * important: Below methods has to be updated for new datatypes
     */

    protected Type getTypedData(String dataType, String data) {
        if (dataType.equalsIgnoreCase("uint256"))
            return new Uint256(new BigInteger(data));
        else if (dataType.equalsIgnoreCase("uint128"))
            return new Uint128(new BigInteger(data));
        else if (dataType.equalsIgnoreCase("string"))
            return new Utf8String(data);
        else if (dataType.equalsIgnoreCase("address")) {
            return new org.web3j.abi.datatypes.Address(data);
        }
        else  {
            throw new NotImplementedException("Need to add the datatype");
        }
    }

    protected TypeReference<?> getTypeReference(String type) {
        if(type.equalsIgnoreCase("uint256")) {
            return new TypeReference<Uint256>() {};
        }
        else if(type.equalsIgnoreCase("uint128")) {
            return new TypeReference<Uint128>() {};
        }
        else if(type.equalsIgnoreCase("string")) {
            return new TypeReference<Utf8String>() {};
        }
        else if(type.equalsIgnoreCase("address[]")) {
            return new TypeReference<DynamicArray<Address>>() {};
        }
        else if(type.equalsIgnoreCase("address")) {
            return new TypeReference<Address>() {};
        }
        else {
            throw new NotImplementedException("Need to add the datatype");
        }
    }

    protected String getCompiledContract(SmartContractEntity smartContractEntity, String extension) {
        return FileUtils.getFileContent.apply(contractLocation +"/"+smartContractEntity.getId()+"/"+getClassName(new String(smartContractEntity.getBody())) + extension);
    }

    private String getClassName(String fileContent) {
        String[] contracts = fileContent.split("contract");
        return contracts[1].split("\\{")[0].trim();
    }
}
