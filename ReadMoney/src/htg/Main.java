package htg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import htg.bean.DeviceBean;
import htg.dftt.DfttTaskThread;
import htg.htt.HttTaskThread;
import htg.sb.SbTaskThread;
import htg.shzx.ShzxTaskThread;

public class Main {

	private static List<DeviceBean> devices;
	
	private static Map<String, Thread> threadMap;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Runtime runtime = Runtime.getRuntime();

		try {

			// 获取当前链接的设备
			Process pro_get_deviceslist = runtime.exec(Commands.cmd_get_deviceslist);

			InputStream is_get_deviceslist = pro_get_deviceslist.getInputStream();
			InputStreamReader isr_get_deviceslist = new InputStreamReader(is_get_deviceslist);
			BufferedReader br_get_deviceslist = new BufferedReader(isr_get_deviceslist);

			if (devices == null) {
				devices = new ArrayList<>();
			}

			String rl_get_deviceslist = null;
			while ((rl_get_deviceslist = br_get_deviceslist.readLine()) != null) {

				if (rl_get_deviceslist.contains("	")) {
					rl_get_deviceslist = rl_get_deviceslist.trim().split("	")[0];

					DeviceBean devicesBean = new DeviceBean();
					devicesBean.setDevicename(rl_get_deviceslist);
					devices.add(devicesBean);
				}

			}

			br_get_deviceslist.close();
			isr_get_deviceslist.close();
			is_get_deviceslist.close();
			pro_get_deviceslist.destroy();

			if (devices == null || devices.size() < 1) {
				return;
			}

			// 遍历设备列表，计算每个设备的屏幕宽高
			for (DeviceBean devicesBean : devices) {
				Process pro_get_width_height = runtime.exec(Commands.getPixelCommand(devicesBean.getDevicename()));
				InputStream is_get_width_height = pro_get_width_height.getInputStream();
				InputStreamReader isr_get_width_height = new InputStreamReader(is_get_width_height);
				BufferedReader br_get_width_height = new BufferedReader(isr_get_width_height);

				String rl_get_width_height = br_get_width_height.readLine();
				int width = Integer.parseInt((rl_get_width_height.split(" ")[2].trim().split("x")[0]));
				int height = Integer.parseInt((rl_get_width_height.split(" ")[2].trim().split("x")[1]));
				devicesBean.setWidth(width);
				devicesBean.setHeight(height);

				br_get_width_height.close();
				isr_get_width_height.close();
				is_get_width_height.close();
				pro_get_width_height.destroy();
			}

			// 设置 每个设备 要要运行的程序
			while (true) {
				System.out.println("\n 请选择设备，输入设备前的编号（设置完成请输入y）：\n");

				for (int j = 0; j < devices.size(); j++) {
					DeviceBean deviceBean = devices.get(j);
					System.out.println(" " + j + ", " + deviceBean.getDevicename() + " " + deviceBean.getWidth() + "x"
							+ deviceBean.getHeight() + " " + deviceBean.getCurrRunPackName());
				}

				System.out.println();

				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				String line = br.readLine().trim();

				if (line.equals("y")) {
					System.out.println("\n 设置完成 \n");
					break;
				}

				boolean paramsDeviceOk = false;
				for (int m = 0; m < devices.size(); m++) {
					if (Integer.parseInt(line) == m) {
						paramsDeviceOk = true;
					}
				}

				if (!paramsDeviceOk) {
					System.out.println(" \n 输入有误  程序结束  \n");
					throw new Exception(" \n 输入有误  \n ");
				}

				DeviceBean deviceBean = devices.get(Integer.parseInt(line));

				System.out.println("\n 您选择的设备是 ： " + deviceBean.getDevicename() + "\n");

				System.out.println(" \n 请选择项目，输入项目前的编号：\n");

				for (int s = 0; s < Commons.PACKAGENAMES.size(); s++) {
					System.out.println(" " + s + " , " + Commons.PACKAGENAMES.get(s));
				}

				InputStreamReader isr2 = new InputStreamReader(System.in);
				BufferedReader br2 = new BufferedReader(isr2);
				String line2 = br2.readLine().trim();

				boolean paramsProjectOk = false;
				for (int m = 0; m < Commons.PACKAGENAMES.size(); m++) {
					if (Integer.parseInt(line2) == m) {
						paramsProjectOk = true;
					}
				}

				if (!paramsProjectOk) {
					System.out.println(" \n 输入有误  程序结束  \n");
					throw new Exception(" \n 输入有误  \n ");
				}

				devices.get(Integer.parseInt(line))
						.setCurrRunPackName(Commons.PACKAGENAMES.get(Integer.parseInt(line2)));

			}
			
			
			//开始启动程序
			threadMap = new HashMap<String, Thread>();

			for (DeviceBean devicesBean : devices) {
				String packageName = devicesBean.getCurrRunPackName();
				switch (packageName) {
				case HttTaskThread.PACKAGENAME:
					HttTaskThread taskThread = new HttTaskThread(devicesBean, runtime);
					taskThread.start();
					threadMap.put(devicesBean.getCurrRunPackName(), taskThread);
					break;
				case DfttTaskThread.PACKAGENAME:
					DfttTaskThread dfttTaskThread = new DfttTaskThread(devicesBean, runtime);
					dfttTaskThread.start();
					threadMap.put(devicesBean.getCurrRunPackName(), dfttTaskThread);
					break;
				case SbTaskThread.PACKAGENAME:
					SbTaskThread sbTaskThread = new SbTaskThread(devicesBean, runtime);
					sbTaskThread.start();
					threadMap.put(devicesBean.getCurrRunPackName(), sbTaskThread);
					break;
					
				case ShzxTaskThread.PACKAGENAME:
					ShzxTaskThread shzxTaskThread = new ShzxTaskThread(devicesBean, runtime);
					shzxTaskThread.start();
					threadMap.put(devicesBean.getCurrRunPackName(), shzxTaskThread);
					break;

				default:
					break;
				}
				
			}

			// 开启监护线程
			MonitorThread monitorThread = new MonitorThread(runtime, devices,threadMap);
			monitorThread.start();

			// 开启定时杀死程序线程
			TimerKiller timerKillThread = new TimerKiller(runtime, devices);
			timerKillThread.start();
			

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
