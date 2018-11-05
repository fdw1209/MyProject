package com.ch.sockettest.dao;

import java.lang.reflect.Field;

/**
 * �豸�ͺ�
 * 
 * @author yuanji
 * 
 */
public enum DeviceType {
	// WIFI PRODUCTS--------------------------------------------------
	@Language(value = "WiFi Modual of Broadcom", cn = "��ͨWiFiģ��")
	WIFI_BCM, // ��ͨWIFIģ��,
	@Language(value = "WiFi Modual of Realtek", cn = "RTL WiFiģ��")
	WIFI_RTL, // RTL WIFIģ��,8192
	@Language(value = "WiFi Modual of Realtek", cn = "RTL8676S WiFiģ��")
	WIFI_RTL8676S, // RTL8676S WiFiģ��
	// ROUTER
	@Language(value = "CWR900", cn = "CWR900")
	CWR900, //
	@Language(value = "RP120", cn = "RP120")
	RP120, //
	// CM PRODUCTS-----------------------------------------------
	@Language(value = "CM100UW21CN", cn = "����CABLEè��WIFI")
	CM100UW21CN, // ����CABLEè��WIFIģ��
	@Language(value = "CM100UW21EI", cn = "ӡ��CABLEè��WIFI")
	CM100UW21EI, // ӡ��CABLEè��WIFIģ��
	@Language(value = "CM500UW41", cn = "�齭����CM500UW41")
	CM500UW41ZJ, // �齭����CM �����ڴ�WIFI
	@Language(value = "CM500UW11HW", cn = "ӡ��CM500UW11")
	CM500UW11, // ӡ��CM �����ڴ�WIFI
	@Language(value = "CM500UW1_SI", cn = "CM500UW1_SI")
	CM500UW1_SI, // ӡ��CM �����ڴ�BRCM WIFI
	@Language(value = "CM500GH/CM500ZJ", cn = "CM500GH/CM500ZJ")
	CM500, // CM3.0 �����ڣ�����WIFI���軪/�齭ͨ��
	@Language(value = "CM500U", cn = "CM500U")
	CM500U, // CM3.0 �����ڣ�����WIFI���軪/�齭ͨ��
	@Language(value = "CM500 W4F2D TU", cn = "CM500 W4F2D TU")
	CM500W4F2DTU,
	// Pon Products--------------------------------------
	@Language(value = "CH801_A21", cn = "��èA21")
	CH801_A21, // ��è
	@Language(value = "EP_3000_GX", cn = "����EP_3000")
	EP_3000_GX, // MTK�����Ĺ�è�����ڹ���
	@Language(value = "EP921", cn = "EP921")
	EP921, // MTK 7525�Ĵ���ģ��ķ���
	@Language(value = "GP921", cn = "GP921")
	GP921, // MTK 7525�Ĵ���ģ��ķ���
	@Language(value = "GP920", cn = "GP920")
	GP920, // HISI SD5116H�Ĵ���ģ��ķ���
	@Language(value = "GP920", cn = "�Ĵ��ƶ�4K-GP920")
	GP920_4K_SCCM, // �Ĵ��ƶ�4K-GP920,ZTE 127����
	@Language(value = "ES100UW2", cn = "ES100UW2")
	ES100UW2, // ˫���ڴ�wifi��EPON,MTK 7526����
	@Language(value = "GS100UW1", cn = "GS100UW1")
	GS100UW1, // ˫���ڴ�wifi��GPON,MTK 7526����
	/** ��Ƶwifi��GPON,ZTE 127���� */
	@Language(value = "GS100W", cn = "GS100W")
	GS100W, // ��Ƶwifi��GPON,ZTE 127����
	/** ˫Ƶwifi��GPON,ZTE 127���� */
	@Language(value = "GS100DW", cn = "GS100DW")
	GS100DW, // ˫Ƶwifi��GPON,ZTE 127����
	@Language(value = "GS200DW", cn = "GS200DW(h1s-2)")
	GS200DW, // ˫Ƶwifi��GPON,ZTE 128����
	@Language(value = "ES100U4BI", cn = "ES100U4BI")
	ES100U4BI, // 4���ڵĴ���ص�EPON,MTK 7520����
	@Language(value = "EP921_HUNAN", cn = "EP921(����)")
	EP921_HUNAN, // MTK 7525�Ĵ�BOSA�ķ���
	@Language(value = "ES100U1Z", cn = "ES100U1Z")
	ES100U1Z, // ZTE 279110������EPON����BOSA
	@Language(value = "GS100U1Z", cn = "GS100U1Z")
	GS100U1Z, // ZTE 279110������GPON,��BOSA
	@Language(value = "GS100_RW4F2_AR", cn = "GS100_RW4F2_AR(����͢)")
	GS100_RW4F2_AR, // ����͢GS100 RW4F2 AR

