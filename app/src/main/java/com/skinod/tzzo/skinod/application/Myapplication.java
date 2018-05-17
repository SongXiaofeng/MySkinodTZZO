package com.skinod.tzzo.skinod.application;

import android.app.Application;

import com.baidu.tts.client.TtsMode;
import com.skinod.tzzo.baiduasrtts.util.OfflineResource;


public class Myapplication extends Application {
    //  public static  String current_left_languange="zh";
    //  public static String  current_right_languange="en";
    public static final String APP_ID = "20171223000107788";
    public static final String SECURITY_KEY = "F6dVpPuThjwl_viiQ2gb";

    public static final String appId = "10873693";
    public static final String appKey = "26Q2C9shyPEvkAVamAA2AB20";
    public static final String secretKey = "71gqktbdXwxwUuXVxrGCuguQMl0EUkV9";

    public static final TtsMode ttsMode = TtsMode.MIX;

    public static final int NUMBER_KEYBOARD = 12;
    public static final int LATTER_KEYBOARD = 13;
    public static final int CHAR_KEYBOARD = 14;

    public static int AUDIO_BUTTION_FLAG = 100;


    public static String baidutranres = " ";

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    public static final String offlineVoice = OfflineResource.VOICE_MALE;

    public static final String CHAT_DATABASE_NAME = "chatting_table";

}
