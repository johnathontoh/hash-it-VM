package sg.com.paloit.hashit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.com.paloit.hashit.EthereumManagementClient;
import sg.com.paloit.hashit.EthereumManagementClientFactory;
import sg.com.paloit.hashit.RestExecutor;
import sg.com.paloit.hashit.dao.entity.HashItUserEntity;
import sg.com.paloit.hashit.dao.repository.HashItUserJPARepository;
import sg.com.paloit.hashit.model.EthereumWallet;
import sg.com.paloit.hashit.model.HashItUser;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;
import static sg.com.paloit.hashit.EthereumManagementApiInfo.API_CREATE_WALLET_PATH;
import static sg.com.paloit.hashit.EthereumManagementApiInfo.API_ETHEREUM_PATH;

@Service
public class AuthenticationService {
    @Autowired
    private HashItUserJPARepository repository;

    Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    public HashItUser register(final HashItUser hashItUser) {
        HashItUser newUser;
        Optional<HashItUserEntity> oldUser = ofNullable(repository
                .findOneByEmail(hashItUser.getEmail()));
        if (oldUser.isPresent()) {
            LOG.info("User already registered");
            return convert(oldUser.get());
        } else {
            newUser = convert(repository.save(map(HashItUser.newBuilder(hashItUser)
                    .setWallet(createWallet(hashItUser).getWalletFileName()).build())));
        }
        return newUser;
    }

    private EthereumWallet createWallet(final HashItUser hashItUser) {
        CompletableFuture<EthereumWallet> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
             EthereumManagementClient client = EthereumManagementClientFactory
                    .getInstance(API_ETHEREUM_PATH + API_CREATE_WALLET_PATH);
            client.createWallet(hashItUser)
                    .onSuccess(getWallet(completableFuture))
                    .onError((e,d) -> completableFuture.cancel(true))
                    .execute();
        });

        try {
            return completableFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Error {}", e);
            throw new FormatException(ValidationMessages.WALLET_CREATION_FAILED);
        }
    }

    private RestExecutor.OnSuccess<ResponseEntity> getWallet(final CompletableFuture<EthereumWallet>
                                                                              completableFuture) {
        return ex -> {
            if (ofNullable((EthereumWallet) ex.getBody()).isPresent()) {
                LOG.info("Retrieved Wallet:- {}", ex.getBody());
                completableFuture.complete((EthereumWallet) ex.getBody());
            } else {
                completableFuture.cancel(true);
            }
        };
    }

    private HashItUserEntity map(final HashItUser hashItUser) {
        HashItUserEntity.HashItUserEntityBuilder builder = HashItUserEntity.newBuilder();
        ofNullable(hashItUser.getId()).ifPresent(builder::setId);
        ofNullable(hashItUser.getUserId()).ifPresent(builder::setUserId);
        ofNullable(hashItUser.getEmail()).ifPresent(builder::setPassword);
        ofNullable(hashItUser.getWallet()).ifPresent(builder::setWallet);
        return builder.build();
    }

    private HashItUser convert(final HashItUserEntity entity) {
        if (entity == null || entity.getId() == null) {
            return null;
        }
        HashItUser.HashItUserBuilder builder = HashItUser.newBuilder();
        ofNullable(entity.getId()).ifPresent(builder::setId);
        ofNullable(entity.getUserId()).ifPresent(builder::setUserId);
        ofNullable(entity.getWallet()).ifPresent(builder::setWallet);
        ofNullable(entity.getEmail()).ifPresent(builder::setEmail);
        return builder.build();
    }
}
