package server;

public class Data {
	private String wifimessage;
	private String pictureUrl;
	
	public Data(String wifimessage, String pictureUrl) {
		this.wifimessage = wifimessage;
		this.pictureUrl = pictureUrl;
	}
	
	public void setWifimessage(String wifimessage) {
		this.wifimessage = wifimessage;
	}
	public String getWifimessage() {
		return wifimessage;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
}
