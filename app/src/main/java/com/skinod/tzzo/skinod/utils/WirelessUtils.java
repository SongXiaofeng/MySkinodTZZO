package com.skinod.tzzo.skinod.utils;

/**
 * Created by Administrator on 2018/3/16.
 */
import android.content.Context;
import android.provider.Settings;

public class WirelessUtils {

  /*  public static boolean isRadioAllowed(Context context, String type) {
        if (!isAirplaneModeOn(context)) {
            return true;
        }
        String toggleable = Settings.Global.getString(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_TOGGLEABLE_RADIOS);
        return toggleable != null && toggleable.contains(type);
    }*/

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

}

