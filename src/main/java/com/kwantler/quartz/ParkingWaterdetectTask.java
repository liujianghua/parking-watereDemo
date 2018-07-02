package com.kwantler.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kwantler.model.ParkingWaterDetect;
import com.kwantler.service.IParkingWaterDetectService;
import com.kwantler.util.BGYUtils;
import com.kwantler.util.IotUpsServiceUtil;

import cn.xlink.iot.sdk.exception.XlinkIotException;

/**
 * 水浸设备定时任务
 * 
 * @author admin
 *
 */
@Component
@EnableScheduling
public class ParkingWaterdetectTask {

	private final static Logger log = LoggerFactory.getLogger(ParkingWaterdetectTask.class);

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 */
	private static final String OFF_LINE_TIME = BGYUtils.getLocalConfig("offlineTime",
			"WEB-INF/classes/environment.properties");

	@Autowired
	private IParkingWaterDetectService parkingWaterDetectService;

	/**
	 * 上报数据
	 */
	// @Scheduled(cron = "15 0/1 * * * ?")
	public void params() {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_MONTH, -7);

		Map<String, Object> params = new HashMap<>();

		params.put("status", 2);
		params.put("startTime", SIMPLE_DATE_FORMAT.format(calendar.getTime()));
		List<ParkingWaterDetect> list = parkingWaterDetectService.selectAll(params);

		for (ParkingWaterDetect parkingWaterDetect : list) {

			boolean result = false;

			try {

				result = IotUpsServiceUtil.toBLinkParamsParking(parkingWaterDetect);

			} catch (Exception e) {

				result = false;
				e.printStackTrace();
			}

			if (result) {
				// 上报结果
				parkingWaterDetect.setStatus(1);
				parkingWaterDetectService.updateStatusByPk(parkingWaterDetect);
			}

		}

	}

	/**
	 * 上报状态
	 */
//	@Scheduled(cron = "0 0/2 * * * ?")
	public void status() {

		log.info("-------------------------------------------");

		Calendar calendar = Calendar.getInstance();

		if (OFF_LINE_TIME == null || OFF_LINE_TIME.trim().isEmpty()) {
			calendar.add(Calendar.MINUTE, -40);
		} else {

			calendar.add(Calendar.MINUTE, Integer.parseInt(OFF_LINE_TIME.trim()));

		}
		Date baseDate = calendar.getTime();
		List<ParkingWaterDetect> list = parkingWaterDetectService.selectNewestMonitorTime();
		try {
			for (ParkingWaterDetect parkingWaterDetect : list) {

				String deviceId = parkingWaterDetect.getDeviceId();

				if (baseDate.before(parkingWaterDetect.getMonitorTime())) {
					// 在线
					log.info(deviceId + ":在线");
					IotUpsServiceUtil.toBLinkStatusParking(deviceId, 1, 1);
				} else {
					// 离线
					log.info(deviceId + ":离线");
					IotUpsServiceUtil.toBLinkStatusParking(deviceId, 2, 1);
				}

			}
		} catch (XlinkIotException e) {

			e.printStackTrace();

		}

	}

}
