package sg.com.paloit.hashit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import sg.com.paloit.hashit.dao.entity.HashItUserEntity;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.model.FunctionInput;
import sg.com.paloit.hashit.model.MethodSpec;
import sg.com.paloit.hashit.service.model.ABIDefinition;
import sg.com.paloit.hashit.service.model.FunctionInputOutput;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ContractInteractionManager extends SmartContractManager {

    private Logger LOG = LoggerFactory.getLogger(ContractInteractionManager.class);

    public Object interact(final MethodSpec methodSpec, final SmartContractEntity smartContract, final String token) {
        String walletFileName = walletService.getWalletFileName(token);
        HashItUserEntity hashItUser = hashItUserJPARepository.findOneByWallet(walletFileName);
        Credentials credentials = walletService.getCredentials(hashItUser.getEmail(), walletFileName);

        ABIDefinition[] abiDefinitions = getAbiDefinitions(smartContract);
        Optional<ABIDefinition> abiDefinitionForMethod = getAbiDefinitionForMethod(methodSpec, abiDefinitions);

        if(isView(abiDefinitionForMethod)) {
            return view(methodSpec, smartContract, token, abiDefinitions);
        }

        RawTransaction rawTransaction = RawTransaction.createTransaction(getNonce(credentials, smartContract.getNetworkId()), gasPrice, gasLimit, smartContract.getAddress(),
                getEncodedFunction(methodSpec, abiDefinitions));
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        try {
            EthSendTransaction ethSendTransaction = web3JClient.getClient(smartContract.getNetworkId()).ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            return pollForTransactionReceipt(transactionHash, smartContract.getNetworkId());
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
            throw new FormatException(ValidationMessages.FAILED_TO_EXECUTE_METHOD_IN_SMART_CONTRACT);
        }

    }


    public Object view(final MethodSpec methodSpec, SmartContractEntity smartContract, final String token, ABIDefinition[] abiDefinitions) {
        String walletFileName = walletService.getWalletFileName(token);
        HashItUserEntity hashItUser = hashItUserJPARepository.findOneByWallet(walletFileName);
        Credentials credentials = walletService.getCredentials(hashItUser.getEmail(), walletFileName);

        Optional<ABIDefinition> abiSpec = getAbiDefinitionForMethod(methodSpec, abiDefinitions);

        // Build input parameters list
        List<Type> inputs = methodSpec.getInput().stream().map(functionInput -> {
            FunctionInputOutput inputSpec = abiSpec.get().getInputs()
                    .stream()
                    .filter(abifunc -> abifunc.getName().equals(functionInput.getName()))
                    .findAny()
                    .orElse(null);
            LOG.info("INPUT spec is {}", inputSpec);
            return getTypedData(inputSpec.getType(), functionInput.getValue());
        }).collect(Collectors.toList());

        final Function function = new Function(methodSpec.getName(),
            inputs,
            getReturnType(abiDefinitions, methodSpec)
        );
        String encodedFunction = FunctionEncoder.encode(function);
        try {

            EthCall response = web3JClient.getClient(smartContract.getNetworkId()).ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), smartContract.getAddress(), encodedFunction),
                    DefaultBlockParameterName.LATEST)
                    .sendAsync().get();

            return FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
            throw new FormatException(ValidationMessages.FAILED_TO_EXECUTE_METHOD_IN_SMART_CONTRACT);
        }
    }

    private Map<String, String> methodParamsMap(final MethodSpec methodSpec) {
        return (methodSpec.getInput() != null) ? methodSpec.getInput().stream().collect(
                Collectors.toMap(FunctionInput::getName, FunctionInput::getValue)) : null;
    }

    private List<TypeReference<?>> getReturnType(ABIDefinition[] abiDefinitions, MethodSpec methodSpec) {
        Optional<ABIDefinition> abiSpec = getAbiDefinitionForMethod(methodSpec, abiDefinitions);
        return abiSpec.<List<TypeReference<?>>>map(abiDefinition -> abiDefinition.getOutputs().stream().map(output -> {
            return getTypeReference(output.getType());
        }).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    private String getEncodedFunction(final MethodSpec methodSpec, final ABIDefinition[] abiDefinitions) {
        AtomicReference<String> encodedFunction = new AtomicReference<>();
        Map<String, String> methodParamsMap = methodParamsMap(methodSpec);
        Optional<ABIDefinition> abiSpec = getAbiDefinitionForMethod(methodSpec, abiDefinitions);

        abiSpec.ifPresent(a -> {
            List<Type> types = Arrays.asList();
            if (a.getInputs() != null && a.getInputs().size() > 0) {
                types = a.getInputs().stream().map(i -> getTypedData(i.getType(), methodParamsMap.get(i.getName()))
                ).collect(Collectors.toList());
            }

            Function function = new Function(
                    methodSpec.getName(),
                    types,
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            encodedFunction.set(FunctionEncoder.encode(function));

        });
        return encodedFunction.get();
    }

}

