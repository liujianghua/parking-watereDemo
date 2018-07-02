package com.kwantler.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kwantler.model.WaterDetect;
import com.kwantler.model.WeatherStation;
import com.kwantler.service.IWaterDetectService;
import com.kwantler.service.IWeatherStationService;
import com.kwantler.socket.IoSessionCache;
import com.kwantler.socket.WaterDetectServiceHandler;
import com.kwantler.socket.WeatherServiceHandler;
import com.kwantler.util.UpsServiceUtil;

import cn.xlink.iot.sdk.exception.XlinkIotException;

/**
 * 气象站,水质监测定时任务
 * 
 * @author admin
 *
 */
@Component
@EnableScheduling
public class WaterAndWeatherTask {

	private final static Logger log = LoggerFactory.getLogger(WaterAndWeatherTask.class);

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static char[] CH = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	@Autowired
	IWeatherStationService weatherStationService;

	@Autowired
	IWaterDetectService waterDetectService;

	@Autowired
	private IoAcceptor ioAcceptor;

	/**
	 * 气象设备对时
	 */
	@Scheduled(cron = "0 0 4 0/1 * ?")
	public void setTime() {

		IoHandler ioHandler = ioAcceptor.getHandler();

		if (ioHandler instanceof WeatherServiceHandler) {

			WeatherServiceHandler handler = (WeatherServiceHandler) ioAcceptor.getHandler();

			for (Iterator<Map.Entry<Long, IoSessionCache>> it = handler.session_pools.entrySet().iterator(); it
					.hasNext();) {
				Map.Entry<Long, IoSessionCache> item = it.next();
				IoSessionCache ioSessionCache = item.getValue();

				if (ioSessionCache.getIoSession().isConnected()) {
					push(ioSessionCache.getIoSession());
				}

			}

		}

	}

	/**
	 * 监测数据上报
	 */

	@Scheduled(cron = "0 0/30 * * * ?")
	public void params() {

		IoHandler ioHandler = ioAcceptor.getHandler();

		if (ioHandler instanceof WaterDetectServiceHandler) {

			paramsWater();
		} else {

			paramsWeather();

		}

	}

	/**
	 * 设备状态上报
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void status() {

		IoHandler ioHandler = ioAcceptor.getHandler();

		if (ioHandler instanceof WaterDetectServiceHandler) {

			statusWater();

		} else {
			statusWeather();

		}

	}

	/**
	 * 水质设备上报数据
	 */
	public void paramsWater() {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_MONTH, -7);

		Map<String, Object> params = new HashMap<>();

		params.put("status", 2);
		params.put("startTime", SIMPLE_DATE_FORMAT.format(calendar.getTime()));
		List<WaterDetect> list = waterDetectService.selectAll(params);

		for (WaterDetect waterDetect : list) {

			boolean result = false;

			try {

				result = UpsServiceUtil.toBLinkParamsWater(waterDetect);

			} catch (Exception e) {

				result = false;
				e.printStackTrace();
			}

			if (result) {
				// 上报成功
				waterDetect.setStatus(1);
				waterDetectService.updateStatusByPk(waterDetect);
			}

		}

	}

	/**
	 * 水质设备上报状态
	 */
	public void statusWater() {

		log.info("-------------------------------------------");

		WaterDetectServiceHandler handler = (WaterDetectServiceHandler) ioAcceptor.getHandler();

		for (Iterator<Map.Entry<Long, IoSessionCache>> it = handler.session_pools.entrySet().iterator(); it
				.hasNext();) {
			Map.Entry<Long, IoSessionCache> item = it.next();
			IoSessionCache ioSessionCache = item.getValue();

			if (ioSessionCache.getDeviceId() == null) {

				continue;

			}
			try {

				if (!ioSessionCache.getIoSession().isConnected()) {

					log.info("水质设备" + ioSessionCache.getDeviceId() + ":离线");

					UpsServiceUtil.toBLinkStatusWater(ioSessionCache.getDeviceId(), 2, 1);

					it.remove();
				} else {

					log.info("水质设备" + ioSessionCache.getDeviceId() + ":在线");

					UpsServiceUtil.toBLinkStatusWater(ioSessionCache.getDeviceId(), 1, 1);
				}

			} catch (XlinkIotException e) {

				e.printStackTrace();
			}

		}

	}

	/**
	 * 气象设备上报数据
	 */
	public void paramsWeather() {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_MONTH, -7);

		Map<String, Object> params = new HashMap<>();

		params.put("status", 2);
		params.put("startTime", SIMPLE_DATE_FORMAT.format(calendar.getTime()));
		List<WeatherStation> list = weatherStationService.selectAll(params);

		for (WeatherStation weatherStation : list) {

			boolean result = false;

			try {

				result = UpsServiceUtil.toBLinkParamsWeather(weatherStation);

			} catch (Exception e) {

				result = false;
				e.printStackTrace();
			}

			if (result) {
				// 上报结果
				weatherStation.setStatus(1);
				weatherStationService.updateStatusByPk(weatherStation);
			}

		}

	}

	/**
	 * 气象设备上报状态
	 */
	public void statusWeather() {

		log.info("-------------------------------------------");

		WeatherServiceHandler handler = (WeatherServiceHandler) ioAcceptor.getHandler();

		for (Iterator<Map.Entry<Long, IoSessionCache>> it = handler.session_pools.entrySet().iterator(); it
				.hasNext();) {
			Map.Entry<Long, IoSessionCache> item = it.next();
			IoSessionCache ioSessionCache = item.getValue();
			if (ioSessionCache.getDeviceId() == null) {

				continue;

			}

			try {

				if (!ioSessionCache.getIoSession().isConnected()) {

					log.info("气象设备" + ioSessionCache.getDeviceId() + ":离线");

					UpsServiceUtil.toBLinkStatusWeather(ioSessionCache.getDeviceId(), 2, 2);

					it.remove();
				} else {

					log.info("气象设备" + ioSessionCache.getDeviceId() + ":在线");

					UpsServiceUtil.toBLinkStatusWeather(ioSessionCache.getDeviceId(), 1, 1);
					// 其他状态
					otherStatus(ioSessionCache.getDeviceId());
				}

			} catch (XlinkIotException e) {

				e.printStackTrace();
			}

		}

	}

	/**
	 * 故障和传感器
	 * 
	 * @param deviceId
	 * @throws XlinkIotException
	 */
	private void otherStatus(String deviceId) throws XlinkIotException {

		UpsServiceUtil.toBLinkAlertWeather(deviceId, 1);
		UpsServiceUtil.toBLinkSensorWeather(deviceId, 0, 0);

	}

	/**
	 * 气象设备时间校准
	 * 
	 * @param session
	 */
	private void push(IoSession session) {

		String time = "";

		Calendar calendar = Calendar.getInstance();
		time += toHex(calendar.get(Calendar.DAY_OF_MONTH));
		time += toHex(calendar.get(Calendar.HOUR_OF_DAY));
		time += toHex(calendar.get(Calendar.MINUTE));
		time += toHex(calendar.get(Calendar.SECOND));

		if (session.isWriteSuspended()) {
			session.resumeWrite();
		}
		WriteFuture future1 = session.write(time);

		if (future1.isWritten()) {

		}

	}

	/**
	 * 十进制转16进制
	 * 
	 * @param num
	 * @return
	 */
	public static String toHex(int num) {

		if (num == 0)
			return "00";
		String result = "";
		while (num != 0) {
			int x = num & 0xF;
			result = CH[(x)] + result;
			num = (num >>> 4);
		}

		if (result.length() % 2 != 0) {

			result = "0" + result;
		}
		return result;

	}

}