	// Fused Mode Products-------------------------------------------
	@Language(value = "OTS-3000 HB", cn = "OTS-3000 HB")
	OTS_3000_HB, //
	@Language(value = "HMG3000", cn = "HMG3000(����)")
	HMG3000, //
	@Language(value = "HMG3000G", cn = "HMG3000G-�������")
	HMG3000G, // ������������ں������ز�Ʒ
	@Language(value = "HMG3000GE", cn = "HMG3000GE(�߰�EoC)")
	HMG3000GE, // ������������ں������ز�Ʒ+EOC
	@Language(value = "OTS_4K_SC", cn = "OTS-4K SC(������)")
	OTS_4K_SC, // OTS-4K �����ͣ�IPTV+WiFi
	@Language(value = "OTS_4K_SCE", cn = "OTS-4K SCE(�ں���EOC)")
	OTS_4K_SCE, // OTS-4K �ں���EOC��IPTV+WiFi+EOC
	@Language(value = "OTS_4K_SCC", cn = "OTS-4K SCC(�ں���CM)")
	OTS_4K_SCC, // OTS-4K �ں���CM��IPTV+WiFi+CM
	// Function Products--------------------------------
	@Language(value = "ZTE BOSA", cn = "ZTE BOSA")
	ZTE_BOSA, // ���˵�BOSA����
	@Language(value = "MTK BOSA", cn = "MTK BOSA")
	MTK_BOSA, // MTK��BOSA����
	@Language(value = "BOB 8 IN 1", cn = "Pon����_��ͨ��")
	PON_8IN1, //
	@Language(value = "BOB 8 IN 1", cn = "BOB����_8��1")
	BOB_8IN1, //
	// EOC PRODUCTS--------------------------------------------
	@Language(value = "EOC_CNU_HPAV", cn = "HPAV����EoC�ն�")
	EOC_CNU_HPAV, // EOC�նˣ�HOMEPLUG AV����
	@Language(value = "EOC_HS200UW4S", cn = "��������EOC_HS200UW4S")
	EOC_HS200UW4S, // ��������,4���ڴ�WIFI��EOC 7411Lģ��
	@Language(value = "HS200_U4M_SC", cn = "��������EoC")
	HS200_U4M_SC, //
	// ADSL PRODUCTS--------------------------------------------
	@Language(value = "ADSL_BHS_V3", cn = "ADSL_BHS_V3")
	ADSL_BHS_V3, // ����ADSL
	@Language(value = "ADSL_CH210E", cn = "ADSL_CH210E")
	ADSL_CH210E, // ���� ��������� ADSL
	// MOCA PRODUCTS--------------------------------------------------
	@Language(value = "MOCA1_0_NC", cn = "MoCa1.0�ֶ�")
	MOCA1_0_NC, // moca1.0�ֶ�
	@Language(value = "MOCA1_0_CPE", cn = "MoCa1.0�ն�")
	MOCA1_0_CPE, // moca1.0�ն�
	@Language(value = "MOCA1_1_NC", cn = "MoCa1.1�ֶ�")
	MOCA1_1_NC, // moca1.1�ֶ�
	@Language(value = "MOCA1_1_CPE", cn = "MoCa1.1�ն�")
	MOCA1_1_CPE, // moca1.1�ն�
	// ��������Ʒ�ͺ�
	@Language(value = "[IOT]Geomagnetic Vehicle Sensors", cn = "[������]�شų�����")
	IOT_GVS, //
	@Language(value = "[IOT]Bluetooth Locator", cn = "[������]������λ��")
	IOT_BLL, // ������λ��
	@Language(value = "[IOT]Intelligent Meter Reading Terminal", cn = "[������]���ܳ����ն�")
	IOT_IMRT, // ���ܳ����ն�
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
