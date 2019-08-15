package sg.com.paloit.hashit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import sg.com.paloit.hashit.dao.repository.WalletJPARepository;
import sg.com.paloit.hashit.service.model.Wallet;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.io.IOException;

import static java.util.Optional.ofNullable;
import static sg.com.paloit.hashit.security.SecurityConstants.SECRET;
import static sg.com.paloit.hashit.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class WalletService {

    Logger LOG = LoggerFactory.getLogger(WalletService.class);

    @Value("${wallet.file.location}")
    private String walletLocation;

    @Autowired
    private WalletJPARepository walletJPARepository;

    public String getWalletFileName(String token) {
        return ofNullable(token).map(t -> {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(t.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return (claims.getSubject());
        }).orElseGet(() -> "JWT");
    }

    public Wallet getWalletData(String walletFileName) {
        ObjectMapper mapper = new ObjectMapper();
        Wallet wallet = null;
        try {
            String walletJson = walletJPARepository.findByFileName(walletFileName).getContent();
            wallet = mapper.readValue(walletJson, Wallet.class);
        } catch (Exception e) {
            throw new FormatException(ValidationMessages.FAILED_TO_READ_WALLET_DATA);
        }
        return wallet;
    }

    public Credentials getCredentials(String password, String walletFileName) {
        try {
            return WalletUtils.loadCredentials(password, walletLocation + "/" + walletFileName);
        } catch (IOException | CipherException e) {
            LOG.error("Error {}", e.getMessage());
            LOG.debug("Error {}", e);
            throw new FormatException(ValidationMessages.FAILED_TO_READ_WALLET_DATA);
        }
    }
}
