package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Lv on 2018/11/5.
 * 存放 省
 */

public class Province extends DataSupport {
    private int id ;
    private String provinceName;   //省份名称
    private int provinceCode ;         //省份简称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
