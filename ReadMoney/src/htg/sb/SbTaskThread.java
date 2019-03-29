package htg.sb;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

public class SbTaskThread extends Thread {

	// 程序包名
	public static final String PACKAGENAME = "com.jm.video";

	// 程序主页
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

			// 启动主页
			startMainPage();
			Thread.sleep(1000);

			while (true) {

				mainProcess();

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	// 主流程
	private void mainProcess() throws Exception {

		Thread.sleep(1000 * 20);

		// 往上滑
		moveToUp();

	}

	// 启动主页
	private void startMainPage() throws Exception {

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + MainTabActivity);
		System.out.println("刷宝 ：  启动主页 ......");
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
	 * 清楚弹框
	 * 
	 * @return 是否清楚成功
	 * @throws Exception
	 */
	private boolean clearWindows() throws Exception {

		// 查看 含有的window个数

		Thread.sleep(500);

		int sizeOfWindow = Commands.getCurrentAct(run, devicesBean).getSizeOfWindow();

		if (sizeOfWindow > 1) {
			Thread.sleep(500);
			// 说明含有 弹框
			sizeOfWindow = sizeOfWindow - 1;
			// 通过点击返回键，来取消所有弹框
			for (int i = 0; i < sizeOfWindow; i++) {
				// 返回
				run.exec(Commands.getTabBackCommand(devicesBean.getDevicename()));
				Thread.sleep(500);
			}
		}

		Thread.sleep(1000);

		// 当返回键无法 消除弹框，则试图重新启动 主页
		if (Commands.getCurrentAct(run, devicesBean).getSizeOfWindow() > 1) {
			return false;
		}
		return true;
	}

	// 往上滑
	private void moveToUp() throws Exception {
		run.exec(Commands.getMoveToNextTextCommand(devicesBean.getDevicename(), devicesBean.getWidth(),
				devicesBean.getHeight()));
	}

	// 点击进入
	private void tabIn() throws Exception {
		run.exec(
				Commands.getTabInCommand(devicesBean.getDevicename(), devicesBean.getWidth(), devicesBean.getHeight()));
	}

	// 返回
	private void back() throws Exception {
		run.exec(Commands.getTabBackCommand(devicesBean.getDevicename()));
	}

	
}
