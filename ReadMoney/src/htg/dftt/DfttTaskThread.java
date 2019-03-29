package htg.dftt;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

/**
 * ����ͷ�� ˢ ��Ƶ�ȽϺã��������Ž�����Ƚ϶�
 * 
 * @author 55376
 *
 */
public class DfttTaskThread extends Thread {

	// �������
	public static final String PACKAGENAME = "com.songheng.eastnews";

	// ����ҳ
	public static final String SplashActivity = "com.oa.eastfirst.activity.WelcomeActivity";

	// ������ҳ
	private static final String MainTabActivity = "com.songheng.eastfirst.common.view.activity.MainActivity";

	// ��������ҳ
	private static final String NewsDetailActivity = "com.songheng.eastfirst.business.newsdetail.view.activity.NewsDetailH5Activity";

	// ��Ƶ����ҳ
	private static final String VideoDetailActivity = "com.songheng.eastfirst.business.video.view.activity.VideoDetailActivity";

	// ���ҳ
	private static final String TTLandingPageActivity = "com.bdtt.sdk.wmsdk.activity.TTLandingPageActivity";
	private static final String EastNewActivity = "com.songheng.eastnews.EastNewActivity";

	private Runtime run;

	private DeviceBean devicesBean;

	public DfttTaskThread(DeviceBean devicesBean, Runtime run) {
		super();
		this.devicesBean = devicesBean;
		this.run = run;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {

			starApp();

			while (true) {

				mainProcess();

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	// ��������
	private void starApp() throws Exception {
		
		System.out.println("����ͷ����  ������ҳ ......");
		
		Commands.killApp(run, devicesBean);
		Thread.sleep(1000);

		// ������ҳ
		startMainPage();
		Thread.sleep(20000);

		if (Commands.getCurrentAct(run, devicesBean).getSizeOfWindow() > 1) {
			back();
			Thread.sleep(1000 * 2);
			if (Commands.getCurrentAct(run, devicesBean).getSizeOfWindow() > 1) {
				starApp();
			}

		}

		swipeToVideo();
		Thread.sleep(1000*1);
	}

	// ������
	private void mainProcess() throws Exception {
		boolean clearSuccess = clearWindows();

		if (!clearSuccess) {
			Commands.killApp(run, devicesBean);
			Thread.sleep(1000);
		}

		if (!Commands.getCurrentAct(run, devicesBean).getActivityName().equals(MainTabActivity)) {
			starApp();
		}

		// ���ϻ�
		moveToUp();

		Thread.sleep(1000);

		// ���
		tabIn();

		Thread.sleep(6000);

		//
		String activityName = Commands.getCurrentAct(run, devicesBean).getActivityName();
		switch (activityName) {
		case MainTabActivity:
			System.out.println(" ����ͷ�� �� ��ҳ");
			mainProcess();
			break;
		case NewsDetailActivity:
			System.out.println(" ����ͷ�� ���ı�����ҳ");
			mainProcess();
			break;

		case TTLandingPageActivity:
			System.out.println(" ����ͷ�� �����ҳ");
			back();
			Thread.sleep(1000);
			mainProcess();
		case EastNewActivity:
			System.out.println(" ����ͷ�� �����ҳ");
			back();
			Thread.sleep(1000*2);
			mainProcess();
			break;
		case VideoDetailActivity:
			Thread.sleep(1000 * 60 * 1);
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

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + SplashActivity);
		/*
		 * System.out.println(" ������ҳ ......"); Thread.sleep(500); ActivityBean
		 * activityBean = Commands.getCurrentAct(run, devicesBean); if (activityBean ==
		 * null) { startMainPage(); } else { String currentActivity =
		 * activityBean.getActivityName(); if (!currentActivity.equals(MainTabActivity))
		 * { back(); startMainPage(); } }
		 */

	}

	// ������Ƶģ��
	private void swipeToVideo() throws Exception {
		for (int i = 0; i < 2; i++) {
			Thread.sleep(1000 * 1);
			System.out.println(" ����ͷ�� ����������   ");
			moveToLeft();
			Thread.sleep(1000 * 2);
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
				Thread.sleep(1000);
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
		run.exec(Commands.getMoveToNextVideoCommand(devicesBean.getDevicename(), devicesBean.getWidth(),
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
				swipeToVideo();
			} else {
				// �������ɹ� ģ���Ķ�
				Thread.sleep(1000 * 10);
				back();
				Thread.sleep(1000);

			}
		} else {
			Thread.sleep(1000 * 10);
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
				System.out.println(Thread.currentThread().getName() + "����ͷ�� ��: �Ķ��ж�  �� " + i + " current activity : "
						+ currentActivityName + " window size : " + sizeOfWindow);
				break;
			} else {
				if (sizeOfWindow > 1 && !clearWindows()) {
					System.out.println(Thread.currentThread().getName() + "����ͷ�� ��: �Ķ��ж� �� " + i + " current activity : "
							+ currentActivityName + " window size : " + sizeOfWindow);
					break;
				}
			}

			moveToUp();

			System.out.println(Thread.currentThread().getName() + "����ͷ�� ��: �Ķ�ʱ�� �� " + (i * 4.5) + "s");

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
