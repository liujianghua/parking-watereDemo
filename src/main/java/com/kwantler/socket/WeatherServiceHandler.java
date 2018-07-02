package com.kwantler.socket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kwantler.model.WeatherStation;
import com.kwantler.service.IWeatherStationService;
import com.kwantler.util.BGYUtils;
import com.kwantler.util.UpsServiceUtil;

import cn.xlink.iot.sdk.exception.XlinkIotException;

public class WeatherServiceHandler extends IoHandlerAdapter {

	/**
	 * 时间格式化yyyyMMddHHmmss
	 */
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	// 打印日志信息
	private final static Logger log = LoggerFactory.getLogger(WeatherServiceHandler.class);

	@Autowired
	private IWeatherStationService weatherStationService;

	public Map<Long, IoSessionCache> session_pools = new ConcurrentHashMap<>();

	public ConcurrentMap<String, IoSession> hearttable = new ConcurrentHashMap<String, IoSession>();

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		IoSessionCache cache = session_pools.get(session.getId());

		String in = message.toString();

		WeatherStation weatherStation = receiveData(in);

		cache.setDeviceId(weatherStation.getDeviceId());



		if (session.isWriteSuspended()) {
			session.resumeWrite();
		}
		WriteFuture future1 = session.write("OK");

		if (future1.isWritten()) {

			log.info("应答完成!");
		}
		
		send(weatherStation);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

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

		String deviceId = session_pools.get(session.getId()).getDeviceId();

		if (deviceId != null) {
			
			UpsServiceUtil.toBLinkStatusWeather(deviceId, 2, 2);
		}

		session_pools.remove(session.getId());

