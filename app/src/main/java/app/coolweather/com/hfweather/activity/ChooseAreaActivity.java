package app.coolweather.com.hfweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.coolweather.com.hfweather.R;
import app.coolweather.com.hfweather.db.HFWeatherDB;
import app.coolweather.com.hfweather.model.City;
import app.coolweather.com.hfweather.model.County;
import app.coolweather.com.hfweather.model.Province;
import app.coolweather.com.hfweather.util.HttpCallbackListener;
import app.coolweather.com.hfweather.util.HttpUtil;
import app.coolweather.com.hfweather.util.Utillity;


public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<String>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    private HFWeatherDB hfWeatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView)findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        hfWeatherDB = HFWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();

    }

    private void queryProvinces(){
        provinceList = hfWeatherDB.loadProvinces();
        if(provinceList.size() > 0){
            Log.d("ChooseAreaActivity", "size>0");
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            Log.d("ChooseAreaActivity", "add List");
            adapter.notifyDataSetChanged();//刷新listView\
            Log.d("ChooseAreaActivity", "刷新listView");
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;

        }else {
            queryFromServer("Province");//从服务器查询
        }

    }

    private void queryCities(){
        cityList = hfWeatherDB.loadCities(selectedProvince.getProvinceName());//从数据库读取省份信息
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : this.cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();//刷新listView\
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;

        }else {
            queryFromServer("City");//从服务器查询
        }
    }

    private void queryCounties(){
        countyList = hfWeatherDB.loadCounties(selectedCity.getCityName());//从数据库读取省份信息
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();//刷新listView\
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;

        }else {
            queryFromServer("County");//从服务器查询
        }
    }

    public void queryFromServer(final String type){
        String address = "";
        if(type == "Province"){
            address = "http://wangjie1492.pythonanywhere.com/static/Province.json";
        }else if(type == "City"){
            address = "http://wangjie1492.pythonanywhere.com/static/City.json";
        }else {
            address = "http://wangjie1492.pythonanywhere.com/static/County.json";
        }
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {

                boolean result = false;
                if ("Province".equals(type)){
                    result = Utillity.handleProvincesResponse(hfWeatherDB,response);
                }else if ("City".equals(type)){
                    result = Utillity.handleCitiesResponse(hfWeatherDB, response);
                }else {
                    result = Utillity.handleCountiesResponse(hfWeatherDB,response);
                }


            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed(){
        if (currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
