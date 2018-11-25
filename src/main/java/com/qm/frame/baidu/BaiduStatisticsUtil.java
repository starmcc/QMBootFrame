package com.qm.frame.baidu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qm.frame.baidu.BaiDuMeasurement;



/**
 * @author 浅梦工作室
 * @createDate 2018年9月23日 上午12:45:59
 * @Description 百度地图工具类
 */
public class BaiduStatisticsUtil {


	private static final Logger LOG = LoggerFactory.getLogger(BaiduStatisticsUtil.class);
	
	public static final String BAIDU_MAP_AK = "F87EQcrxktjHDD2pjOIMRjPPgNKs6RDR";


	
	/**
	 * 根据地址计算出经纬度
	 * 
	 * @param addr
	 * @return Map
	 */
	public static BaiDuMeasurement getLatAndLngByAddress(String address) {
		String lat = "";
		String lng = "";
		try {
			address = java.net.URLEncoder.encode(address, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		StringBuffer url = new StringBuffer();
		url.append("http://api.map.baidu.com/geocoder/v2/?address=");
		url.append(address);
		url.append("&output=json&ak=");
		url.append(BAIDU_MAP_AK);

		try {
			// 进行转码
			URL myURL = new URL(url.toString());
			URLConnection httpsConn = (URLConnection) myURL.openConnection();
			if (httpsConn != null) {
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(insr);
				String data = null;
				if ((data = br.readLine()) != null) {
					lat = data.substring(data.indexOf("\"lat\":") + ("\"lat\":").length(),
							data.indexOf("},\"precise\""));
					lng = data.substring(data.indexOf("\"lng\":") + ("\"lng\":").length(), data.indexOf(",\"lat\""));
				}
				insr.close();
			}
			BaiDuMeasurement measurement = new BaiDuMeasurement();
			measurement.setLatitude(new BigDecimal(lat));
			measurement.setLongitude(new BigDecimal(lng));
			return measurement;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据经纬度计算两点距离(公里)
	 * @param baiDuMeasurement1
	 * @param baiDuMeasurement2
	 * @return
	 */
	private static Integer getDistance(BaiDuMeasurement baiDuMeasurement1,BaiDuMeasurement baiDuMeasurement2) {
		try {
			double distance = 0.0;
			double R = 6378.137; // 地球半径
			double lat1 = Double.parseDouble(baiDuMeasurement1.getLongitude().toString()) * Math.PI / 180.0;
			double lng1 = Double.parseDouble(baiDuMeasurement1.getLatitude().toString()) * Math.PI / 180.0;
			double lat2 = Double.parseDouble(baiDuMeasurement2.getLongitude().toString()) * Math.PI / 180.0;
			double lng2 = Double.parseDouble(baiDuMeasurement2.getLatitude().toString()) * Math.PI / 180.0;

			Double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1))
					* R;
			d = (double) Math.round(d * 100000) / 100000;
			distance += d;
			distance = (double) Math.round(distance * 100000) / 100000;
			return (int)Math.ceil(distance);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据经纬度计算两点距离(公里)
	 * 
	 * @param address1
	 * @param address2
	 * @return
	 */
	public static Integer getSeparation(String address1, String address2) {
		BaiDuMeasurement baiDuMeasurement1 = getLatAndLngByAddress(address1);
		LOG.debug(address1 + "纬度--->" + baiDuMeasurement1.getLongitude());
		LOG.debug(address1 + "经度--->" + baiDuMeasurement1.getLatitude());
		BaiDuMeasurement baiDuMeasurement2 = getLatAndLngByAddress(address2);
		LOG.debug(address2 + "纬度--->" + baiDuMeasurement2.getLongitude());
		LOG.debug(address2 + "经度--->" + baiDuMeasurement2.getLatitude());
		return getDistance(baiDuMeasurement1, baiDuMeasurement2);
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String addr1 = "广州";
		String addr2 = "深圳";
		Integer distance = getSeparation(addr1, addr2);
		LOG.info("123");
		System.out.println(addr1 + "到" + addr2 + "的distance----->" + distance + " KM");

	}

}
