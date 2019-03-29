package htg.htt;

import htg.Commands;
import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

/**
 *    惠头条 刷 文字新闻
 * @author 55376
 *
 */
public class HttTaskThread extends Thread {
	
	
	
	

	// 程序包名
	public static final String PACKAGENAME = "com.cashtoutiao";

	// 程序主页
	private static final String MainTabActivity = "com.cashtoutiao.account.ui.main.MainTabActivity";

	// 文字新闻页
	private static final String NewsDetailActivity = "com.cashtoutiao.news.ui.NewsDetailActivity";

	// 视频新闻页
	private static final String AliVideoDetailActivity = "com.cashtoutiao.alivideodetail.AliVideoDetailActivity";

	// 广告页
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
		boolean clearSuccess = clearWindows();

		if (!clearSuccess) {
			Commands.killApp(run, devicesBean);

			Thread.sleep(1000);
		}

		if (!Commands.getCurrentAct(run, devicesBean).getActivityName().equals(MainTabActivity)) {
			startMainPage();
			Thread.sleep(2000);
		}

		// 往上滑
		moveToUp();

		Thread.sleep(1000);

		// 点击
		tabIn();

		Thread.sleep(5000);

		//
		String activityName = Commands.getCurrentAct(run, devicesBean).getActivityName();
		switch (activityName) {
		case MainTabActivity:
			System.out.println("惠头条： 主页");
			mainProcess();
			break;
		case NewsDetailActivity:
			System.out.println("惠头条： 文本新闻页");
			readTextNews();

			break;
		case AliVideoDetailActivity:
			System.out.println("惠头条： 视频新闻页");
			back();
			Thread.sleep(1000);
			mainProcess();
			break;
		case CustomBrowserWithoutX5:
			System.out.println("惠头条： 内嵌广告页");
			back();
			Thread.sleep(1000);
			mainProcess();
			break;
		case TTLandingPageActivity:
			System.out.println("惠头条： 百度广告页");
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

	// 启动主页
	private void startMainPage() throws Exception {

		Commands.startActivity(run, devicesBean, PACKAGENAME + "/" + MainTabActivity);
		System.out.println("惠头条： 启动主页 ......");
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

	// 阅读文字新闻的方法
	private void readTextNews() throws Exception {
		int windwoSize = Commands.getCurrentAct(run, devicesBean).getSizeOfWindow();

		if (windwoSize > 1) {

			boolean clearSuccess = clearWindows();
			if (!clearSuccess) {
				// 无法清楚 弹框
				startMainPage();
				Thread.sleep(1000);
			} else {
				// 清楚弹框成功 模拟阅读
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

	// 模拟阅读
	private void readRepeatedly() throws Exception {
		for (int i = 0; i < 55; i++) {

			Thread.sleep(4000);

			ActivityBean activityMe = Commands.getCurrentAct(run, devicesBean);

			String currentActivityName = activityMe.getActivityName();

			int sizeOfWindow = activityMe.getSizeOfWindow();

			if (!currentActivityName.equals(NewsDetailActivity)) {
				System.out.println(Thread.currentThread().getName() + "惠头条：: 阅读中断  ： " + i + " current activity : "
						+ currentActivityName + " window size : " + sizeOfWindow);
				break;
			} else {
				if (sizeOfWindow > 1 && !clearWindows()) {
					System.out.println(Thread.currentThread().getName() + "惠头条：: 阅读中断 ： " + i + " current activity : "
							+ currentActivityName + " window size : " + sizeOfWindow);
					break;
				}
			}

			moveToLeft();

			System.out.println(Thread.currentThread().getName() + "惠头条：: 阅读时长 ： " + (i * 4.5) + "s");

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