		session.closeOnFlush();

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.closeOnFlush();
	}

	private WeatherStation receiveData(String data) {

		Map<String, String> map = new LinkedHashMap<>();
		WeatherStation weatherStation = new WeatherStation();

		map.put("设备识别", data.substring(8, 14));
		weatherStation.setDeviceId(data.substring(8, 14));

		String monitorTime = "";

		map.put("年", data.substring(16, 18));
		monitorTime += "20" + Integer.parseInt(data.substring(16, 18), 16);
		map.put("月", data.substring(18, 20));
		monitorTime += prefixZero(data.substring(18, 20));
		map.put("日", data.substring(20, 22));
		monitorTime += prefixZero(data.substring(20, 22));
		map.put("时", data.substring(22, 24));
		monitorTime += prefixZero(data.substring(22, 24));
		map.put("分", data.substring(24, 26));
		monitorTime += prefixZero(data.substring(24, 26));
		map.put("秒", data.substring(26, 28));
		monitorTime += prefixZero(data.substring(26, 28));
		try {
			weatherStation.setMonitorTime(SIMPLE_DATE_FORMAT.parse(monitorTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		map.put("湿度", data.substring(28, 32));
		weatherStation.setCurrentHumidity(toFloat(data.substring(28, 32)));
		map.put("温度", data.substring(32, 36));
		weatherStation.setCurrentTemp(toFloat(data.substring(32, 36)));
		map.put("照度", data.substring(52, 56));
		map.put("风向", data.substring(56, 60));
		weatherStation.setInstantaneousWindDirection(Integer.parseInt(data.substring(56, 60), 16));
		map.put("风速", data.substring(60, 64));
		weatherStation.setInstantaneousWindSpeed(toFloat(data.substring(60, 64)));
		map.put("雨量", data.substring(64, 68));
		weatherStation.setCurrentRainfallHour(toFloat(data.substring(64, 68)));

		map.put("主板电压", data.substring(88, 90));
		weatherStation.setBatteryLevel(toFloat(data.substring(88, 90)));

		map.put("主板温度", data.substring(90, 92));
		map.put("气压", data.substring(118, 122));
		weatherStation.setCurrentAirPressure(toFloat(data.substring(118, 122)));
		map.put("PM2.5", data.substring(122, 126));
		map.put("PM10", data.substring(126, 130));

		for (Map.Entry<String, String> entry : map.entrySet()) {

			System.out.println(entry.getKey() + "=" + Integer.parseInt(entry.getValue(), 16));

		}
		weatherStation.setInsertTime(new Date());
		weatherStation.setBygId(BGYUtils.getUUID());
		return weatherStation;
	}

	private void send(WeatherStation weatherStation) {
		// 设置最大最小值
		setMaxMinValue(weatherStation);

		boolean flag = false;

		try {

			flag = UpsServiceUtil.toBLinkParamsWeather(weatherStation);

		} catch (XlinkIotException e) {

			flag = false;

			e.printStackTrace();
		}
		

		if (flag) {
			
			weatherStation.setStatus(1);
		} else {
			
			weatherStation.setStatus(2);
		}
		
		//TODO 发送到管理平台
		
		weatherStationService.insert(weatherStation);

	}

	/**
	 * 16进制转Float
	 * 
	 * @param s
	 * @return
	 */
	private Float toFloat(String s) {

		int hex = Integer.parseInt(s, 16);

		return Float.parseFloat(hex + "");

	}

	/**
	 * 16进制转int后不够两位前补0
	 * 
	 * @param s
	 * @return
	 */
	private String prefixZero(String s) {

		int hex = Integer.parseInt(s, 16);

		return BGYUtils.prefixZero(hex, 2);

	}

	/**
	 * 设置最大最小值
	 * 
	 * @param weatherStation
	 */
	private void setMaxMinValue(WeatherStation weatherStation) {

		WeatherStation last = weatherStationService.selectNewestByDeviceId(weatherStation.getDeviceId());
		// 今天零时
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		if (last == null || last.getMonitorTime().before(calendar.getTime())) {
			// 最高温
			weatherStation.setTopTemp(weatherStation.getCurrentTemp());
			weatherStation.setTopTempTime(weatherStation.getMonitorTime());
			// 最低温
			weatherStation.setLowestTemp(weatherStation.getCurrentTemp());
			weatherStation.setLowestTempTime(weatherStation.getMonitorTime());
			// 最大湿度
			weatherStation.setTopHumidity(weatherStation.getCurrentHumidity());
			weatherStation.setTopHumidityTime(weatherStation.getMonitorTime());
			// 最小湿度
			weatherStation.setLowestHumidity(weatherStation.getCurrentHumidity());
			weatherStation.setLowestHumidityTime(weatherStation.getMonitorTime());
		} else {

			// 最高温
			if (weatherStation.getCurrentTemp() > last.getTopTemp()) {
				weatherStation.setTopTemp(weatherStation.getCurrentTemp());
				weatherStation.setTopTempTime(weatherStation.getMonitorTime());
			} else {
				weatherStation.setTopTemp(last.getTopTemp());
				weatherStation.setTopTempTime(last.getTopTempTime());

			}

			// 最低温
			if (weatherStation.getCurrentTemp() < last.getLowestTemp()) {
				weatherStation.setLowestTemp(weatherStation.getCurrentTemp());
				weatherStation.setLowestTempTime(weatherStation.getMonitorTime());
			} else {
				weatherStation.setLowestTemp(last.getLowestTemp());
				weatherStation.setLowestTempTime(last.getLowestTempTime());

			}

			// 最大湿度
			if (weatherStation.getCurrentHumidity() > last.getTopHumidity()) {
				weatherStation.setTopHumidity(weatherStation.getCurrentHumidity());
				weatherStation.setTopHumidityTime(weatherStation.getMonitorTime());
			} else {
				weatherStation.setTopHumidity(last.getTopHumidity());
				weatherStation.setTopHumidityTime(last.getTopHumidityTime());

			}

			// 最小湿度
			if (weatherStation.getCurrentHumidity() < last.getLowestHumidity()) {
				weatherStation.setLowestHumidity(weatherStation.getCurrentHumidity());
				weatherStation.setLowestHumidityTime(weatherStation.getMonitorTime());
			} else {
				weatherStation.setLowestHumidity(last.getLowestHumidity());
				weatherStation.setLowestHumidityTime(last.getLowestHumidityTime());

			}

		}

	}

}
