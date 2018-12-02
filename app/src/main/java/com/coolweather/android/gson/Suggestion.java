package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Lv on 2018/11/6.
 * 包含一些天气相关的生活建议。
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport ;

    public class Comfort{
        @SerializedName("txt")
        public String info ;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
