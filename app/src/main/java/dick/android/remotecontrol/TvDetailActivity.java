package dick.android.remotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TvDetailActivity extends AppCompatActivity implements WheelView.WheelClickListener{
    private WheelView wheel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);
        wheel = (WheelView) findViewById(R.id.ctrls);
        wheel.setWheelClickListener(this);
    }
    @Override
    public void onWheelClick(int type) {
        switch (type) {
            case WheelView.CLICK_BOTTOM_DOWN:
                //下面按钮按下的时候
                break;
            case WheelView.CLICK_LEFT_DOWN:
                //左边按钮按下的时候
                break;
            case WheelView.CLICK_RIGHT_DOWN:
                //右边按钮按下的时候
                break;
            case WheelView.CLICK_TOP_DOWN:
                //上面按钮按下的时候
                break;
            case WheelView.CLICK_BOTTOM_UP:
                //下面按钮按下抬起的时候
                break;
            case WheelView.CLICK_LEFT_UP:
                //左边按钮按下抬起的时候
                break;
            case WheelView.CLICK_RIGHT_UP:
                //右边按钮按下抬起的时候
                break;
            case WheelView.CLICK_TOP_UP:
                //上面按钮按下抬起的时候
                break;
        }
    }
}
