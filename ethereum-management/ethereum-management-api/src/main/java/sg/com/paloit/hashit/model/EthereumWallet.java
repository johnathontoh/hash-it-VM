package sg.com.paloit.hashit.model;

import java.io.Serializable;
import java.util.Optional;

public class EthereumWallet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String walletFileName;

    public EthereumWallet() {
    }

    public EthereumWallet(String walletFileName) {
        this.walletFileName = walletFileName;
    }

    public String getWalletFileName() {
        return walletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        this.walletFileName = walletFileName;
    }

    public static class EthereumWalletBuilder {
        private String walletFileName;

        public EthereumWalletBuilder() {
        }

        public EthereumWalletBuilder(EthereumWallet ethereumWallet) {
            Optional.ofNullable(ethereumWallet.getWalletFileName()).ifPresent(this::setWalletFileName);

        }

        public EthereumWalletBuilder setWalletFileName(String walletFileName) {
            this.walletFileName = walletFileName;
            return this;
        }

        public EthereumWallet build() {
            return new EthereumWallet(walletFileName);
        }
    }

    public static EthereumWalletBuilder newBuilder() {
        return new EthereumWalletBuilder();
    }

    public static EthereumWalletBuilder newBuilder(EthereumWallet ethereumWallet) {
        return new EthereumWalletBuilder(ethereumWallet);
    }

    @Override
    public String toString() {
        return "EthereumWallet{" +
                ", walletFileName='" + walletFileName + '\'' +
                '}';
    }
}
