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

}
