package app.coolweather.com.hfweather.util;

/**
 * Created by Jay on 2015/10/8.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
