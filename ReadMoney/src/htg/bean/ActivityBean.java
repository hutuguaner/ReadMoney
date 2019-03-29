package htg.bean;

public class ActivityBean {

	// 包名
	private String packageName;
	// activity名
	private String activityName;
	// window的个数
	private int sizeOfWindow;

	public ActivityBean() {
				super();
			}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getSizeOfWindow() {
		return sizeOfWindow;
	}

	public void setSizeOfWindow(int sizeOfWindow) {
		this.sizeOfWindow = sizeOfWindow;
	}

}
