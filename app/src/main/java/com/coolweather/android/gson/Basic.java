package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lv on 2018/11/6.
 * 定义Gson Basic实体类      包含城市的基本信息
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}
