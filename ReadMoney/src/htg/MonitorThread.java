package htg;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import htg.bean.DeviceBean;
import htg.dftt.DfttTaskThread;
import htg.htt.HttTaskThread;
import htg.sb.SbTaskThread;
import htg.shzx.ShzxTaskThread;

public class MonitorThread extends Thread {

	private Runtime runtime;
	private List<DeviceBean> devices;

	// key:程序包名，value:控制程序的綫程
	private Map<String, Thread> threadMap;

	public MonitorThread(Runtime runtime, List<DeviceBean> devices, Map<String, Thread> threadMap) {
		super();
		this.runtime = runtime;
		this.devices = devices;
		this.threadMap = threadMap;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Set<String> set = threadMap.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Thread thread = threadMap.get(key);
				if (!thread.isAlive()) {

					DeviceBean devicesBean = findByName(key);
					if (devicesBean != null) {

						switch (key) {
						case HttTaskThread.PACKAGENAME:
							HttTaskThread taskThread = new HttTaskThread(devicesBean, runtime);
							taskThread.start();
							threadMap.replace(key, thread, taskThread);
							break;
						case DfttTaskThread.PACKAGENAME:
							DfttTaskThread dfttTaskThread = new DfttTaskThread(devicesBean, runtime);
							dfttTaskThread.start();
							threadMap.replace(key, thread, dfttTaskThread);
							break;

						case SbTaskThread.PACKAGENAME:
							SbTaskThread sbTaskThread = new SbTaskThread(devicesBean, runtime);
							sbTaskThread.start();
							threadMap.replace(key, thread, sbTaskThread);
							break;

						case ShzxTaskThread.PACKAGENAME:
							ShzxTaskThread shzxTaskThread = new ShzxTaskThread(devicesBean, runtime);
							shzxTaskThread.start();
							threadMap.replace(key, thread, shzxTaskThread);
							break;

						default:
							break;
						}

					}

				}
			}
		}

	}

	private DeviceBean findByName(String deviceName) {
		for (DeviceBean devicesBean : devices) {
			if (devicesBean.getDevicename().equals(deviceName))
				return devicesBean;
		}
		return null;
	}

}
