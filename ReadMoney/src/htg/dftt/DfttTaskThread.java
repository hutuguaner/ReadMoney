package htg.dftt;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

/**
 * 东方头条 刷 视频比较好，文字新闻界面广告比较多
 * 
 * @author 55376
 *
 */
public class DfttTaskThread extends Thread {

	// 程序包名
	public static final String PACKAGENAME = "com.songheng.eastnews";

	// 启动页
	public static final String SplashActivity = "com.oa.eastfirst.activity.WelcomeActivity";

	// 程序主页
	private static final String MainTabActivity = "com.songheng.eastfirst.common.view.activity.MainActivity";

	// 文字新闻页
	private static final String NewsDetailActivity = "com.songheng.eastfirst.business.newsdetail.view.activity.NewsDetailH5Activity";

	// 视频新闻页
	private static final String VideoDetailActivity = "com.songheng.eastfirst.business.video.view.activity.VideoDetailActivity";

	// 广告页
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

	// 启动程序
	private void starApp() throws Exception {
		
		System.out.println("东方头条：  启动主页 ......");
		
		Commands.killApp(run, devicesBean);
		Thread.sleep(1000);

		// 启动主页
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

	// 主流程
	private void mainProcess() throws Exception {
		boolean clearSuccess = clearWindows();

		if (!clearSuccess) {
			Commands.killApp(run, devicesBean);
			Thread.sleep(1000);
		}

		if (!Commands.getCurrentAct(run, devicesBean).getActivityName().equals(MainTabActivity)) {
			starApp();
		}

		// 往上滑
		moveToUp();

		Thread.sleep(1000);

		// 点击
		tabIn();

		Thread.sleep(6000);

		//
		String activityName = Commands.getCurrentAct(run, devicesBean).getActivityName();
		switch (activityName) {
		case MainTabActivity:
			System.out.println(" 东方头条 ： 主页");
			mainProcess();
			break;
		case NewsDetailActivity:
			System.out.println(" 东方头条 ：文本新闻页");
			mainProcess();
			break;

		case TTLandingPageActivity:
			System.out.println(" 东方头条 ：广告页");
			back();
			Thread.sleep(1000);
			mainProcess();
		case EastNewActivity:
			System.out.println(" 东方头条 ：广告页");
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

	// 启动主页
	private void startMainPage() throws Exception {

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + SplashActivity);
		/*
		 * System.out.println(" 启动主页 ......"); Thread.sleep(500); ActivityBean
		 * activityBean = Commands.getCurrentAct(run, devicesBean); if (activityBean ==
		 * null) { startMainPage(); } else { String currentActivity =
		 * activityBean.getActivityName(); if (!currentActivity.equals(MainTabActivity))
		 * { back(); startMainPage(); } }
		 */

	}

	// 滑到视频模块
	private void swipeToVideo() throws Exception {
		for (int i = 0; i < 2; i++) {
			Thread.sleep(1000 * 1);
			System.out.println(" 东方头条 ：从右向左滑   ");
			moveToLeft();
			Thread.sleep(1000 * 2);
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
				Thread.sleep(1000);
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
		run.exec(Commands.getMoveToNextVideoCommand(devicesBean.getDevicename(), devicesBean.getWidth(),
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

	// 阅读文字新闻的方法
	private void readTextNews() throws Exception {
		int windwoSize = Commands.getCurrentAct(run, devicesBean).getSizeOfWindow();

		if (windwoSize > 1) {

			boolean clearSuccess = clearWindows();
			if (!clearSuccess) {
				// 无法清楚 弹框
				startMainPage();
				Thread.sleep(1000);
				swipeToVideo();
			} else {
				// 清楚弹框成功 模拟阅读
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

	// 模拟阅读
	private void readRepeatedly() throws Exception {
		for (int i = 0; i < 55; i++) {

			Thread.sleep(4000);

			ActivityBean activityMe = Commands.getCurrentAct(run, devicesBean);

			String currentActivityName = activityMe.getActivityName();

			int sizeOfWindow = activityMe.getSizeOfWindow();

			if (!currentActivityName.equals(NewsDetailActivity)) {
				System.out.println(Thread.currentThread().getName() + "东方头条 ：: 阅读中断  ： " + i + " current activity : "
						+ currentActivityName + " window size : " + sizeOfWindow);
				break;
			} else {
				if (sizeOfWindow > 1 && !clearWindows()) {
					System.out.println(Thread.currentThread().getName() + "东方头条 ：: 阅读中断 ： " + i + " current activity : "
							+ currentActivityName + " window size : " + sizeOfWindow);
					break;
				}
			}

			moveToUp();

			System.out.println(Thread.currentThread().getName() + "东方头条 ：: 阅读时长 ： " + (i * 4.5) + "s");

		}

	}

	// 从右向左滑 模拟阅读
	private void moveToLeft() throws Exception {
		
		int downx = devicesBean.getWidth()*3/4;
		int downy = devicesBean.getHeight()/2;
		int upx = devicesBean.getWidth()*1/4;
		int upy = devicesBean.getHeight()/2;
		
		run.exec(Commands.getMoveFromRightToLeft(devicesBean,downx,downy,upx,upy,100));
	}

}
