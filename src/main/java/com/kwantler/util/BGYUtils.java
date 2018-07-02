package com.kwantler.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class BGYUtils {

	public static void main(String[] args) {
		String aString1 = "QN=20160801090000082;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC1;Flag=5;CP=&&DataTime=20180613100000;w01001-Cou=6.0,w01001-Min=5.4,w01001-Avg=7.5,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=25.5,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=8.5,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=20.5,w21022-Max=23.1,w21022-Flag=N&&";

		String aString2 = "QN=20160801090000083;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC1;Flag=5;CP=&&DataTime=20180613110000;w01001-Cou=7.0,w01001-Min=7.4,w01001-Avg=8.5,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=20.5,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=7.5,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=21.5,w21022-Max=23.1,w21022-Flag=N&&";
		String aString3 = "QN=20160801090000084;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC1;Flag=5;CP=&&DataTime=20180613120000;w01001-Cou=7.0,w01001-Min=7.4,w01001-Avg=6.5,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=22.5,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=8.2,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=24.3,w21022-Max=23.1,w21022-Flag=N&&";
		String aString4 = "QN=20160801090000085;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC1;Flag=5;CP=&&DataTime=20180613130000;w01001-Cou=7.0,w01001-Min=7.4,w01001-Avg=7.2,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=25.7,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=8.1,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=24.7,w21022-Max=23.1,w21022-Flag=N&&";

		String aString5 = "QN=20160801090000086;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC2;Flag=5;CP=&&DataTime=20180613100000;w01001-Cou=7.0,w01001-Min=7.4,w01001-Avg=7.1,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=25.9,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=7.8,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=26.5,w21022-Max=23.1,w21022-Flag=N&&";
		String aString6 = "QN=20160801090000087;ST=32;CN=2061;PW=123456;MN=010000A8900016F000169DC2;Flag=5;CP=&&DataTime=20180613110000;w01001-Cou=7.0,w01001-Min=7.4,w01001-Avg=7.9,w01001-Max=7.1,w01001-Flag=N;w01010-Cou=23.0,w01010-Min=24.4,w01010-Avg=27.5,w01010-Max=26.1,w01010-Flag=N;w01012-Cou=5.0,w01012-Min=6.4,w01012-Avg=6.7,w01012-Max=9.1,w01012-Flag=N;w21022-Cou=21.0,w21022-Min=26.4,w21022-Avg=23.8,w21022-Max=23.1,w21022-Flag=N&&";
//		System.out.println(crc16(aString1.getBytes()));
//		System.out.println(crc16(aString2.getBytes()));
//		System.out.println(crc16(aString3.getBytes()));
//		System.out.println(crc16(aString4.getBytes()));
//		System.out.println(crc16(aString5.getBytes()));
//		System.out.println(crc16(aString6.getBytes()));
		System.out.println(getUUID());

		//receiveData(aString3);
	}

	private static String receiveData(String data) {

		Map<String, String> map = new LinkedHashMap<>();

		map.put("地址3", data.substring(8, 14));

		map.put("年", data.substring(16, 18));
		map.put("月", data.substring(18, 20));
		map.put("日", data.substring(20, 22));
		map.put("时", data.substring(22, 24));
		map.put("分", data.substring(24, 26));
		map.put("秒", data.substring(26, 28));

		map.put("湿度", data.substring(28, 32));
		map.put("温度", data.substring(32, 36));
		map.put("照度", data.substring(52, 56));
		map.put("风向", data.substring(56, 60));
		map.put("风速", data.substring(60, 64));
		map.put("雨量", data.substring(64, 68));

		map.put("主板电压", data.substring(88, 90));
		map.put("主板温度", data.substring(90, 92));
		map.put("气压", data.substring(118, 122));
		map.put("PM2.5", data.substring(122, 126));
		map.put("PM10", data.substring(126, 130));

		for (Map.Entry<String, String> entry : map.entrySet()) {

			System.out.println(entry.getKey() + "=" + Integer.parseInt(entry.getValue(), 16));

		}

		return null;

	}

	public static String crc16(byte[] data) {

		int crc_reg = 0xFFFF;
		int check;

		for (int i = 0; i < data.length; i++) {

			crc_reg = (crc_reg >> 8) ^ data[i];

			for (int j = 0; j < 8; j++) {

				check = crc_reg & 0x0001;

				crc_reg >>= 1;

				if (check == 0x0001) {
					crc_reg ^= 0xA001;
				}

			}

		}
		String ret = Integer.toHexString(crc_reg).toUpperCase();

		if (ret.length() < 4) {
			ret = "0" + ret;
		}

		return ret;
	}

	/**
	 * 加载本地配置文件
	 * 
	 * @param propertiesName
	 *            属性名称
	 * @param fileRootPath
	 *            本地配置文件路径（webapp以下路径）
	 * @return
	 */
	public static String getLocalConfig(String propertiesName, String fileRootPath) {
		String realPath = System.getProperty("parking-water-sdk");
		Properties prop = new Properties();
		try {
			// 读取属性文件
			InputStream in = new BufferedInputStream(new FileInputStream(realPath + fileRootPath));
			prop.load(in); /// 加载属性列表
			String filePath = prop.getProperty(propertiesName);
			in.close();
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 定长字符，不足前面补0
	 * 
	 * @param v
	 * @param length
	 * @return
	 */
	public static String prefixZero(Integer v, int length) {

		String src = String.valueOf(v);

		String ret = src;

		for (int i = length - src.length(); i > 0; i--) {

			ret = "0" + ret;
		}

		return ret;
	}
	
	
	public static String getUUID(){
		
        return UUID.randomUUID().toString().replace("-", "");  
		
	}
	
}
