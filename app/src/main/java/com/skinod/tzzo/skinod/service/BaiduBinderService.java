package com.skinod.tzzo.skinod.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.skinod.tzzo.baiduasrtts.MainHandlerConstant;
import com.skinod.tzzo.baiduasrtts.control.InitConfig;
import com.skinod.tzzo.baiduasrtts.control.MyRecognizer;
import com.skinod.tzzo.baiduasrtts.control.NonBlockSyntherizer;
import com.skinod.tzzo.baiduasrtts.listener.UiMessageListener;
import com.skinod.tzzo.baiduasrtts.recognization.CommonRecogParams;
import com.skinod.tzzo.baiduasrtts.recognization.IStatus;
import com.skinod.tzzo.baiduasrtts.recognization.MessageStatusRecogListener;
import com.skinod.tzzo.baiduasrtts.recognization.StatusRecogListener;
import com.skinod.tzzo.baiduasrtts.recognization.offline.OfflineRecogParams;
import com.skinod.tzzo.baiduasrtts.recognization.online.OnlineRecogParams;
import com.skinod.tzzo.baiduasrtts.util.OfflineResource;
import com.skinod.tzzo.baidutranapi.TransApi;
import com.skinod.tzzo.skinod.application.Myapplication;
import com.skinod.tzzo.skinod.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaiduBinderService extends Service implements MainHandlerConstant, IStatus {

    private String[] lan = {"zh", "en", "yue", "jp", "kor",
            "fra", "spa", "th", "ara", "ru",
            "pt", "de", "it", "el", "nl",
            "pl", "bul", "est", "dan", "fin",
            "cs", "rom", "slo", "swe", "hu", "vie"};

    private MyBinder binder = new MyBinder();
    protected MyRecognizer myRecognizer;
    protected CommonRecogParams apiParams;
    protected boolean enableOffline = false;
    protected NonBlockSyntherizer synthesizer;
    private ClientHandler mHandler;
    protected Intent intent;
    private String baidutranres;
    TransApi api = new TransApi(Myapplication.APP_ID, Myapplication.SECURITY_KEY);
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        LogUtil.d("BaiduBinderService_onCreate");
        HandlerThread myThread = new HandlerThread("baidubinderservice");
        myThread.start();
        mHandler = new ClientHandler(myThread.getLooper());
        initRecog();
        initialTts();
    }

    protected void initRecog() {
        StatusRecogListener listener = new MessageStatusRecogListener(mHandler);
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = new OnlineRecogParams(this);
        if (enableOffline) {
            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        }
    }

    protected void initialTts() {
        LoggerProxy.printable(true);
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mHandler);
        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(Myapplication.appId, Myapplication.appKey, Myapplication.secretKey, Myapplication.ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
   /*     AutoCheck.getInstance(this.getActivity()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        // toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                        Log.d("sxf_AutoCheckMessage", message);
                        mHandler.sendEmptyMessage(4001);

                    }
                }
            }

        });*/
        synthesizer = new NonBlockSyntherizer(this, initConfig, mHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(Myapplication.offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return offlineResource;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("BaiduBinderService_onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public BaiduBinderService getService() {
            return BaiduBinderService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRecognizer.release();
        synthesizer.release();
        LogUtil.d("BaiduBinderService_onDestroy");
    }

    public int getRandomNumber() {
        return new Random().nextInt();
    }

    private class ClientHandler extends Handler {
        private String query;
        private String current_languange_one, current_languange_two;

        ClientHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("ClientHandler_message : " + msg.what + ";(msg.arg2=" + msg.arg2);
            final int MSG_TRANSALTE = 8849;
            switch (msg.what) {
                case INIT_SUCCESS:
                    LogUtil.d("sxf", "tts_init_success");
                    msg.what = PRINT;
                    break;
                case STATUS_FINISHED:
                    if (msg.arg2 == 1) {
                        query = msg.obj.toString() + "";
                        LogUtil.d("query=" + query);
                        Pattern p = Pattern.compile("”(.*?)”");
                        Matcher m = p.matcher(query);
                        while (m.find()) {
                            query = m.group(1);
                        }

                        if (query.length() > 0 && !query.contains("error code") && !query.contains("识别错误, 错误码")) {
                            mHandler.sendEmptyMessage(MSG_TRANSALTE);
                        } else {
                            speak("识别错误,请再说一遍");
                        }
                    }
                    break;
                case STATUS_NONE:
                case STATUS_READY:
                case STATUS_SPEAKING:
                case STATUS_RECOGNITION:
                    break;
                case MSG_TRANSALTE:

                    LogUtil.d("sxf", "query=" + query + ";bt_flage=" + Myapplication.AUDIO_BUTTION_FLAG);
                    int left = sp.getInt("Left", -1);
                    int right = sp.getInt("Right", -1);

                    if(left==-1) left=0;
                    if(right==-1) right=1;


                    LogUtil.d("sxf", "query=" + query +
                            ";bt_flage=" + Myapplication.AUDIO_BUTTION_FLAG
                            + ";current_languange_one=" + current_languange_one +
                            ";current_languange_two=" + current_languange_two);

                    if(Myapplication.AUDIO_BUTTION_FLAG==200){
                        current_languange_one = lan[right];
                        current_languange_two = lan[left];
                    }else{
                        current_languange_one = lan[left];
                        current_languange_two = lan[right];
                    }

                    try {
                        baidutranres = api.getTransResult(query, current_languange_one, current_languange_two);
                        //{"from":"zh","to":"en","trans_result":[{"src":"\u4f60\u597d","dst":"Hello,"}]}
                        baidutranres = new JSONObject(baidutranres).get("trans_result").toString();
                        baidutranres = baidutranres.substring(1, baidutranres.length() - 1);
                        baidutranres = new JSONObject(baidutranres).get("dst").toString();
                        LogUtil.d("baidutranres=" + baidutranres);
                        Myapplication.baidutranres = query + "_" + baidutranres;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    speak(baidutranres);
                    intent = new Intent("com.skinod.send_translate_res");
                    BaiduBinderService.this.getApplication().sendBroadcast(intent);
                    break;
            }
        }
    }


    public void startasr() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        // 集成时不需要上面的代码，只需要params参数。
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        myRecognizer.start(params);
    }

/*    public void stopasr() {
        myRecognizer.stop();
    }

    public void cancelasr() {
        myRecognizer.cancel();
    }*/

    protected void speak(String text) {
        LogUtil.d("baidubinderservice_speak_text="+text);
        // mShowText.setText("");
        // String text = mInput.getText().toString();
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (!TextUtils.isEmpty(text)) {
            // text = "百度语音，面向广大开发者永久免费开放语音合成技术。";
            //  mInput.setText(text);
            // 合成前可以修改参数：
            // Map<String, String> params = getParams();
            // synthesizer.setParams(params);
            int result = synthesizer.speak(text);
            checkResult(result);
        }
    }

    private static void checkResult(int result) {
        if (result != 0) {
            LogUtil.d("sxf", "error code :" + result + "错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }
}
