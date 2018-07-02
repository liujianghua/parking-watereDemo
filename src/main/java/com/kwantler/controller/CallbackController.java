package com.kwantler.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kwantler.model.ParkingWaterDetect;
import com.kwantler.model.SewerCover;
import com.kwantler.service.IParkingWaterDetectService;
import com.kwantler.service.ISewerCoverService;
import com.kwantler.util.BGYUtils;
import com.kwantler.util.IotUpsServiceUtil;

import cn.xlink.iot.sdk.exception.XlinkIotException;

@Controller
@RequestMapping("/callback")
public class CallbackController {

	/**
	 * 通知类型
	 */
	private static final String NOTIFY_TYPE = "deviceDataChanged";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 设备报警偏移量
	 */
	private static final int OFFSET = 5;

	@Autowired
	private IParkingWaterDetectService parkingWaterDetectService;

	@Autowired
	private ISewerCoverService sewerCoverService;

	/**
	 * 井盖设备接口回调
	 * 
	 * @return
	 */
	@RequestMapping("/sewerCover")
	@ResponseBody
	public Map<String, String> sewerCover(@RequestBody String param) {
		String data = param;

		Map<String, String> ret = new HashMap<>();

		boolean flag = false;

		SewerCover sewerCover = new SewerCover();

		try {

			JSONObject jsonObject = JSONObject.parseObject(data);

			if (NOTIFY_TYPE.equals(jsonObject.getString("notifyType"))) {

				String deviceid = jsonObject.getString("deviceid");

				String eventTime = jsonObject.getString("eventTime").replace("T", "").replace("Z", "");

				String tp_data = jsonObject.getJSONObject("attrs").getString("tp_data");

				sewerCover.setDeviceId(deviceid);
				sewerCover.setBygId(BGYUtils.getUUID());
				sewerCover.setMonitorTime(SDF.parse(eventTime));
				// TODO
				sewerCover.setAlertAngleSlant(2);
				sewerCover.setAlertLowbattery(1);
				sewerCover.setAlertOutWaterline(1);
				sewerCover.setAngleAlert(10);
				sewerCover.setAngleCurrent(5);
				sewerCover.setDeviceBatteryLevel(3000);
				sewerCover.setWaterLevel(400);

				//	在线状态
				IotUpsServiceUtil.toBLinkStatusSewer(1, sewerCover);
				// 设备数据
				flag = IotUpsServiceUtil.toBLinkParamsSewer(sewerCover);
			}

		} catch (XlinkIotException e) {

			e.printStackTrace();
			flag = false;
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {

			if (flag) {

				sewerCover.setStatus(1);
			} else {
				sewerCover.setStatus(2);
			}
			sewerCover.setInsertTime(new Date());
			sewerCoverService.insert(sewerCover);

		}

		return ret;

	}

	/**
	 * 水浸设备接口回调
	 * 
	 * @return
	 */
	@RequestMapping("/parkingWaterDetect")
	@ResponseBody
	public Map<String, String> parkingWaterDetect(@RequestBody String param) {
		String data = param;

		Map<String, String> ret = new HashMap<>();

		boolean flag = false;

		ParkingWaterDetect parkingWaterDetect = new ParkingWaterDetect();

		try {

			JSONObject jsonObject = JSONObject.parseObject(data);

			if (NOTIFY_TYPE.equals(jsonObject.getString("notifyType"))) {

				String deviceid = jsonObject.getString("deviceid");

				String eventTime = jsonObject.getString("eventTime").replace("T", "").replace("Z", "");

				String tp_data = jsonObject.getJSONObject("attrs").getString("tp_data");

				Calendar calendar = Calendar.getInstance();

				calendar.add(Calendar.DAY_OF_MONTH, -3);

				Map<String, Object> params = new HashMap<>();
				params.put("deviceId", deviceid);
				params.put("startTime", SIMPLE_DATE_FORMAT.format(calendar.getTime()));

				ParkingWaterDetect base = parkingWaterDetectService.selectBaseWaterLevel(params);

				int baseWaterLevel = 0;

				if (base == null) {

					baseWaterLevel = Integer.parseInt(tp_data);

				} else {

					baseWaterLevel = base.getCurrentWaterLevel();
				}

				parkingWaterDetect.setDeviceId(deviceid);
				parkingWaterDetect.setBygId(BGYUtils.getUUID());
				parkingWaterDetect.setMonitorTime(SDF.parse(eventTime));

				parkingWaterDetect.setCurrentWaterLevel(Integer.parseInt(tp_data));
				parkingWaterDetect.setOutWaterLevel(baseWaterLevel - parkingWaterDetect.getCurrentWaterLevel());
				if (OFFSET > parkingWaterDetect.getOutWaterLevel().intValue()) {

					parkingWaterDetect.setAlertOutWater(1);

				} else {

					parkingWaterDetect.setAlertOutWater(2);
				}
				// 在线状态
				IotUpsServiceUtil.toBLinkStatusParking(parkingWaterDetect.getDeviceId(), 1, 2);
				// 设备数据
				flag = IotUpsServiceUtil.toBLinkParamsParking(parkingWaterDetect);

			}

		} catch (XlinkIotException e) {

			e.printStackTrace();
			flag = false;
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {

			if (flag) {

				parkingWaterDetect.setStatus(1);
			} else {
				parkingWaterDetect.setStatus(2);
			}
			parkingWaterDetect.setInsertTime(new Date());
			parkingWaterDetectService.insert(parkingWaterDetect);

		}

		return ret;

	}

}
