package dick.android.remotecontrol.AC;

import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dick.android.remotecontrol.R;

//需要api大于19与下面if判断用途类似
@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class InfraredActivity extends AppCompatActivity implements View.OnClickListener{
    //获取红外控制类
    private ConsumerIrManager IR;
    //判断是否有红外功能
    boolean IRBack;
    private TextView tv_detail;
    private Button btn_AirConditioner_GL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared);
        inItEvent();
        inItUI();
    }


    //初始化事务
    private void inItEvent() {
        //获取ConsumerIrManager实例
        IR = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        //如果sdk版本大于4.4才进行是否有红外的功能（手机的android版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IRBack = IR.hasIrEmitter();
            if (!IRBack) {
                Toast.makeText(InfraredActivity.this, "对不起，该设备上没有红外功能!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(InfraredActivity.this, "红外设备就绪", Toast.LENGTH_LONG).show();
            }
        }
    }

    //初始化UI
    private void inItUI() {
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        btn_AirConditioner_GL = (Button) findViewById(R.id.btn_AirConditioner_GL);
        btn_AirConditioner_GL.setOnClickListener(this);
    }


    /**
     * 发射红外信号
     *
     * @param carrierFrequency 红外传输的频率，一般的遥控板都是38KHz
     * @param pattern          指以微秒为单位的红外开和关的交替时间
     */
    private void sendMsg(int carrierFrequency, int[] pattern) {
        IR.transmit(carrierFrequency, pattern);

        Toast.makeText(InfraredActivity.this, "发送成功", Toast.LENGTH_LONG).show();

        String content = null;
        for(int i = 0;i<pattern.length;i++){
            content += String.valueOf(pattern[i])+",";
        }
        tv_detail.setText(content+"\n"+(pattern.length)+"个时间数据");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_AirConditioner_GL:
                if (IRBack) {
                    sendMsg(38000, CodeCommand.base);
                } else {
                    Toast.makeText(InfraredActivity.this, "对不起，该设备上没有红外功能!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}