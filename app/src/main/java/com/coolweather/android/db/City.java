package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Lv on 2018/11/5.
 *城市
 */

public class City extends DataSupport {

    private int id ;
    private String cityName ;           //城市名字
    private int cityCode ;      //  城市简称 ，代号
    //所属省份的id 值
    private int provinceId;         //省份Id

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
