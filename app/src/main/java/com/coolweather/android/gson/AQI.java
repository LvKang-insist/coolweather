package com.coolweather.android.gson;

/**
 * Created by Lv on 2018/11/6.
 * 定义Gson aqi 实体类      api中 会包含控件的质量情况
 */

public class AQI {

    public AQICity city;
    public class  AQICity{
        public String  aqi ;
        public String pm25;
    }
}
