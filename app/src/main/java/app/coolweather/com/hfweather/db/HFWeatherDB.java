package app.coolweather.com.hfweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.coolweather.com.hfweather.model.City;
import app.coolweather.com.hfweather.model.County;
import app.coolweather.com.hfweather.model.Province;

/**
 * Created by Jay on 2015/10/16.
 */
public class HFWeatherDB {
    public  static final String DB_NAME = "hf_weather";
    public static final int VERSION = 1;
    private  static HFWeatherDB hfWeatherDB;
    private SQLiteDatabase db;

    private HFWeatherDB(Context context){
        HFWeatherOpenHelper dbHelper = new HFWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }
    public  synchronized static HFWeatherDB getInstance(Context context){
        if(hfWeatherDB == null){
            hfWeatherDB = new HFWeatherDB(context);
        }
        return hfWeatherDB;
    }


    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_name",city.getProvinceName());
            db.insert("City",null,values);
        }
    }
    public List<City> loadCities(String provinceName){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_name = ?",new String[] {provinceName},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceName(provinceName);
                list.add(city);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_name",county.getCityName());
            db.insert("County",null,values);
        }
    }
    public List<County> loadCounties(String cityName){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_name = ?",new String[] {cityName},null,null,null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityName(cityName);
                list.add(county);
            }while (cursor.moveToNext());
        }
        return list;
    }


}
