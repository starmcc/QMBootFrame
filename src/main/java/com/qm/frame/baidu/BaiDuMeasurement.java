package com.qm.frame.baidu;

import java.math.BigDecimal;

/**
 * @author 浅梦工作室
 * @createDate 2018年9月23日 上午2:21:21
 * @Description 百度地图实体类
*/
public class BaiDuMeasurement {
	
	/**
	 * 经度
	 */
	private BigDecimal longitude;
	/**
	 * 纬度
	 */
	private BigDecimal latitude;

	/**
	 * 经度
	 * @return
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * 纬度
	 * @return
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * 经度
	 * @param longitude
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	/**
	 * 纬度
	 * @param latitude
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

}