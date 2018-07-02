package com.kwantler.util;

import cn.xlink.iot.sdk.XlinkIot;
import cn.xlink.iot.sdk.XlinkIotBuilder;
import cn.xlink.iot.sdk.datastruct.XlinkIotPublishRsp;
import cn.xlink.iot.sdk.datastruct.XlinkIotPublishModel;
import cn.xlink.iot.sdk.exception.XlinkIotException;
import cn.xlink.iot.sdk.operator.XlinkIotPublish;
import cn.xlink.iot.sdk.type.XlinkIotPublishOperation;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.kwantler.model.WaterDetect;
import com.kwantler.model.WeatherStation;

/**
 * tcp设备上传数据
 * @author admin
 *
 */
public class UpsServiceUtil {

	/**
	 * 
	 */
	// private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");

	/**
	 * 水质接口
	 */
	private static String SERVICE_ID_WATER = "water_detect";

	/**
	 * 水质参数
	 */
	private static String OBJECT_NAME_PARAMETER_WATER = "water_parameter";

	/**
	 * 水质设备状态
	 */
	private static String OBJECT_NAME_STATUS_WATER = "device_status";

	/**
	 * 水质设备产品id
	 */
	private static String PRODUCT_ID_WATER = "1607d4b6099400011607d4b609946c01";

	/**
	 * 气象接口
	 */
	private static String SERVICE_ID_WEATHER = "weather_station";

	/**
	 * 气象参数
	 */
	private static String OBJECT_NAME_PARAMETER_WEATHER = "collector";

	/**
	 * 气象设备状态
	 */
	private static String OBJECT_NAME_STATUS_WEATHER = "station";

	/**
	 * 传感器状态
	 */
	private static String OBJECT_NAME_SENSOR_WEATHER = "sensor";

	/**
	 * 警告状态
	 */
	private static String OBJECT_NAME_ALERT_WEATHER = "alert";

	/**
	 * 气象设备产品id
	 */
	private static String PRODUCT_ID_WEATHER = "1607d4b6099300011607d4b609934a01";

	/**
	 * 
	 */
	private static final String APP_ID = BGYUtils.getLocalConfig("appId", "WEB-INF/classes/environment.properties");

	/**
	 * 
	 */
	private static final String APP_SECRET = BGYUtils.getLocalConfig("appSecret",
			"WEB-INF/classes/environment.properties");

	/**
	 * 
	 */
	private static final String END_POINT = BGYUtils.getLocalConfig("endpoint",
			"WEB-INF/classes/environment.properties");

