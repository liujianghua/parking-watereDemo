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

import com.kwantler.model.ParkingWaterDetect;
import com.kwantler.model.SewerCover;

/**
 * iot设备上传数据
 * @author admin
 *
 */
public class IotUpsServiceUtil {

	/**
	 * 
	 */
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 水浸接口
	 */
	private static String SERVICE_ID_PARKING = "parking_water_detect";

	/**
	 * 水浸参数
	 */
	private static String OBJECT_NAME_PARAMETER_PARKING = "water_line";

	/**
	 * 水浸设备状态
	 */
	private static String OBJECT_NAME_STATUS_PARKING = "device_state";

	/**
	 * 水浸设备故障记录
	 */
	private static String OBJECT_NAME_FAULT_PARKING = "fault";

	/**
	 * 水浸设备产品id
	 */
	private static String PRODUCT_ID_PARKING = "1607d4b6099400011607d4b609949201";

	/**
	 * 井盖接口
	 */
	private static String SERVICE_ID_SEWER = "sewer_cover";

	/**
	 * 井盖设备状态
	 */
	private static String OBJECT_NAME_STATUS_SEWER = "sewer_cover_device";

	/**
	 * 井盖参数
	 */
	private static String OBJECT_NAME_PARAMETER_SEWER = "sewer_cover_alert";

	/**
	 * 井盖设备产品id
	 */
	private static String PRODUCT_ID_SEWER = "1607d4b6099400011607d4b60994b001";

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
	 * 发送状态(井盖)
	 * 
	 * @param onlineStatus
	 *            1在线2离线
	 * @param sewerCover
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkStatusSewer(Integer onlineStatus, SewerCover sewerCover) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("id", sewerCover.getDeviceId());
		data.put("online_status", onlineStatus);
		data.put("angle_alert", sewerCover.getAngleAlert());
		data.put("angle_current", sewerCover.getAngleCurrent());
		data.put("device_battery_level", sewerCover.getDeviceBatteryLevel());
		data.put("water_level", sewerCover.getWaterLevel());

		return toBLink(data, SERVICE_ID_SEWER, OBJECT_NAME_STATUS_SEWER, PRODUCT_ID_SEWER,
				XlinkIotPublishOperation.Upsert);

	}

	/**
	 * 发送数据
	 * 
	 * @param sewerCover
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkParamsSewer(SewerCover sewerCover) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();
		data.put("id", sewerCover.getBygId());
		data.put("device_id", sewerCover.getDeviceId());
		data.put("alert_out_waterline", sewerCover.getAlertOutWaterline());
		data.put("alert_angle_slant", sewerCover.getAlertAngleSlant());
		data.put("alert_lowbattery", sewerCover.getAlertLowbattery());

		return toBLink(data, SERVICE_ID_SEWER, OBJECT_NAME_PARAMETER_SEWER, PRODUCT_ID_SEWER,
				XlinkIotPublishOperation.Insert);

	}

	/**
	 * 故障记录
	 * 
	 * @param deviceId
	 * @param deviceFault
	 *            1正常2故障
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkFaultParking(String deviceId, Integer deviceFault) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("device_id", deviceId);
		data.put("device_fault", deviceFault);

		return toBLink(data, SERVICE_ID_PARKING, OBJECT_NAME_FAULT_PARKING, PRODUCT_ID_PARKING,
				XlinkIotPublishOperation.Insert);

	}

	/**
	 * 发送状态
	 * 
	 * @param deviceId
	 * @param onlineStatus
	 *            1在线2离线
	 * @param work_mode
	 *            1不休眠 2定时休眠3深度休眠
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkStatusParking(String deviceId, Integer onlineStatus, Integer workMode)
			throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();

		data.put("id", deviceId);
		data.put("online_status", onlineStatus);
		data.put("work_mode", workMode);

		return toBLink(data, SERVICE_ID_PARKING, OBJECT_NAME_STATUS_PARKING, PRODUCT_ID_PARKING,
				XlinkIotPublishOperation.Upsert);

	}

	/**
	 * 发送数据
	 * 
	 * @param waterDetect
	 * @return
	 * @throws XlinkIotException
	 */
	public static boolean toBLinkParamsParking(ParkingWaterDetect parkingWaterDetect) throws XlinkIotException {

		Map<String, Object> data = new HashMap<>();
		data.put("id", parkingWaterDetect.getBygId());
		data.put("device_id", parkingWaterDetect.getDeviceId());
		data.put("current_water_level", parkingWaterDetect.getCurrentWaterLevel());
		data.put("out_water_level", parkingWaterDetect.getOutWaterLevel());
		data.put("alert_out_water", parkingWaterDetect.getAlertOutWater());

		return toBLink(data, SERVICE_ID_PARKING, OBJECT_NAME_PARAMETER_PARKING, PRODUCT_ID_PARKING,
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

}
