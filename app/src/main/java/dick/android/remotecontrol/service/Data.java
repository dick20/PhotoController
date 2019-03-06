package dick.android.remotecontrol.service;

public class Data {
	private String wifimessage;
	private String pictureUrl;
	
	public Data(String wifimessage, String pictureUrl) {
		this.wifimessage = wifimessage;
		this.pictureUrl = pictureUrl;
	}
	public Data() {
		this.wifimessage = "";
		this.pictureUrl = "";
	}
	public Data(Data data){
		this.wifimessage = data.wifimessage;
		this.pictureUrl = data.pictureUrl;
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