	/**
	 * 故障警告状态(气象站)
	 * 
	 * @param deviceId
	 * @param alertStatus 1正常2故障
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkAlertWeather(String deviceId, Integer alertStatus) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("device_id", deviceId);
		data.put("alert_status", alertStatus);

		return toBLink(data, SERVICE_ID_WEATHER, OBJECT_NAME_ALERT_WEATHER, PRODUCT_ID_WEATHER,
				XlinkIotPublishOperation.Insert);

	}

	/**
	 * 传感器状态(气象站)
	 * 
	 * @param deviceId
	 * @param detectStatus 0正常
	 * @param signalStrength
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkSensorWeather(String deviceId, Integer detectStatus, Integer signalStrength)
			throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("id", deviceId);
		data.put("detect_status", detectStatus);
		data.put("signal_strength", signalStrength);

		return toBLink(data, SERVICE_ID_WEATHER, OBJECT_NAME_SENSOR_WEATHER, PRODUCT_ID_WEATHER,
				XlinkIotPublishOperation.Upsert);

	}

	/**
	 * 发送状态(气象站)
	 * 
	 * @param deviceId
	 * @param onlineStatus
	 * @param stationStatus
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkStatusWeather(String deviceId, Integer onlineStatus, Integer stationStatus)
			throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("id", deviceId);
		data.put("online_status", onlineStatus);
		data.put("station_status", stationStatus);

		return toBLink(data, SERVICE_ID_WEATHER, OBJECT_NAME_STATUS_WEATHER, PRODUCT_ID_WEATHER,
				XlinkIotPublishOperation.Upsert);

	}

	/**
	 * 发送数据(气象站)
	 * 
	 * @param weatherStation
	 * @param productId
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkParamsWeather(WeatherStation weatherStation) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();
		data.put("id", weatherStation.getBygId());
		data.put("device_id", weatherStation.getDeviceId());
		data.put("current_temp", weatherStation.getCurrentTemp());

		data.put("top_temp", weatherStation.getTopTemp());

		data.put("top_temp_time", SIMPLE_DATE_FORMAT.format(weatherStation.getTopTempTime()));

		data.put("lowest_temp", weatherStation.getLowestTemp());

		data.put("lowest_temp_time", SIMPLE_DATE_FORMAT.format(weatherStation.getLowestTempTime()));

		data.put("current_humidity", weatherStation.getCurrentHumidity());

		data.put("top_humidity", weatherStation.getTopHumidity());

		data.put("top_humidity_time", SIMPLE_DATE_FORMAT.format(weatherStation.getTopHumidityTime()));

		data.put("lowest_humidity", weatherStation.getLowestHumidity());

		data.put("lowest_humidity_time", SIMPLE_DATE_FORMAT.format(weatherStation.getLowestHumidityTime()));

		data.put("current_air_pressure", weatherStation.getCurrentAirPressure());

		data.put("instantaneous_wind_speed", weatherStation.getInstantaneousWindSpeed());

		data.put("instantaneous_wind_direction", weatherStation.getInstantaneousWindDirection());

		data.put("current_rainfall_min", replayNull(weatherStation.getCurrentRainfallMin()));

		data.put("current_rainfall_hour", weatherStation.getCurrentRainfallHour());

		data.put("battery_level", weatherStation.getBatteryLevel());

		// data.put("monitor_time",
		// SIMPLE_DATE_FORMAT.format(weatherStation.getMonitorTime()));

		return toBLink(data, SERVICE_ID_WEATHER, OBJECT_NAME_PARAMETER_WEATHER, PRODUCT_ID_WEATHER,
				XlinkIotPublishOperation.Insert);

	}

	/**
	 * 发送状态
	 * 
	 * @param deviceId
	 * @param onlineStatus
	 * @param faultState
	 * @param productId
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkStatusWater(String deviceId, Integer onlineStatus, Integer faultState)
			throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("id", deviceId);
		data.put("online_status", onlineStatus);
		data.put("fault_state", faultState);

		return toBLink(data, SERVICE_ID_WATER, OBJECT_NAME_STATUS_WATER, PRODUCT_ID_WATER,
				XlinkIotPublishOperation.Upsert);

	}

	/**
	 * 发送数据
	 * 
	 * @param waterDetect
	 * @param productId
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkParamsWater(WaterDetect waterDetect) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();
		data.put("id", waterDetect.getBygId());
		data.put("device_id", waterDetect.getDeviceId());
		data.put("free_chlorine", waterDetect.getFreeChlorine());
		data.put("ph", waterDetect.getPh());
		data.put("temperature", waterDetect.getTemperature());
		data.put("turbidity", waterDetect.getTurbidity());
		// data.put("monitor_time",
		// SIMPLE_DATE_FORMAT.format(waterDetect.getMonitorTime()));

		return toBLink(data, SERVICE_ID_WATER, OBJECT_NAME_PARAMETER_WATER, PRODUCT_ID_WATER,
				XlinkIotPublishOperation.Insert);

	}

	public static boolean toBLink(Map<String, Object> data, String serviceId, String objectName, String productId,
			XlinkIotPublishOperation operation) throws XlinkIotException {

		// 创建一个客户端构造器
		XlinkIotBuilder builder = new XlinkIotBuilder();
		// 设置凭证和地址
		builder.setAppId(APP_ID).setAppSecret(APP_SECRET).setEndPoint(END_POINT);

		System.out.println("serviceId=" + serviceId);
		System.out.println("objectName=" + objectName);
		System.out.println("productId=" + productId);
		System.out.println("appId=" + APP_ID);
		System.out.println("appSecret=" + APP_SECRET);
		System.out.println("endpoint=" + END_POINT);

		// 创建一个http连接的客户端
		XlinkIot xlinkIot = builder.buildXlinkIotHttpClient();

		// 1. 创建一个publish实例
		XlinkIotPublish xlinkIotPublish = new XlinkIotPublish(xlinkIot);
		// 2. 构建publish数据
		XlinkIotPublishModel publishModel = createUpsModel(data, serviceId, objectName, productId, operation);

		XlinkIotPublishRsp publishResponse = xlinkIotPublish.publishToXlinkIot(publishModel);

		System.out.println(publishResponse.isSuccess());
		System.out.println(publishResponse.getContent());

		return publishResponse.isSuccess();

	}

	private static XlinkIotPublishModel createUpsModel(Map<String, Object> data, String serviceId, String objectName,
			String productId, XlinkIotPublishOperation operation) {
		XlinkIotPublishModel publishModel = new XlinkIotPublishModel();
		publishModel.setServiceId(serviceId);
		publishModel.setObjectName(objectName);
		publishModel.setOperation(operation);
		// 可选，设置产品ID，由服务端提供，用于关联物联平台的相关信息
		publishModel.setProductId(productId);

		publishModel.setData(data);
		return publishModel;
	}

	private static Float replayNull(Float f) {

		if (f == null) {
			return 0f;
		}
		return f;
	}

}
