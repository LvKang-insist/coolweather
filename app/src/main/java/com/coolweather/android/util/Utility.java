package com.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lv on 2018/11/5.
 * 解析 Json 数据
 */

public class Utility {
    /*
    * 解析和处理 服务器返回的 省级数据
    * */
    public static boolean handleProvinceResponse(String response) {
//        TextUtils.isEmpty 如果 字符串结果为 0 ，返回true
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {

                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.optInt("id"));
                    province.setProvinceName(provinceObject.optString("name"));
                    province.save();  // 将数据 存储在 数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    * 解析和 处理服务器 返回的 市级数据
    * */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.optString("name"));
                    city.setCityCode(cityObject.optInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 解析 何处理 服务器 返回的 县级数据
    * */
    public static boolean headleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++) {
                    JSONObject countJson = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setId(countJson.optInt("id"));
                    county.setCountyName(countJson.getString("name"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
