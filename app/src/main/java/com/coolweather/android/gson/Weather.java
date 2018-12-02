package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lv on 2018/11/6.
 * 总的实例类，来引用 创建好的各个实体类
 */

public class Weather {
    public String status;  //请求状态  ok表示成功
    public Basic basic;     //包含城市的基本信息
    public AQI aqi;         // 会包含控件的质量情况
    public Now now;         //包含天气信息
    public  Suggestion suggestion; //包含一些天气相关的生活建议。

    @SerializedName("daily_forecast")
    public List<Forecast> forecastsList;  //包含未来几天天气的信息
}
