package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lv on 2018/11/6.
 * 包含天气信息
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
