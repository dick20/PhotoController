package dick.android.remotecontrol;

import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import dick.android.remotecontrol.AC.AirBean;
import dick.android.remotecontrol.AC.CodeCommand;

//需要api大于19与下面if判断用途类似
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AcDetailActivity extends AppCompatActivity implements View.OnClickListener{

    //获取红外控制类
    private ConsumerIrManager IR;
    //判断是否有红外功能
    boolean IRBack;

    private View view;
    private TextView tempShow, airWindDir, windDirAuto, windSpeed, modeShow;
    private ImageButton air_power, air_tempadd, air_tempsub;
    private Button air_mode, air_wind_auto_dir, air_wind_count, air_wind_dir ;

    //开关、度数、模式、自动手动、风向、风量
    private AirBean airBean = new AirBean(0, 25, 0, 0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_detail);
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
                Toast.makeText(AcDetailActivity.this, "对不起，该设备上没有红外功能!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AcDetailActivity.this, "红外设备就绪", Toast.LENGTH_LONG).show();
            }
        }
    }

    //初始化UI
    private void inItUI() {
        air_power = findViewById(R.id.open_but);
        air_mode = findViewById(R.id.mode_but);
        air_tempadd = findViewById(R.id.increase_but);
        air_tempsub = findViewById(R.id.decrease_but);
        air_wind_auto_dir = findViewById(R.id.menergy_but);
        air_wind_count = findViewById(R.id.speed_but);
        air_wind_dir = findViewById(R.id.direction_but);

        air_power.setOnClickListener(this);
        air_mode.setOnClickListener(this);
        air_tempadd.setOnClickListener(this);
        air_tempsub.setOnClickListener(this);
        air_wind_auto_dir.setOnClickListener(this);
        air_wind_count.setOnClickListener(this);
        air_wind_dir.setOnClickListener(this);

        tempShow = findViewById(R.id.number);
        modeShow = findViewById(R.id.mode);
        windDirAuto = findViewById(R.id.menergy_but);
        windSpeed = findViewById(R.id.wind_speed);
        airWindDir = findViewById(R.id.wind_direction);

    }

    @Override
    public void onStart() {
        super.onStart();
        updataAirUI(airBean);
    }

    /**
     * 点击处理
     */
    @Override
    public void onClick(View v) {
        //五中模式
        int data;
        //关机状态
        if (!IRBack) {
            Toast.makeText(AcDetailActivity.this, "对不起，该设备上没有红外功能!", Toast.LENGTH_LONG).show();
            return;
        }
        if (airBean.getmPower() == 0x00 && v.getId() != R.id.open_but) {
            return;
        }

        switch (v.getId()) {

            case R.id.mode_but:
                data = airBean.getmMode();
                data++;
                if (data > 4) {
                    data = 0;
                }
                airBean.setmMode(data);
                SendMsg(airBean);
                break;
            case R.id.open_but:

                if (airBean.getmPower() == 0) {
                    airBean.setmPower(1);
                } else {
                    airBean.setmPower(0);
                }
                //发送消息
                SendMsg(airBean);
                break;
            case R.id.increase_but:
                data = airBean.getmTmp();
                data++;
                if (data > 30) {
                    data = 30;
                }
                airBean.setmTmp(data);
                SendMsg(airBean);
                break;
            case R.id.decrease_but:
                data = airBean.getmTmp();
                data--;
                if (data < 16) {
                    data = 16;
                }
                airBean.setmTmp(data);
                SendMsg(airBean);
                break;
            case R.id.menergy_but:
                if (airBean.getMenergy() == 0) {
                    airBean.setMenergy(1);
                } else {
                    airBean.setMenergy(0);
                }
                SendMsg(airBean);
                break;
            case R.id.speed_but:
                data = airBean.getmWindCount();
                data++;
                if (data > 3) {
                    data = 0;
                }
                airBean.setmWindCount(data);
                SendMsg(airBean);
                break;
            case R.id.direction_but:
                data = airBean.getmWindDir();
                data++;

                if (data > 3) {
                    data = 0;
                }
                airBean.setmWindDir(data);
                SendMsg(airBean);
                break;
            default:
                break;
        }
        //不论点击了什么 都要更新UI
        updataAirUI(airBean);
    }

    /**
     * 更新UI
     *
     */
    public void updataAirUI(AirBean airBean_ui) {

        if (airBean_ui.getmPower() == 0x01) {

            if (airBean_ui.getmMode() == 0x00) {
                modeShow.setText("自动");
                tempShow.setText(String.valueOf(airBean_ui.getmTmp()));
            }
            if (airBean_ui.getmMode() == 0x01) {
                modeShow.setText("制冷");
                tempShow.setText(String.valueOf(airBean_ui.getmTmp()));
            }
            if (airBean_ui.getmMode() == 0x02) {
                modeShow.setText("除湿");
                tempShow.setText("");
            }
            if (airBean_ui.getmMode() == 0x03) {
                modeShow.setText("送风");
                tempShow.setText("");
            }
            if (airBean_ui.getmMode() == 0x04) {
                modeShow.setText("加热");
                tempShow.setText(String.valueOf(airBean_ui.getmTmp()));
            }

            if (airBean_ui.getmWindCount() == 0x00) {
                windSpeed.setText("风速: 小风");
            } else if (airBean_ui.getmWindCount() == 0x01) {
                windSpeed.setText("风速: 中风");
            } else if (airBean_ui.getmWindCount() == 0x02) {
                windSpeed.setText("风速: 强风");
            } else if (airBean_ui.getmWindCount() == 0x03) {
                windSpeed.setText("风速: 自动");
            }

            if (airBean_ui.getmWindDir() == 0x00) {
                airWindDir.setText("风向: 上下扫风");

            } else if (airBean_ui.getmWindDir() == 0x01) {
                airWindDir.setText("风向: 左右扫风");

            } else if (airBean_ui.getmWindDir() == 0x02) {
                airWindDir.setText("风向: 不动");

            }

            if (airBean_ui.getMenergy() == 0x00) {
                windDirAuto.setText("节能");

            } else if (airBean_ui.getMenergy() == 0x01) {
                windDirAuto.setText("正常");
            }

        } else {
            tempShow.setText("0");
            windSpeed.setText("风速");
            airWindDir.setText("正常");
            modeShow.setText("模式");
        }
    }

    /**
     * 逻辑处理
     * 发送消息
     *
     */
    public void SendMsg(AirBean airBean_Event) {
        Log.e("gaoyu", "要发送的信息" + airBean_Event.toString());

        int mPower = airBean_Event.getmPower(); //开关
        int mTmp = airBean_Event.getmTmp();  //温度
        int mMode = airBean_Event.getmMode();  //模式
        int menergy = airBean_Event.getMenergy();  //节能省电/换气
        int mWindDir = airBean_Event.getmWindDir();   //风向
        int mWindCount = airBean_Event.getmWindCount(); //风量

        int tmWindDir = 0;//二进制方向
        //左右扫风风向判断
        if (mWindDir == 2) {
            tmWindDir = 1;
        } else if (mWindDir == 1) {
            tmWindDir = 0;
        } else {
            tmWindDir = 0;
        }
        //根据所选模式确定检验码
        //校验码 = [(模式 – 1) + (温度 – 16) + 5 +左右扫风+换气+节能]取二进制后四位，再逆序
        //以下为了思路清晰 就不写在一起了
        int check = (mMode - 1) + (mTmp - 16) + 5 + tmWindDir + menergy + menergy;//十进制数字
        String two_chack = Integer.toBinaryString(check);//转换成二进制
        //如果大于四位进行裁剪
        //补零
        switch (two_chack.length()){
            case 3:
                two_chack = "0"+two_chack;
                break;
            case 2:
                two_chack = "00"+two_chack;
                break;
            case 1:
                two_chack = "000"+two_chack;
                break;
        }
        two_chack = two_chack.substring(two_chack.length() - 4, two_chack.length());//取后四位
        String Cut = new StringBuffer(two_chack).reverse().toString();//倒序
        Log.e("gaoyu", "裁剪之前" + two_chack + "裁剪倒序之后" + Cut);

        //分解字符（承载最后四个逆序字符）
        char[] item = new char[5];
        for (int i = 0; i < Cut.length(); i++) {
            item[i] = Cut.charAt(i);
        }
        //操作大数组
        int base[] = CodeCommand.base;

        //第一步 替换校验码  （分七步）
        //取出数组里的四个数
        int one = Integer.valueOf(String.valueOf(item[0])).intValue();
        int two = Integer.valueOf(String.valueOf(item[1])).intValue();
        int three = Integer.valueOf(String.valueOf(item[2])).intValue();
        int four = Integer.valueOf(String.valueOf(item[3])).intValue();
        //64-67位为校验码 131、132 \ 133、134 \ 135、136 \ 137、138
        //第一个数
        if (one == 1) {
            Log.e("gaoyu", "第一个数是1");
            //将大数组里的130、131位置1
            base[130] = CodeCommand.check_d;
            base[131] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第一个数是0");
            //将大数组里的64位不用变
        }
        //第二个数
        if (two == 1) {
            Log.e("gaoyu", "第二个数是1");
            //将大数组里的132、133位置1
            base[132] = CodeCommand.check_d;
            base[133] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第二个数是0");
            //将大数组里的132、133位不用变
        }
        //第三个数
        if (three == 1) {
            Log.e("gaoyu", "第三个数是1");
            //将大数组里的134、135位置1
            base[134] = CodeCommand.check_d;
            base[135] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第三个数是0");
            //将大数组里的134、135位不用变
        }
        //第四个数
        if (four == 1) {
            Log.e("gaoyu", "第四个数是1");
            //将大数组里的136、137位置1
            base[136] = CodeCommand.check_d;
            base[137] = CodeCommand.check_u;
        } else {
            Log.e("gaoyu", "第四个数是0");
            //将大数组里的136、137位不用变
        }

        //第二步 开关  8/9
        if (mPower == 1) {
            Log.e("gaoyu", "开");
            base[8] = CodeCommand.onedown;
            base[9] = CodeCommand.oneup;
        } else {
            base[8] = CodeCommand.zerodown;
            base[9] = CodeCommand.zeroup;
            Log.e("gaoyu", "关");
        }

        //第三步 温度 16-30度   数组中18、25
        switch (mTmp) {
            case 16:
                //默认十六
                break;
            case 17:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 18:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 19:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 20:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 21:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 22:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 23:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 24:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 25:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 26:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 27:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 28:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 29:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 30:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            default:
                break;
        }

        //第四步  模式  2-7
        switch (mMode) {
            case 0:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 1:
                base[2] = CodeCommand.onedown;
                base[3] = CodeCommand.oneup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 2:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.onedown;
                base[5] = CodeCommand.oneup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 3:
                base[2] = CodeCommand.onedown;
                base[3] = CodeCommand.oneup;
                base[4] = CodeCommand.onedown;
                base[5] = CodeCommand.oneup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 4:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.onedown;
                base[7] = CodeCommand.oneup;
                break;
        }
        //第五步 节电、换气 48-51
        if (menergy == 1) {
            Log.e("gaoyu", "开启节电换气");
            base[48] = CodeCommand.onedown;
            base[49] = CodeCommand.oneup;
            base[50] = CodeCommand.onedown;
            base[51] = CodeCommand.oneup;
        }else{
            base[48] = CodeCommand.zerodown;
            base[49] = CodeCommand.zeroup;
            base[50] = CodeCommand.zerodown;
            base[51] = CodeCommand.zeroup;
        }
        //第六步  风向  1、上下 36 数组 74.75   2、左右 40  80.81
        switch (mWindDir) {
            case 0:
                //默认
                break;
            case 1:
                base[74] = CodeCommand.onedown;
                base[75] = CodeCommand.oneup;
                break;
            case 2:
                base[80] = CodeCommand.onedown;
                base[81] = CodeCommand.oneup;
                break;
        }

        //第七步  风量  10-13
        switch (mWindCount) {
            case 0:
                //默认
                break;
            case 1:
                base[10] = CodeCommand.onedown;
                base[11] = CodeCommand.oneup;
                base[12] = CodeCommand.zerodown;
                base[13] = CodeCommand.zeroup;
                break;
            case 2:
                base[10] = CodeCommand.zerodown;
                base[11] = CodeCommand.zeroup;
                base[12] = CodeCommand.onedown;
                base[13] = CodeCommand.oneup;
                break;
            case 3:
                base[10] = CodeCommand.onedown;
                base[11] = CodeCommand.oneup;
                base[12] = CodeCommand.onedown;
                base[13] = CodeCommand.oneup;
                break;
        }

        //最后一步 调取红外进行发送
        String content = null;
        for (int i = 0; i < base.length; i++) {
            content += String.valueOf(base[i]) + ",";
        }
        Log.e("gaoyu", "数组信息是" + content);
        //发送完数据将大数组还原
        sendIrMsg(38000,base);
        base = CodeCommand.base;
    }

    /**
     * 发射红外信号
     * 可以查看这个标签的log   ConsumerIr
     * @param carrierFrequency 红外传输的频率，一般的遥控板都是38KHz
     * @param pattern          指以微秒为单位的红外开和关的交替时间
     */
    private void sendIrMsg(int carrierFrequency, int[] pattern) {
        IR.transmit(carrierFrequency, pattern);

        Toast.makeText(AcDetailActivity.this, "发送成功", Toast.LENGTH_LONG).show();
        String content = null;
        for(int i = 0;i<pattern.length;i++){
            content += String.valueOf(pattern[i])+",";
        }
        Log.e("gaoyu", "数组信息是" + content);
        Log.e("gaoyu", "一共有" + pattern.length);
    }

}
