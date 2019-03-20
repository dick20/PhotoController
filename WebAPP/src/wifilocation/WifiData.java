package wifilocation;

public class WifiData {
    private String address;
    private String wifiMessage;

    public WifiData() {
        this.address = "";
        this.wifiMessage = "";
    }
    public WifiData(String address, String wifiMessage) {
        this.address = address;
        this.wifiMessage = wifiMessage;
    }
    public  WifiData(WifiData wifiData) {
        this.address = wifiData.address;
        this.wifiMessage = wifiData.wifiMessage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWifiMessage() {
        return wifiMessage;
    }

    public void setWifiMessage(String wifiMessage) {
        this.wifiMessage = wifiMessage;
    }
}
