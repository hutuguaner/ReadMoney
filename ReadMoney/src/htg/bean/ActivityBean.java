package htg.bean;

public class ActivityBean {

	// ����
	private String packageName;
	// activity��
	private String activityName;
	// window�ĸ���
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
