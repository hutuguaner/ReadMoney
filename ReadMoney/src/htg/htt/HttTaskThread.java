package htg.htt;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

/**
 *    ��ͷ�� ˢ ��������
 * @author 55376
 *
 */
public class HttTaskThread extends Thread {
	
	
	
	

	// �������
	public static final String PACKAGENAME = "com.cashtoutiao";

	// ������ҳ
	private static final String MainTabActivity = "com.cashtoutiao.account.ui.main.MainTabActivity";

	// ��������ҳ
	private static final String NewsDetailActivity = "com.cashtoutiao.news.ui.NewsDetailActivity";

	// ��Ƶ����ҳ
	private static final String AliVideoDetailActivity = "com.cashtoutiao.alivideodetail.AliVideoDetailActivity";

	// ���ҳ
	private static final String CustomBrowserWithoutX5 = "com.cashtoutiao.common.ui.CustomBrowserWithoutX5";
	private static final String TTLandingPageActivity = "com.bytedance.sdk.openadsdk.activity.TTLandingPageActivity";
	private static final String HTouTiaoActivity = "com.cashtoutiao.HTouTiaoActivity";

	private Runtime run;

	private DeviceBean devicesBean;

	public HttTaskThread(DeviceBean devicesBean, Runtime run) {
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
		boolean clearSuccess = clearWindows();

		if (!clearSuccess) {
			Commands.killApp(run, devicesBean);

			Thread.sleep(1000);
		}

		if (!Commands.getCurrentAct(run, devicesBean).getActivityName().equals(MainTabActivity)) {
			startMainPage();
			Thread.sleep(2000);
		}

		// ���ϻ�
		moveToUp();

		Thread.sleep(1000);

		// ���
		tabIn();

		Thread.sleep(5000);

		//
		String activityName = Commands.getCurrentAct(run, devicesBean).getActivityName();
		switch (activityName) {
		case MainTabActivity:
			System.out.println("��ͷ���� ��ҳ");
			mainProcess();
			break;
		case NewsDetailActivity:
			System.out.println("��ͷ���� �ı�����ҳ");
			readTextNews();

			break;
		case AliVideoDetailActivity:
			System.out.println("��ͷ���� ��Ƶ����ҳ");
			back();
			Thread.sleep(1000);
			mainProcess();
			break;
		case CustomBrowserWithoutX5:
			System.out.println("��ͷ���� ��Ƕ���ҳ");
			back();
			Thread.sleep(1000);
			mainProcess();
			break;
		case TTLandingPageActivity:
			System.out.println("��ͷ���� �ٶȹ��ҳ");
			back();
			Thread.sleep(1000);
			mainProcess();
			break;
		case HTouTiaoActivity:
			back();
			Thread.sleep(1000);
			mainProcess();
			break;

		default:
			break;
		}

	}

	// ������ҳ
	private void startMainPage() throws Exception {

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + MainTabActivity);
		System.out.println("��ͷ���� ������ҳ ......");
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

	// �Ķ��������ŵķ���
	private void readTextNews() throws Exception {
		int windwoSize = Commands.getCurrentAct(run, devicesBean).getSizeOfWindow();

		if (windwoSize > 1) {

			boolean clearSuccess = clearWindows();
			if (!clearSuccess) {
				// �޷���� ����
				startMainPage();
				Thread.sleep(1000);
			} else {
				// �������ɹ� ģ���Ķ�
				readRepeatedly();
				back();
				Thread.sleep(1000);

			}
		} else {
			readRepeatedly();
			back();
			Thread.sleep(1000);
		}

	}

	// ģ���Ķ�
	private void readRepeatedly() throws Exception {
		for (int i = 0; i < 55; i++) {

			Thread.sleep(4000);

			ActivityBean activityMe = Commands.getCurrentAct(run, devicesBean);

			String currentActivityName = activityMe.getActivityName();

			int sizeOfWindow = activityMe.getSizeOfWindow();

			if (!currentActivityName.equals(NewsDetailActivity)) {
				System.out.println(Thread.currentThread().getName() + "��ͷ����: �Ķ��ж�  �� " + i + " current activity : "
						+ currentActivityName + " window size : " + sizeOfWindow);
				break;
			} else {
				if (sizeOfWindow > 1 && !clearWindows()) {
					System.out.println(Thread.currentThread().getName() + "��ͷ����: �Ķ��ж� �� " + i + " current activity : "
							+ currentActivityName + " window size : " + sizeOfWindow);
					break;
				}
			}

			moveToLeft();

			System.out.println(Thread.currentThread().getName() + "��ͷ����: �Ķ�ʱ�� �� " + (i * 4.5) + "s");

		}

	}

	// �������� ģ���Ķ�
	private void moveToLeft() throws Exception {
		int downx = devicesBean.getWidth()*3/4;
		int downy = devicesBean.getHeight()/2;
		int upx = devicesBean.getWidth()*1/4;
		int upy = devicesBean.getHeight()/2;
		run.exec(Commands.getMoveFromRightToLeft(devicesBean,downx,downy,upx,upy,100));
	}

}
