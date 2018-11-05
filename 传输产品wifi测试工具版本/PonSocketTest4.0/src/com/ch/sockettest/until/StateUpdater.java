package com.ch.sockettest.until;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.ch.sockettest.dao.State;
import com.ch.sockettest.main.MainView;
import com.ch.sockettest.wifi.Ping;
import com.ch.sockettest.wifi.TelnetManager;

/**
 * ×´Ì¬¸üÐÂÆ÷
 * 
 * @author fdw
 * 
 */
public class StateUpdater extends Observable {
	public static State NC_ETH_STATE = State.NOTREADY;
	private TelnetManager telnetm;
	public static boolean closed = false;
	private static String ip = "192.168.1.1";

	public void init() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(new updater());
		executor.shutdown();
	}

	public class updater implements Runnable {

		@Override
		public void run() {
			updateNcEthState();
		}

		private void updateNcEthState() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (!closed) {
						String ip1, ip2;
						try {
							ip1 = IPUtil.getNicInfo(MainView.getProp().getProperty("Dev1_CableNet"));
							ip2 = IPUtil.getNicInfo(MainView.getProp().getProperty("Dev2_CableNet"));
							if (ip1 == null || ip1.equals("")) {
								NC_ETH_STATE = State.NOTREADY;
							}
							if (ip2 == null || ip2.equals("")) {
								NC_ETH_STATE = State.NOTREADY;
							}
							if (Utils.isIP(ip1) && Utils.isIP(ip2)) {
								if (telnetm != null) {
									try {
										telnetm.setIp(ip);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (new Ping().isReachable(ip, 1)) {
									if (telnetm == null) {
										telnetm = new TelnetManager(ip);
										telnetm.start();
									} else if (NC_ETH_STATE != State.CONNECTED) {
										telnetm.start();
									}
									NC_ETH_STATE = State.CONNECTED;
								} else {
									if (telnetm != null && telnetm.isClosed() == false) {
										telnetm.close();
									}
									NC_ETH_STATE = State.OFF;
								}
							} else {
								NC_ETH_STATE = State.OFF;
							}
							try {
								TimeUnit.MILLISECONDS.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("updateNcEthState close");
				}

			}, "updateNcEthState").start();
		}
	}
}
