package sg.com.paloit.hashit.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import sg.com.paloit.hashit.dao.entity.HashItUserEntity;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.model.FunctionInput;
import sg.com.paloit.hashit.service.model.ABIDefinition;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContractDeploymentManager extends SmartContractManager {

    Logger LOG = LoggerFactory.getLogger(ContractDeploymentManager.class);

    public String deployContract(final SmartContractEntity smartContract, final String token, final List<FunctionInput> constructorArgs) {
        String walletFileName = walletService.getWalletFileName(token);
        HashItUserEntity hashItUser = hashItUserJPARepository.findOneByWallet(walletFileName);
        Credentials credentials = walletService.getCredentials(hashItUser.getEmail(), walletFileName);

        BigInteger nonce = getNonce(credentials, smartContract.getNetworkId());

        ABIDefinition[] abiDefinitions = getAbiDefinitions(smartContract);

        String dataToSend =  getCompiledContract(smartContract, FILE_EXTENSION_BIN);

        if(constructorArgs != null ||  constructorArgs.size() != 0) {
            dataToSend = dataToSend + getEncodedConstructor(getConstructor(abiDefinitions, constructorArgs));
        }

        RawTransaction rawTransaction = RawTransaction.createContractTransaction(
                nonce,
                gasPrice,
                gasLimit,
                BigInteger.ZERO,
                dataToSend
               );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        try {
          EthSendTransaction transactionResponse = web3JClient.getClient(smartContract.getNetworkId()).ethSendRawTransaction(hexValue)
                    .send();
            TransactionReceipt transactionReceipt = pollForTransactionReceipt(transactionResponse.getTransactionHash(), smartContract.getNetworkId());
            String contractAddress = transactionReceipt.getContractAddress();
            if(!StringUtils.isBlank(contractAddress)) {
                LOG.info("Contract address {}", contractAddress);
                return contractAddress;
            }
        } catch (Exception e) {
            LOG.error("Error {}", e.getMessage());
            LOG.debug("Error {}", e);
            throw new FormatException(ValidationMessages.FAILED_TO_DEPLOY_CONTRACT);
        }
        return null;
    }

    private List<Type> getConstructor(ABIDefinition[] abiDefinitions, List<FunctionInput> constructorArgs) {
        List<ABIDefinition> constructorDefinition = getConstructorDefinition(abiDefinitions);
        Map<String, String> constructorDataMap = getConstructorDataMap(constructorArgs);
        List<List<Type>> collect = constructorDefinition.stream().map(a -> a.getInputs().stream().map(i -> getTypedData(i.getType(), constructorDataMap.get(i.getName()))).collect(Collectors.toList())).collect(Collectors.toList());

        return collect.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<ABIDefinition> getConstructorDefinition(ABIDefinition[] abiDefinitions) {
        return Arrays.stream(abiDefinitions).filter(a -> a.getType().equalsIgnoreCase("constructor")).collect(Collectors.toList());
    }

    private String getEncodedConstructor(List<Type> types) {
        return FunctionEncoder.encodeConstructor(types);
    }

    private Map<String, String> getConstructorDataMap(List<FunctionInput> cons) {
        return cons.stream().collect(
                Collectors.toMap(FunctionInput::getName, FunctionInput::getValue));
    }

}
