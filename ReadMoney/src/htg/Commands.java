package htg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import htg.bean.ActivityBean;
import htg.bean.DeviceBean;

public class Commands {

	public static final String cmd_get_deviceslist = "adb devices";

	// 杀掉程序
	public static void killApp(Runtime run, DeviceBean devicesBean) throws Exception {

		String cmd = "adb -s " + devicesBean.getDevicename() + " shell \"am force-stop " + devicesBean.getCurrRunPackName() + "\"";
		run.exec(cmd);

	}

	// 启动activity
	public static void startActivity(Runtime run, DeviceBean devicesBean, String packageNameAndActivityName)
			throws Exception {
		String cmd = "adb -s " + devicesBean.getDevicename() + " shell am start -n " + packageNameAndActivityName;
		run.exec(cmd);
	}

	// 查看當前Activity 信息
	public static ActivityBean getCurrentAct(Runtime runtime, DeviceBean devicesBean) throws Exception {

		Process process = runtime
				.exec("adb -s " + devicesBean.getDevicename() + " shell \"dumpsys window displays|grep allAppWindow\"");
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readline = br.readLine();

		readline = readline.trim();
		// 取等号右边的
		readline = readline.split("=")[1];

		readline = readline.replace('[', ' ');
		readline = readline.replace(']', ' ');

		readline = readline.trim();

		ActivityBean activityMe = new ActivityBean();

		String[] items;

		if (readline.contains(",")) {
			items = readline.split(",");
			activityMe.setSizeOfWindow(items.length);
		} else {
			items = new String[] { readline };
			activityMe.setSizeOfWindow(1);
		}

		for (String item : items) {
			int start = item.indexOf("{");
			int end = item.indexOf("}");

			item = item.substring(start + 1, end);

			item = item.split(" ")[2];
			if (item.contains("/")) {
				activityMe.setPackageName(item.split("/")[0]);
				activityMe.setActivityName(item.split("/")[1]);
				return activityMe;
			} else {
				continue;
			}
		}

		if (activityMe.getPackageName() != null && !activityMe.getPackageName().equals("")
				&& activityMe.getActivityName() != null && !activityMe.getActivityName().equals("")) {
			return activityMe;
		} else {
			return null;
		}

	}

	public static String getMoveToNextTextCommand(String devicename, int width, int height) {
		// 垂直方向 从屏幕中间点 往上滑
		int downx = (int) (width / 2f);
		int downy = (int) (height / 2f);
		int upx = downx;
		int upy = (int) (height * 385 / 1400f);

		System.out.println("getMoveToNextCommand : " + devicename + "/" + downx + "/" + downy + "/" + upx + "/" + upy);

		return "adb -s " + devicename + " shell input swipe " + downx + " " + downy + " " + upx + " " + upy + "";
	}
	
	public static String getMoveToNextVideoCommand(String devicename, int width, int height) {
		// 垂直方向 从屏幕中间点 往上滑
		int downx = (int) (width / 2f);
		int downy = (int) (height / 2f);
		int upx = downx;
		int upy = (int) (height * 300 / 1400f);

		System.out.println("getMoveToNextCommand : " + devicename + "/" + downx + "/" + downy + "/" + upx + "/" + upy);

		return "adb -s " + devicename + " shell input swipe " + downx + " " + downy + " " + upx + " " + upy + "";
	}

	public static String getTabInCommand(String devicename, int width, int height) {
		int tabx = (int) (width / 2f);
		int taby = (int) (height * 1 / 4f);
		return "adb -s " + devicename + " shell input tap " + tabx + " " + taby + "";
	}

	// 模拟滑动阅读
	public static String getMoveFromRightToLeft(DeviceBean deviceBean,int downx,int downy,int upx,int upy,long duration) {
		return "adb -s " + deviceBean.getDevicename() + " shell input swipe " + downx + " " + downy + " " + upx + " " + upy + " "+duration;
	}

	public static String getTabBackCommand(String devicename) {
		return "adb -s " + devicename + " shell input keyevent 4";
	}

	// 读取手机像素的命令
	public static String getPixelCommand(String devicename) {
		return "adb -s " + devicename + " shell wm size";
	}

	// 查看当前activity的命令
	public static String getActivitysCommand(String devicename) {
		return "adb -s " + devicename + " shell dumpsys activity";
	}

}
