package htg.bean;

public class DeviceBean {
	
	private String devicename;
	private int width;
	private int height;
	
	//在该设备上 当前运行的软件的包名
	private String currRunPackName;
	
	

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCurrRunPackName() {
		return currRunPackName;
	}

	public void setCurrRunPackName(String currRunPackName) {
		this.currRunPackName = currRunPackName;
	}
	
	

}
