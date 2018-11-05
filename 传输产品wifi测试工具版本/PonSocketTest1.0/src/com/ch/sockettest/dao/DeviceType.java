package com.ch.sockettest.dao;

import java.lang.reflect.Field;

/**
 * 设备型号
 * 
 * @author yuanji
 * 
 */
public enum DeviceType {
	// WIFI PRODUCTS--------------------------------------------------
	@Language(value = "WiFi Modual of Broadcom", cn = "博通WiFi模块")
	WIFI_BCM, // 博通WIFI模块,
	@Language(value = "WiFi Modual of Realtek", cn = "RTL WiFi模块")
	WIFI_RTL, // RTL WIFI模块,8192
	@Language(value = "WiFi Modual of Realtek", cn = "RTL8676S WiFi模块")
	WIFI_RTL8676S, // RTL8676S WiFi模块
	// ROUTER
	@Language(value = "CWR900", cn = "CWR900")
	CWR900, //
	@Language(value = "RP120", cn = "RP120")
	RP120, //
	// CM PRODUCTS-----------------------------------------------
	@Language(value = "CM100UW21CN", cn = "内蒙CABLE猫带WIFI")
	CM100UW21CN, // 内蒙CABLE猫带WIFI模块
	@Language(value = "CM100UW21EI", cn = "印度CABLE猫带WIFI")
	CM100UW21EI, // 印度CABLE猫带WIFI模块
	@Language(value = "CM500UW41", cn = "珠江数码CM500UW41")
	CM500UW41ZJ, // 珠江数码CM 多网口带WIFI
	@Language(value = "CM500UW11HW", cn = "印度CM500UW11")
	CM500UW11, // 印度CM 单网口带WIFI
	@Language(value = "CM500UW1_SI", cn = "CM500UW1_SI")
	CM500UW1_SI, // 印度CM 单网口带BRCM WIFI
	@Language(value = "CM500GH/CM500ZJ", cn = "CM500GH/CM500ZJ")
	CM500, // CM3.0 单网口，不带WIFI，歌华/珠江通用
	@Language(value = "CM500U", cn = "CM500U")
	CM500U, // CM3.0 单网口，不带WIFI，歌华/珠江通用
	@Language(value = "CM500 W4F2D TU", cn = "CM500 W4F2D TU")
	CM500W4F2DTU,
	// Pon Products--------------------------------------
	@Language(value = "CH801_A21", cn = "光猫A21")
	CH801_A21, // 光猫
	@Language(value = "EP_3000_GX", cn = "广西EP_3000")
	EP_3000_GX, // MTK方案的光猫，用于广西
	@Language(value = "EP921", cn = "EP921")
	EP921, // MTK 7525的带光模块的方案
	@Language(value = "GP921", cn = "GP921")
	GP921, // MTK 7525的带光模块的方案
	@Language(value = "GP920", cn = "GP920")
	GP920, // HISI SD5116H的带光模块的方案
	@Language(value = "GP920", cn = "四川移动4K-GP920")
	GP920_4K_SCCM, // 四川移动4K-GP920,ZTE 127方案
	@Language(value = "ES100UW2", cn = "ES100UW2")
	ES100UW2, // 双网口带wifi的EPON,MTK 7526方案
	@Language(value = "GS100UW1", cn = "GS100UW1")
	GS100UW1, // 双网口带wifi的GPON,MTK 7526方案
	/** 单频wifi的GPON,ZTE 127方案 */
	@Language(value = "GS100W", cn = "GS100W")
	GS100W, // 单频wifi的GPON,ZTE 127方案
	/** 双频wifi的GPON,ZTE 127方案 */
	@Language(value = "GS100DW", cn = "GS100DW")
	GS100DW, // 双频wifi的GPON,ZTE 127方案
	@Language(value = "GS200DW", cn = "GS200DW(h1s-2)")
	GS200DW, // 双频wifi的GPON,ZTE 128方案
	@Language(value = "ES100U4BI", cn = "ES100U4BI")
	ES100U4BI, // 4网口的带电池的EPON,MTK 7520方案
	@Language(value = "EP921_HUNAN", cn = "EP921(湖南)")
	EP921_HUNAN, // MTK 7525的带BOSA的方案
	@Language(value = "ES100U1Z", cn = "ES100U1Z")
	ES100U1Z, // ZTE 279110方案，EPON，带BOSA
	@Language(value = "GS100U1Z", cn = "GS100U1Z")
	GS100U1Z, // ZTE 279110方案，GPON,带BOSA
	@Language(value = "GS100_RW4F2_AR", cn = "GS100_RW4F2_AR(阿根廷)")
	GS100_RW4F2_AR, // 阿根廷GS100 RW4F2 AR

