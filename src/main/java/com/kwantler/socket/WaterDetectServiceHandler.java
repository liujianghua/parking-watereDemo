package com.kwantler.socket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kwantler.model.WaterDetect;
import com.kwantler.service.IWaterDetectService;
import com.kwantler.util.BGYUtils;
import com.kwantler.util.UpsServiceUtil;

import cn.xlink.iot.sdk.exception.XlinkIotException;

public class WaterDetectServiceHandler extends IoHandlerAdapter {

	/**
	 * 时间格式化yyyyMMddHHmmss
	 */
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 设备识别号
	 */
	private final static String MN = "MN";

	/**
	 * 数组分割 =
	 */
	private final static String SPLIT_EQUALS = "=";
	/**
	 * 数组分割 ;
	 */
	private final static String SPLIT_SEMICOLON = ";";
	/**
	 * 数组分割 ,
	 */
	private final static String SPLIT_COMMA = ",";

	/**
	 * CP分割 &&
	 */
	private final static String SPLIT_CP = "&&";

	/**
	 * 监测时间
	 */
	private final static String DATA_TIME = "DataTime=";

	// 打印日志信息
	private final static Logger log = LoggerFactory.getLogger(WaterDetectServiceHandler.class);

	@Autowired
	private IWaterDetectService waterDetectService;

	public Map<Long, IoSessionCache> session_pools = new ConcurrentHashMap<>();

	public ConcurrentMap<String, IoSession> hearttable = new ConcurrentHashMap<String, IoSession>();

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String returnMessage = "";

		IoSessionCache cache = session_pools.get(session.getId());

		String in = message.toString();

		String crcCode = in.substring(in.length() - 6, in.length() - 2);

		in = in.substring(0, in.length() - 6);

		if (!crcCode.equals(BGYUtils.crc16(in.getBytes()))) {

			returnMessage = "CRC校验失败";
			log.info("CRC校验失败");

		} else {

			String head = in.substring(0, in.indexOf(";CP"));
			Map<String, String> headMap = getHeadElement(head);
			// 设备标识:设备在线状态更新
			cache.setDeviceId(headMap.get(MN));

			String data = in.substring(in.indexOf(SPLIT_CP) + 2, in.lastIndexOf(SPLIT_CP));
			Map<String, String> dataMap = getCPElement(data);

			returnMessage = receiveData(headMap, dataMap);

		}

		if (session.isWriteSuspended()) {
			session.resumeWrite();
		}
		WriteFuture future1 = session.write(returnMessage);

		if (future1.isWritten()) {

			log.info("应答完成!");
		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

		log.info("发送到客户端消息：" + message);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		try {

			IoSessionCache cache = new IoSessionCache();
			cache.setIoSession(session);
			session_pools.put(session.getId(), cache);
			log.info("厂商通信网关创建会话成功，资源池信息：" + session_pools.toString());

		} catch (Exception e) {
			log.info("厂商通信网关创建会话失败，请确认网络通信正常!" + e.getMessage());
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

		log.info("客户端" + session_pools.get(session.getId()).getDeviceId() + ":已断开链接");

		UpsServiceUtil.toBLinkStatusWater(session_pools.get(session.getId()).getDeviceId(), 2, 1);

		session_pools.remove(session.getId());
		session.closeOnFlush();

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.closeOnFlush();
	}

	private String receiveData(Map<String, String> headMap, Map<String, String> dataMap) {

		WaterDetect waterDetect = getWaterDetect(dataMap);
		// 设备识别号
		waterDetect.setDeviceId(headMap.get(MN));
		waterDetect.setInsertTime(new Date());
		waterDetect.setBygId(BGYUtils.getUUID());

		// 记录并上报信息
		send(waterDetect);

		// 应答消息
		headMap.put("ST", "91");
		headMap.put("CN", "9014");
		headMap.put("Flag", "4");
		Map<String, String> cpMap = new LinkedHashMap<>();
		headMap.put("CP", SPLIT_CP + mapToString(cpMap, SPLIT_SEMICOLON) + SPLIT_CP);

		String returnMessage = mapToString(headMap, SPLIT_SEMICOLON);

		return returnMessage;

	}

	/**
	 * 消息头部信息 流水号，设备信息等
	 * 
	 * @param data
	 * @return
	 */
	private Map<String, String> getHeadElement(String data) {

		Map<String, String> elementMap = new LinkedHashMap<>();

		String[] strings = data.split(SPLIT_SEMICOLON);

		for (int i = 0; i < strings.length; i++) {

			String item = strings[i];

			stringToMap(elementMap, item);

		}

		return elementMap;

	}

	/**
	 * 监测数据信息
	 * 
	 * @param data
	 * @return
	 */
	private Map<String, String> getCPElement(String data) {

		Map<String, String> elementMap = new HashMap<>();

		String[] strings = data.split(SPLIT_SEMICOLON);

		for (int i = 0; i < strings.length; i++) {

			String item = strings[i];
			// 监测时间
			if (item.startsWith(DATA_TIME)) {

				stringToMap(elementMap, item);

			}

			if (item.startsWith("w")) {

				String[] elements = item.split(SPLIT_COMMA);

				for (int j = 0; j < elements.length; j++) {

					stringToMap(elementMap, elements[j]);

				}

			}
		}

		return elementMap;

	}

	private WaterDetect getWaterDetect(Map<String, String> map) {

		WaterDetect waterDetect = new WaterDetect();

		try {

			waterDetect.setMonitorTime(SIMPLE_DATE_FORMAT.parse(map.get("DataTime")));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// pH 值
		String ph = map.get("w01001-Avg");
		if (ph == null) {
			ph = "0";
		}
		waterDetect.setPh(Float.parseFloat(ph));

		// 水温
		String temperature = map.get("w01010-Avg");
		if (temperature == null) {
			temperature = "0";
		}
		waterDetect.setTemperature(Float.parseFloat(temperature));

		// 悬浮物浊度
		String turbidity = map.get("w01012-Avg");
		if (turbidity == null) {
			turbidity = "0";
		}

		// 氯化物
		String freeChlorine = map.get("w21022-Avg");
		if (freeChlorine == null) {
			freeChlorine = "0";
		}

		waterDetect.setFreeChlorine(Float.parseFloat(freeChlorine));

		waterDetect.setTurbidity(Float.parseFloat(turbidity));

		return waterDetect;

	}

	/**
	 * 数据上报
	 * 
	 * @param waterDetect
	 */
	private void send(WaterDetect waterDetect) {

		boolean flag = false;

		try {

			flag = UpsServiceUtil.toBLinkParamsWater(waterDetect);

		} catch (XlinkIotException e) {

			flag = false;

			e.printStackTrace();
		}

		if (flag) {
			waterDetect.setStatus(1);
		} else {
			waterDetect.setStatus(2);
		}
		//TODO 发送到管理平台 
		
		
		waterDetectService.insert(waterDetect);

	}

	/**
	 * 
	 * @param map
	 * @param s
	 */
	private void stringToMap(Map<String, String> map, String s) {

		String[] strings = s.split(SPLIT_EQUALS);

		if (strings.length > 1) {
			map.put(strings[0], strings[1]);
		}

	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	private static String mapToString(Map<String, String> map, String separator) {

		String[] strings = new String[map.size()];

		int index = 0;

		for (Map.Entry<String, String> entry : map.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();
			strings[index] = key + "=" + value;
			index++;
		}

		return StringUtils.join(strings, separator);

	}

}
