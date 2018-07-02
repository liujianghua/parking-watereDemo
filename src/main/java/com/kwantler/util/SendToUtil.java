package com.kwantler.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 发送到管理端
 * 
 * @author
 *
 */
public class SendToUtil {

	private static final String AUTHORIZATION = "0bbf63c0d4c8d7d3792151a4551d5750";
	private static final String APP_ID = "hda0TidqY5";

	private static final String BASE_URL = BGYUtils.getLocalConfig("adminUrl",
			"WEB-INF/classes/environment.properties");

	/**
	 * 
	 *    @param     deviceId    
	 * @param status
	 *            1在线2离线
	 * @return
	 */
	public static boolean sendStatus(String deviceId, int status) {

		String url = BASE_URL + "receive/status";

		JSONObject params = new JSONObject();
		params.put("deviceId", deviceId);
		params.put("status", status);

		try {

			String result = post(url, params, getHeader());

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 
	 * @param deviceType
	 *            数据分类 1 气象2水质3水浸4井盖
	 * @param bean
	 * @return
	 */
	public static boolean sendParams(String deviceType, Object bean) {

		String url = BASE_URL + "receive/params";

		JSONObject params = new JSONObject();
		params.put("type", deviceType);
		params.put("data", bean);

		try {

			String result = post(url, params, getHeader());

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static String post(String path, JSONObject params, Map<String, String> headers) throws IOException {

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
		os.write(params.toString().getBytes());
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

	private static Map<String, String> getHeader() {

		Map<String, String> headers = new HashMap<>();

		headers.put("appId", APP_ID);
		headers.put("Authorization", AUTHORIZATION);

		return headers;

	}

}