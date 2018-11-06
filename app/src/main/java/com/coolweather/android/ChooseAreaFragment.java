package com.coolweather.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lv on 2018/11/5.
 * 遍历省市县 数据 的碎片
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;

    private TextView titleText;
    private Button backButton;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /* 省列表*/
    private List<Province> provinceList;
    /*市列表*/
    private List<City> cityList;
    /*县列表*/
    private List<County> countyList;

    /*选中的 省份*/
    private Province selectedProvince;
    /*选中的 城市*/
    private City selectedCity;
    /*当前选中的级别 */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);

        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //listview 的点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当级别 等于 省的时候，加载 市
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();  //查询 市级数据
                    // 当级别 等于 市的时候 加载县
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();    // 查询 县级数据
                }
            }
        });
        //返回按钮的点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是县级列表 那么就返回市级列表
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                    //如果是市级列表 就返回省级列表
                } else if (currentLevel == LEVEL_CITY) {
                    //加载 省级数据
                    queryProvinces();
                }
            }
        });
        //第一次 加载 省级 数据
        queryProvinces();
    }

    /*查询全国所有的 省，优先从数据库查询，没有再去服务器查询*/
    private void queryProvinces() {
        //将标题 设置为 中国，将返回按钮 隐藏，因为省级列表已经不能再返回了。
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        //读取省级数据
        provinceList = DataSupport.findAll(Province.class);
        //读取到了 就显示在界面上,
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
            //没有读取到 就组装一个地址，调用queryFromServer()方法 从服务器查询数据
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }
    /*查询选中的 省内所有市 ，优先从数据库开始查询，没有再去服务器查询*/
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        //加载 市的数据，(根据省 的id 查 找市，然后返回到cityList 集合)
        cityList = DataSupport.where("provinceid = ?", String.valueOf(
                selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    //查询 所选中市 中的所有县，数据库中没有 就去服务器上查找
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        //加载县的数据，（根据市的 id ，查找县）
        countyList = DataSupport.where("cityid=?", String.valueOf(selectedCity
                .getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            //拿到市的id
            int provinceCode = selectedProvince.getProvinceCode();
            //拿到县 的id
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" +
                    cityCode;
            queryFromServer(address, "county");
        }
    }

    /*根据 传入的地址  和类型从服务器上 查询 省市县数据*/
    private void queryFromServer(String address, final String type) {
        //显示 对话框，(正在加载)
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.headleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /*显示进度对话框*/
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("正在加载");
            //设置该 对话框是否可以在边界之外取消
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*关闭进度对话框*/
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss(); //取消此对话框
        }
    }
}
