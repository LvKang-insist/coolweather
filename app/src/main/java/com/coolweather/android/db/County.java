package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Lv on 2018/11/5.
 *  县
 */

public class County extends DataSupport {

    private int id ;
    String countyName ;   //县，名称
    private String weatherId ;          //天气id
    int cityId ; //当前县所属的 市的id

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
