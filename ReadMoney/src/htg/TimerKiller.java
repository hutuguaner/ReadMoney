package htg;

import java.util.List;

import htg.bean.DeviceBean;
import htg.dftt.DfttTaskThread;
import htg.htt.HttTaskThread;


public class TimerKiller extends Thread{
	
	private Runtime runtime;
	private List<DeviceBean> devices;

	public TimerKiller(Runtime runtime, List<DeviceBean> devices) {
		super();
		this.runtime = runtime;
		this.devices = devices;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while (true) {

			try {
				Thread.sleep(1000 * 60 * 60 * 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (DeviceBean devicesBean : devices) {
				try {
					Commands.killApp(runtime, devicesBean);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
