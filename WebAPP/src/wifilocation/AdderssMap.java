package wifilocation;

public class AdderssMap {
	String address;
	float x;
	float y;
	
	public AdderssMap() {
		this.address = "";
		this.x = 0;
		this.y = 0;
	}
	
	public AdderssMap(String address, float x, float y) {
		this.address = address;
		this.x = x;
		this.y = y;
	}
	
	public AdderssMap(AdderssMap adderssMap) {
		this.address = adderssMap.address;
		this.x = adderssMap.x;
		this.y = adderssMap.y;
	}
	
	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
