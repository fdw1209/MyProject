package com.ch.sockettest.dao;

import com.ch.sockettest.until.Utils;

public class PreData {
	private long id;
	private long taskId;
	private String sn;
	private String mac;
	private String mac1;
	private String mac2;
	private String mac3;
	private String sn1;
	private String sn2;
	private String sn3;
	private String rsv1;
	private String rsv2;
	private String rsv3;
	private String rsv4;
	private String rsv5;
	private String rsv6;
	private String mesid;

	public static String TABLE_PREDATA = "preData";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		mac = Utils.macformat(mac.toUpperCase());
		this.mac = mac;
	}

	public String getMac1() {
		return mac1;
	}

	public void setMac1(String mac1) {
		if (mac1 != null && Utils.isMac(mac1))
			mac1 = Utils.macformat(mac1.toUpperCase());
		this.mac1 = mac1;
	}

	public String getMac2() {
		return mac2;
	}

	public void setMac2(String mac2) {
		if (mac2 != null && Utils.isMac(mac2))
			mac2 = Utils.macformat(mac2.toUpperCase());
		this.mac2 = mac2;
	}

	public String getMac3() {
		return mac3;
	}

	public void setMac3(String mac3) {
		if (mac3 != null && Utils.isMac(mac3))
			mac3 = Utils.macformat(mac3.toUpperCase());
		this.mac3 = mac3;
	}

	public String getSn1() {
		return sn1;
	}

	public void setSn1(String sn1) {
		this.sn1 = sn1;
	}

	public String getSn2() {
		return sn2;
	}

	public void setSn2(String sn2) {
		this.sn2 = sn2;
	}

	public String getSn3() {
		return sn3;
	}

	public void setSn3(String sn3) {
		this.sn3 = sn3;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getRsv1() {
		return rsv1;
	}

	public void setRsv1(String rsv1) {
		this.rsv1 = rsv1;
	}

	public String getRsv2() {
		return rsv2;
	}

	public void setRsv2(String rsv2) {
		this.rsv2 = rsv2;
	}

	public String getRsv3() {
		return rsv3;
	}

	public void setRsv3(String rsv3) {
		this.rsv3 = rsv3;
	}

	public String getRsv4() {
		return rsv4;
	}

	public void setRsv4(String rsv4) {
		this.rsv4 = rsv4;
	}

	public String getRsv5() {
		return rsv5;
	}

	public void setRsv5(String rsv5) {
		this.rsv5 = rsv5;
	}

	public String getRsv6() {
		return rsv6;
	}

	public void setRsv6(String rsv6) {
		this.rsv6 = rsv6;
	}

	public String getMesid() {
		return mesid;
	}

	public void setMesid(String mesid) {
		this.mesid = mesid;
	}
	
	@Override
	public String toString() {
		return "PreData [id=" + id + ", taskId=" + taskId + ", sn=" + sn + ", mac=" + mac + ", mac1=" + mac1 + ", mac2="
				+ mac2 + ", mac3=" + mac3 + ", sn1=" + sn1 + ", sn2=" + sn2 + ", sn3=" + sn3 + ", rsv1=" + rsv1
				+ ", rsv2=" + rsv2 + ", rsv3=" + rsv3 + ", rsv4=" + rsv4 + ", rsv5=" + rsv5 + ", rsv6=" + rsv6
				+ ", mesid=" + mesid + "]";
	}

}
