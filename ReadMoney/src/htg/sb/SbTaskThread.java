package htg.sb;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

public class SbTaskThread extends Thread {

	// �������
	public static final String PACKAGENAME = "com.jm.video";

	// ������ҳ
	private static final String MainTabActivity = "com.jm.video.ui.main.MainActivity";

	private Runtime run;

	private DeviceBean devicesBean;

	public SbTaskThread(DeviceBean devicesBean, Runtime run) {
		super();
		this.devicesBean = devicesBean;
		this.run = run;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {

			// ������ҳ
			startMainPage();
			Thread.sleep(1000);

			while (true) {

				mainProcess();

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	// ������
	private void mainProcess() throws Exception {

		Thread.sleep(1000 * 20);

		// ���ϻ�
		moveToUp();

	}

	// ������ҳ
	private void startMainPage() throws Exception {

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + MainTabActivity);
		System.out.println("ˢ�� ��  ������ҳ ......");
		Thread.sleep(500);
		ActivityBean activityBean = Commands.getCurrentAct(run, devicesBean);
		if (activityBean == null) {
			startMainPage();
		} else {
			String currentActivity = activityBean.getActivityName();
			if (!currentActivity.equals(MainTabActivity)) {
				startMainPage();
			}
		}

	}

	/**
	 * �������
	 * 
	 * @return �Ƿ�����ɹ�
	 * @throws Exception
	 */
	private boolean clearWindows() throws Exception {

		// �鿴 ���е�window����

		Thread.sleep(500);

		int sizeOfWindow = Commands.getCurrentAct(run, devicesBean).getSizeOfWindow();

		if (sizeOfWindow > 1) {
			Thread.sleep(500);
			// ˵������ ����
			sizeOfWindow = sizeOfWindow - 1;
			// ͨ��������ؼ�����ȡ�����е���
			for (int i = 0; i < sizeOfWindow; i++) {
				// ����
				run.exec(Commands.getTabBackCommand(devicesBean.getDevicename()));
				Thread.sleep(500);
			}
		}

		Thread.sleep(1000);

		// �����ؼ��޷� ������������ͼ�������� ��ҳ
		if (Commands.getCurrentAct(run, devicesBean).getSizeOfWindow() > 1) {
			return false;
		}
		return true;
	}

	// ���ϻ�
	private void moveToUp() throws Exception {
		run.exec(Commands.getMoveToNextTextCommand(devicesBean.getDevicename(), devicesBean.getWidth(),
				devicesBean.getHeight()));
	}

	// �������
	private void tabIn() throws Exception {
		run.exec(
				Commands.getTabInCommand(devicesBean.getDevicename(), devicesBean.getWidth(), devicesBean.getHeight()));
	}

	// ����
	private void back() throws Exception {
		run.exec(Commands.getTabBackCommand(devicesBean.getDevicename()));
	}

	
}
