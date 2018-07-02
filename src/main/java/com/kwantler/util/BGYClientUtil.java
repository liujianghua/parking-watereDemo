package com.kwantler.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author
 *
 */
public class BGYClientUtil {
	//
	public static final String IP = "http://iotadmintest.bgycc.com";

	private static final String AUTHORIZATION = "0bbf63c0d4c8d7d3792151a4551d5750";

	private static final String APP_ID = "3207d4b60990b200";

	private static final String SECRET = "7e66adad93beb16e2d7b61a9ea017058";

	private static final String PRODUCT_KEY = "C7xZVPGdy2";

	private static final String DEVICE_ID = "31f925acb673487796f9c47a28dd7df8";

	public static void main(String[] args) throws Exception {

		 login();

//		// add();
//
//		// queryDevice();
//
//		//queryState();
//
//		// queryAllDevice();
//		// getLocation();
//		// commands();
		 deviceDataHistory();
	}

	public static void login() throws IOException {

		Map<String, String> params = new HashMap<>();

		params.put("appId", APP_ID);
		params.put("secret", SECRET);

		String url = IP + "/v2/accesskey_auth";

		System.out.println(post(url, params, null));

	}

	public static void add() throws IOException {

		Map<String, String> params = new HashMap<>();
		// 产品ID
		params.put("product_key", PRODUCT_KEY);
		// 产品序列号
		params.put("sn", "058201801000414");
		// 产品序列号
		params.put("imei", "356566077982841");
		// 产品序列号
		params.put("iccid", "89860317452041015290");
		// 产品序列号
		params.put("imsi", "460111174599274");

		String url = IP + "/iocm/devices/reg";

		System.out.println(post(url, params, getHeader()));

	}

	public static void queryDevice() throws IOException {

		String url = IP + "/iocm/devices/queryDevice?deviceId=" + DEVICE_ID;

		System.out.println(get(url, getHeader()));

	}

	public static void queryState() throws IOException {

		String url = IP + "/iocm/devices/queryState?deviceId=" + DEVICE_ID;

		System.out.println(get(url, getHeader()));

	}

	public static void queryAllDevice() throws IOException {

		String url = IP + "/iocm/devices/queryAllDevice?pageNo=1&pageSize=10";

		System.out.println(get(url, getHeader()));

	}

	/**
	 * 给定物联网卡的iccid卡号或者接入号码accnumber查询设备的经纬度。
	 * 
	 * @throws IOException
	 */
	public static void getLocation() throws IOException {

		String url = IP + "/iocm/devices/getLocation?iccid=89860317452041015290";

		System.out.println(get(url, getHeader()));

	}

	/**
	 * 给定物联网卡的iccid卡号或者接入号码accnumber查询设备的经纬度。
	 * 
	 * @throws IOException
	 */
	public static void commands() throws IOException {

		String url = IP + "/iocm/devices/" + DEVICE_ID + "/commands";

		Map<String, String> params = new HashMap<>();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tp_data", "344345");

		// 产品ID
		params.put("attrs", jsonObject.toString());
		params.put("command", "upd");

		System.out.println(post(url, params, getHeader()));

	}

	/**
	 * 
	 * 
	 * @throws IOException
	 */
	public static void deviceDataHistory() throws IOException {

		String url = IP + "/iocm/data/deviceDataHistory?deviceId=" + DEVICE_ID + "&pageNo=1&pageSize=10";

		System.out.println(get(url, getHeader()));

	}

	public static String post(String path, Map<String, String> params, Map<String, String> headers) throws IOException {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod(RequestMethod.POST.name()); // 设置本次请求的方式 ，
															// 默认是GET方式，
															// 参数要求都是大写字母
		conn.setConnectTimeout(5000);// 设置连接超时
		conn.setDoInput(true);// 是否打开输入流 ， 此方法默认为true
		conn.setDoOutput(true);// 是否打开输出流， 此方法默认为false

		if (headers != null && !headers.isEmpty()) {

			for (Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}

		}

		OutputStream os = conn.getOutputStream();
		String postParams = mapToParamsString(params);
		os.write(postParams.getBytes());
		os.flush();

		// 开始获取数据
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			bos.write(arr, 0, len);
			bos.flush();
		}
		bos.close();
		return bos.toString("utf-8");

	}

	/**
	 * 
	 * @param path
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String get(String path, Map<String, String> headers) throws IOException {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod(RequestMethod.GET.name()); // 设置本次请求的方式 ，默认是GET方式，
		conn.setConnectTimeout(5000);// 设置连接超时
		if (headers != null && !headers.isEmpty()) {

			for (Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}

		}

		conn.connect();

		// 开始获取数据
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			bos.write(arr, 0, len);
			bos.flush();
		}
		bos.close();
		return bos.toString("utf-8");

	}

	/**
	 * 请求参数
	 * 
	 * @param params
	 * @return appId=xxx&secret=xxxxxxx
	 */
	private static String mapToParamsString(Map<String, String> params) {

		if (params == null || params.isEmpty()) {
			return null;
		}

		String[] strings = new String[params.size()];

		int index = 0;

		for (Map.Entry<String, String> entry : params.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();
			strings[index] = key + "=" + value;
			index++;
		}

		return StringUtils.join(strings, "&");
	}

	private static Map<String, String> getHeader() {

		Map<String, String> headers = new HashMap<>();

		headers.put("appId", APP_ID);
		headers.put("Authorization", AUTHORIZATION);

		return headers;

	}

}