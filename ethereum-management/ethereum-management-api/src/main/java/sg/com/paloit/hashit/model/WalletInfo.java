package sg.com.paloit.hashit.model;

public class WalletInfo {
    String publicAddress;

    public WalletInfo(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }
}
