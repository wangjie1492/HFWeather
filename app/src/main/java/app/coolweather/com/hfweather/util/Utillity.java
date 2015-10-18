package app.coolweather.com.hfweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.coolweather.com.hfweather.db.HFWeatherDB;
import app.coolweather.com.hfweather.model.City;
import app.coolweather.com.hfweather.model.County;
import app.coolweather.com.hfweather.model.Province;


/**
 * Created by Jay on 2015/10/8.
 */
public class Utillity {


    public static void saveWeatherInfo(Context context,String cityName, String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date",sdf.format(new Date()));
    }

    public synchronized static boolean handleProvincesResponse(HFWeatherDB hfWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray = new JSONArray(response);
                Log.d("ChooseAreaActivity",String.valueOf(jsonArray.length()));
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    String provinceCode = jsonObject.getString("province_code");
                    province.setProvinceCode(provinceCode);
                    String provinceName = jsonObject.getString("province_name");
                    province.setProvinceName(provinceName);

                    Log.d("ChooseAreaActivity", provinceName);
                    Log.d("ChooseAreaActivity", provinceCode);

                    hfWeatherDB.saveProvince(province);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(HFWeatherDB hfWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray = new JSONArray(response);
                Log.d("ChooseAreaActivity",String.valueOf(jsonArray.length()));
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    String cityCode = jsonObject.getString("city_code");
                    city.setCityCode(cityCode);
                    String cityName = jsonObject.getString("city_name");
                    city.setCityName(cityName);
                    String provinceName = jsonObject.getString("province_name");
                    city.setProvinceName(provinceName);


                    Log.d("ChooseAreaActivity", cityName);
                    Log.d("ChooseAreaActivity", cityCode);

                    hfWeatherDB.saveCity(city);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized static boolean handleCountiesResponse(HFWeatherDB hfWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray = new JSONArray(response);
                Log.d("ChooseAreaActivity",String.valueOf(jsonArray.length()));
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    String countyCode = jsonObject.getString("county_code");
                    county.setCountyCode(countyCode);
                    String countyName = jsonObject.getString("county_name");
                    county.setCountyName(countyName);
                    String cityName = jsonObject.getString("city_name");
                    county.setCityName(cityName);


                    Log.d("ChooseAreaActivity", countyName);
                    Log.d("ChooseAreaActivity", countyCode);

                    hfWeatherDB.saveCounty(county);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
