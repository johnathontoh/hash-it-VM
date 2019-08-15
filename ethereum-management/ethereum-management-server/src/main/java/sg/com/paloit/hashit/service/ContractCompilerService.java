package sg.com.paloit.hashit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class ContractCompilerService {

    Logger LOG = LoggerFactory.getLogger(ContractCompilerService.class);

    @Value("${solidity.compile.command}")
    private String solidityCompile;

    @Value("${contract.location}")
    private String contractLocation;

    public void compileSolidityContract(final SmartContractEntity smartContractEntity, final String fileContent) {
        final String fileName = smartContractEntity.getId().toString()  + "/" + smartContractEntity.getName().replace(" ", "_").concat(".sol");
        createSolFile(fileName, fileContent);
        Process p = null;
        try {
            String command = solidityCompile.replace("{contractLocation}", contractLocation).replace("{fileName}", fileName)
                    .replace("{contractId}", smartContractEntity.getId().toString());
            LOG.info("Executing command {}", command);
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            StreamGobbler streamGobbler =
                    new StreamGobbler(p.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = p.waitFor();
            assert exitCode == 0;
            LOG.info("Finished executing command to compile contract");
        } catch(IOException | InterruptedException ex) {
            LOG.error("Error {}", ex.getMessage());
            throw new FormatException(ValidationMessages.FAILED_TO_COMPILE_SMART_CONTRACT);
        }
    }

    private void createSolFile(final String fileName, String fileContent) {
        try {
            LOG.debug("Generating sol file for {}", fileName);
            File file = new File(contractLocation + fileName);
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent);
            writer.close();
        } catch (Exception ex) {
            LOG.error("Error", ex);
            throw new FormatException(ValidationMessages.FAILED_TO_WRITE_SMART_CONTRACT);
        }
    }


    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

}


