package dick.android.remotecontrol.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dick.android.remotecontrol.MainActivity;
import dick.android.remotecontrol.R;

public class GetDataActivity extends AppCompatActivity {

    private ListView listView;
    private Button getMessageButton;
    private Button jump;
    private List<Data> getdatas = new ArrayList<>();
    static List<Data> datas = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
                    for(Data p : getdatas){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("wifimessage", "wifi: " + p.getWifimessage());
                        item.put("pictureUrl", "picture: " + p.getPictureUrl());
                        data.add(item);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(GetDataActivity.this, data, R.layout.data_item,
                            new String[]{"wifimessage", "pictureUrl"}, new int[]{R.id.wifimessage, R.id.pictureurl});
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);

        init();
        setButton();

    }

    private void init(){
        listView = findViewById(R.id.listview);
        getMessageButton = findViewById(R.id.getmessage);
        jump = findViewById(R.id.jump);
    }

    private void setButton(){
        getMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequestWithOkHttp();
            }
        });
        // 跳转到 MainActivity
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GetDataActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // 访问服务器获取数据
    private List<Data> getRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://139.196.79.193:8080/WebAPP/DataServlet")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    getdatas = parseXML(responseData);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return datas;
    }

    // 解析数据
    private static List<Data> parseXML(String xmlData) throws Exception {
        Data data = new Data();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if ("wifimessage".equals(parser.getName())) {
                        data.setWifimessage(parser.nextText());
                    } else if ("pictureUrl".equals(parser.getName())) {
                        data.setPictureUrl(parser.nextText());
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("data".equals(parser.getName())) {
                        Data tem = new Data(data);
                        datas.add(tem);
                        data.setPictureUrl("");
                        data.setWifimessage("");
                    }
                    break;

                default:
                    break;
            }
            event = parser.next();
        }
        return datas;
    }
}
