package sg.com.paloit.hashit.service;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import sg.com.paloit.hashit.FileUtils;
import sg.com.paloit.hashit.dao.entity.SmartContractEntity;
import sg.com.paloit.hashit.dao.entity.TemplateEntity;
import sg.com.paloit.hashit.dao.entity.WalletEntity;
import sg.com.paloit.hashit.dao.repository.SmartContractJPARepository;
import sg.com.paloit.hashit.dao.repository.TemplatesJPARepository;
import sg.com.paloit.hashit.dao.repository.WalletJPARepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataLoadService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Map<String, String> templateMap =   Stream.of(new String[][] {
            { "Medical History", "MedicalHistory.sol" }
    }).collect(Collectors.collectingAndThen(Collectors.toMap(data -> data[0], data -> data[1]), Collections::unmodifiableMap));


    @Value("${contract.location}")
    private String contractLocation;

    @Value("${wallet.file.location}")
    private String walletLocation;

    @Autowired
    private TemplatesJPARepository templatesJPARepository;

    @Autowired
    private WalletJPARepository walletJPARepository;

    @Autowired
    private SmartContractJPARepository smartContractJPARepository;

    @Autowired
    private ContractCompilerService contractCompilerService;

    private Logger LOG = LoggerFactory.getLogger(DataLoadService.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            loadTemplateData();
            loadWalletsToFileDirectory();
            generateSolAbiAndBinFilesForContractsThatAreDeployed();
        } catch (Exception e) {
            LOG.error("Error {}", e.getMessage());
        }
    }

    private void loadTemplateData() {
        try {
            templateMap.keySet().forEach(k -> {
                TemplateEntity savedTemplate = templatesJPARepository.findByName(k);
                TemplateEntity entity;
                String contract = FileUtils.getFileContent.apply(contractLocation + "/" + templateMap.get(k));
                final String jsonSchemaFileName = FilenameUtils.removeExtension(templateMap.get(k)) + ".json";
                final String jsonSchema = FileUtils.getFileContent.apply(contractLocation + "/" + jsonSchemaFileName);
                if (savedTemplate == null) {
                    entity = TemplateEntity.newBuilder()
                            .setBody(contract.getBytes())
                            .setName(k)
                            .setJsonSchema(jsonSchema)
                            .build();
                } else {
                    entity = TemplateEntity.newBuilder()
                            .setBody(contract.getBytes())
                            .setJsonSchema(jsonSchema)
                            .setName(savedTemplate.getName())
                            .setId(savedTemplate.getId())
                            .build();
                }
                templatesJPARepository.save(entity);
            });
        } catch (Exception e) {
            LOG.error("Loading of data failed");
        }
    }

    private void loadWalletsToFileDirectory() {
        LOG.info("Loading wallet data to file directory");
        Iterable<WalletEntity> walletEntities = walletJPARepository.findAll();
        walletEntities.forEach(f -> {
            try {
                Files.write(Paths.get(walletLocation+"/"+f.getFileName()), f.getContent().getBytes());
            } catch (IOException e) {
                LOG.error("Error in loading file to directory {} , {}", walletLocation, e.getMessage());
            }
        });
    }


    private void generateSolAbiAndBinFilesForContractsThatAreDeployed() {
        Iterable<SmartContractEntity> allSmartContract = smartContractJPARepository.findAll();
        allSmartContract.forEach(i -> {
            SmartContractEntity c = i;
            Boolean deployed = c.getDeployed();
            if(deployed) {
                LOG.debug("Generating sol compiled files");
                contractCompilerService.compileSolidityContract(c, new String(c.getBody()));
            }
        });

    }

}
