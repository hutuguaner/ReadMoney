package htg.shzx;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

/**
 * �Ѻ���Ѷ  ˢ ��Ƶ�ȽϺã��������Ž�����Ƚ϶�
 * 
 * @author 55376
 *
 */
public class ShzxTaskThread extends Thread {

	// �������
	public static final String PACKAGENAME = "com.sohu.infonews";

	// ����ҳ
	public static final String SplashActivity = "com.sohu.quicknews.splashModel.activity.SplashActivity";

	// ������ҳ
	private static final String MainTabActivity = "com.sohu.quicknews.homeModel.activity.HomeActivity";

	// ��������ҳ
	

	// ��Ƶ����ҳ
	private static final String VideoDetailActivity = "com.sohu.quicknews.articleModel.activity.VideoDetailActivity";

	// ���ҳ
	private static final String AppActivity = "com.baidu.mobads.AppActivity";

	private Runtime run;

	private DeviceBean devicesBean;

	public ShzxTaskThread(DeviceBean devicesBean, Runtime run) {
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
		
		System.out.println("�Ѻ���Ѷ ��  ������ҳ ......");
		
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
			System.out.println("�Ѻ���Ѷ �� ��ҳ");
			//�Ķ�
			Thread.sleep(1000*60);
			mainProcess();
			break;

		case AppActivity:
			System.out.println("�Ѻ���Ѷ �� ���ҳ");
			back();
			Thread.sleep(1000*1);
			mainProcess();
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

	
	// �������� ģ���Ķ�
	private void moveToLeft() throws Exception {
		System.out.println(" �Ѻ���Ѷ ����������");
		int downx = devicesBean.getWidth()*4/8;
		int downy = devicesBean.getHeight()*2/8;
		int upx = devicesBean.getWidth()*3/8;
		int upy = devicesBean.getHeight()*2/8;
		
		run.exec(Commands.getMoveFromRightToLeft(devicesBean,downx,downy,upx,upy,100));
	}

}