	// Fused Mode Products-------------------------------------------
	@Language(value = "OTS-3000 HB", cn = "OTS-3000 HB")
	OTS_3000_HB, //
	@Language(value = "HMG3000", cn = "HMG3000(重庆)")
	HMG3000, //
	@Language(value = "HMG3000G", cn = "HMG3000G-邦天代工")
	HMG3000G, // 给邦天代工的融合型网关产品
	@Language(value = "HMG3000GE", cn = "HMG3000GE(高安EoC)")
	HMG3000GE, // 给邦天代工的融合型网关产品+EOC
	@Language(value = "OTS_4K_SC", cn = "OTS-4K SC(基本型)")
	OTS_4K_SC, // OTS-4K 基本型，IPTV+WiFi
	@Language(value = "OTS_4K_SCE", cn = "OTS-4K SCE(融合型EOC)")
	OTS_4K_SCE, // OTS-4K 融合型EOC，IPTV+WiFi+EOC
	@Language(value = "OTS_4K_SCC", cn = "OTS-4K SCC(融合型CM)")
	OTS_4K_SCC, // OTS-4K 融合型CM，IPTV+WiFi+CM
	// Function Products--------------------------------
	@Language(value = "ZTE BOSA", cn = "ZTE BOSA")
	ZTE_BOSA, // 中兴的BOSA方案
	@Language(value = "MTK BOSA", cn = "MTK BOSA")
	MTK_BOSA, // MTK的BOSA方案
	@Language(value = "BOB 8 IN 1", cn = "Pon调测_八通道")
	PON_8IN1, //
	@Language(value = "BOB 8 IN 1", cn = "BOB调测_8合1")
	BOB_8IN1, //
	// EOC PRODUCTS--------------------------------------------
	@Language(value = "EOC_CNU_HPAV", cn = "HPAV方案EoC终端")
	EOC_CNU_HPAV, // EOC终端，HOMEPLUG AV方案
	@Language(value = "EOC_HS200UW4S", cn = "陕西网关EOC_HS200UW4S")
	EOC_HS200UW4S, // 陕西网关,4网口带WIFI带EOC 7411L模块
	@Language(value = "HS200_U4M_SC", cn = "川网外置EoC")
	HS200_U4M_SC, //
	// ADSL PRODUCTS--------------------------------------------
	@Language(value = "ADSL_BHS_V3", cn = "ADSL_BHS_V3")
	ADSL_BHS_V3, // 南美ADSL
	@Language(value = "ADSL_CH210E", cn = "ADSL_CH210E")
	ADSL_CH210E, // 非洲 埃塞俄比亚 ADSL
	// MOCA PRODUCTS--------------------------------------------------
	@Language(value = "MOCA1_0_NC", cn = "MoCa1.0局端")
	MOCA1_0_NC, // moca1.0局端
	@Language(value = "MOCA1_0_CPE", cn = "MoCa1.0终端")
	MOCA1_0_CPE, // moca1.0终端
	@Language(value = "MOCA1_1_NC", cn = "MoCa1.1局端")
	MOCA1_1_NC, // moca1.1局端
	@Language(value = "MOCA1_1_CPE", cn = "MoCa1.1终端")
	MOCA1_1_CPE, // moca1.1终端
	// 物联网产品型号
	@Language(value = "[IOT]Geomagnetic Vehicle Sensors", cn = "[物联网]地磁车检器")
	IOT_GVS, //
	@Language(value = "[IOT]Bluetooth Locator", cn = "[物联网]蓝牙定位器")
	IOT_BLL, // 蓝牙定位器
	@Language(value = "[IOT]Intelligent Meter Reading Terminal", cn = "[物联网]智能抄表终端")
	IOT_IMRT, // 智能抄表终端
	;//

	public String getlabel() {
		Field f = null;
		String lbl = null;
		try {
			f = this.getClass().getDeclaredField(this.toString());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (f != null) {
			lbl = f.getAnnotation(Language.class).cn();
		}
		return lbl == null ? "" : lbl;

	}

	public static DeviceType getValue(String str) {
		DeviceType[] fields = DeviceType.values();
		for (DeviceType tt : fields) {
			Field f = null;
			try {
				f = DeviceType.class.getDeclaredField(tt.toString());

			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
			}
			if (f != null && f.getAnnotation(Language.class).cn().equals(str))
				return tt;
		}

		return null;
	}

	public static String[] list() {
		DeviceType[] typs = DeviceType.values();
		String[] lbs = new String[typs.length];
		for (int i = 0; i < typs.length; i++) {
			lbs[i] = typs[i].getlabel();
		}
		return lbs;
	}
}
