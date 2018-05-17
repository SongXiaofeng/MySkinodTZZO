package com.skinod.tzzo.baiduasrtts;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.skinod.tzzo.R;
import com.skinod.tzzo.baiduasrtts.control.InitConfig;
import com.skinod.tzzo.baiduasrtts.control.MyRecognizer;
import com.skinod.tzzo.baiduasrtts.control.MySyntherizer;
import com.skinod.tzzo.baiduasrtts.control.NonBlockSyntherizer;
import com.skinod.tzzo.baiduasrtts.listener.UiMessageListener;
import com.skinod.tzzo.baiduasrtts.recognization.CommonRecogParams;
import com.skinod.tzzo.baiduasrtts.recognization.IStatus;
import com.skinod.tzzo.baiduasrtts.recognization.MessageStatusRecogListener;
import com.skinod.tzzo.baiduasrtts.recognization.StatusRecogListener;
import com.skinod.tzzo.baiduasrtts.recognization.offline.OfflineRecogParams;
import com.skinod.tzzo.baiduasrtts.recognization.online.InFileStream;
import com.skinod.tzzo.baiduasrtts.recognization.online.OnlineRecogParams;
import com.skinod.tzzo.baiduasrtts.util.AutoCheck;
import com.skinod.tzzo.baiduasrtts.util.Logger;
import com.skinod.tzzo.baiduasrtts.util.OfflineResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MybaiduBaseActivity extends AppCompatActivity implements IStatus, View.OnClickListener,MainHandlerConstant{
    private Handler handler;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    /*
 * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
 */
    protected CommonRecogParams apiParams;

    /**
     * 控制UI按钮的状态
     */
    protected int status;

    /*
    * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
    */
    protected boolean enableOffline = false;
    private String TAG = "sxf";


    // ================== tts初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId = "10873693";

    protected String appKey = "26Q2C9shyPEvkAVamAA2AB20";

    protected String secretKey = "71gqktbdXwxwUuXVxrGCuguQMl0EUkV9";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_MALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    private Handler mainHandler;
    protected  static int audio_bt_flag=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_mybaidu_base);

      //  findViewById(R.id.button).setOnClickListener(this);
       // findViewById(R.id.button2).setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        };
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handle(msg);
            }

        };

        Logger.setHandler(handler);
        asrinit();
        initialTts();
    }

    private void asrinit() {
        InFileStream.setContext(this);
        initRecog();
    }

    protected void initRecog() {
        StatusRecogListener listener = new MessageStatusRecogListener(handler);
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = new OnlineRecogParams(this);
        status = STATUS_NONE;
        if (enableOffline) {
            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        }
    }
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                       // toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                         Log.d("sxf_AutoCheckMessage", message);
                        handler.sendEmptyMessage(4001);

                    }
                }
            }

        });

            synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
        }


    protected void handle(Message msg) {
        switch (msg.what) {
            case INIT_SUCCESS:
              Log.d("sxf","tts_init_success");
                msg.what = PRINT;
                break;
            default:
                break;
        }

    }

    protected void speak(String text) {
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
        checkResult(result, "speak");
        }
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.d("sxf","error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onStop() {
        myRecognizer.release();
        Log.i(TAG, "onDestory");
        super.onStop();
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    public void startasr(int bt_flag) {
        audio_bt_flag=bt_flag;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        // 集成时不需要上面的代码，只需要params参数。
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        myRecognizer.start(params);
    }



    /**
     * 开始录音后，手动停止录音。SDK会识别在此过程中的录音。点击“停止”按钮后调用。
     */
    public void stopasr() {
        myRecognizer.stop();
    }

    /**
     * 开始录音后，取消这次录音。SDK会取消本次识别，回到原始状态。点击“取消”按钮后调用。
     */
    public void cancelasr() {
        myRecognizer.cancel();
    }



    @Override
    public void onClick(View v) {

      /*  if (v.getId() == R.id.button) {
            startasr();
        }*/
    }

    protected void handleMsg(Message msg) {
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
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
            // IO 错误自行处理
            e.printStackTrace();
           // toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }


}
