package dick.android.remotecontrol;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddControllerActivity extends AppCompatActivity {
    private ImageView addPhoto;
    private Bitmap photo;
    private Uri uri;

    private Button ok;
    private String wifiMessage;

    public static final String BASE_URL = "http://139.196.79.193:8080/WebAPP";
//    public static final String BASE_URL =  "http://172.19.45.126:8080/WebAPP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontroller);

        init();

        setImageButton();
    }

    private void init(){
        addPhoto = findViewById(R.id.img_button);
        ok = findViewById(R.id.ok);
        wifiMessage = "wifiTest";
    }

    private void setImageButton(){
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null) {
                    Toast.makeText(AddControllerActivity.this, "请选择图片", Toast.LENGTH_LONG).show();
                } else {
                    String filePath = uri.getEncodedPath();
                    final String imagePath = Uri.decode(filePath);

                    Toast.makeText(AddControllerActivity.this, "图片位置:" + imagePath, Toast.LENGTH_LONG).show();

                    uploadImage(imagePath);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            // 得到图片的全路径
            uri = data.getData();
            this.addPhoto.setImageURI(uri);
            ContentResolver resolver = getContentResolver();
            //使用ContentProvider通过URI获取原始图片
            if(uri != null){
                try {
                    photo = MediaStore.Images.Media.getBitmap(resolver, uri);
                } catch (IOException ignored) {
                }
            }
            Toast.makeText(AddControllerActivity.this, "图片位置:" + uri.getPath(), Toast.LENGTH_LONG).show();

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 上传图片
     * @param imagePath
     */
    private void uploadImage(String imagePath) {
        new NetworkTask().execute(imagePath);
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Server ", "服务器响应" + result);
            if(!"error".equals(result)) {
                Log.i("Send ", "图片地址 " + BASE_URL + result);
            }
        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("wifiMessage", wifiMessage);
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), convertBitmapToFile(photo)));
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(BASE_URL + "/uploadimage")
                .post(requestBody)
                .build();

        Log.d("send ", "请求地址 " + BASE_URL + "/uploadimage");
        Log.d("send ", "传递消息 " + wifiMessage);

        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d("send ", "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d("send ", "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // convert bitmap to file
    private File f;
    private File convertBitmapToFile(Bitmap bitmap) {
        try {
            // create a file to write bitmap data
            f = new File(AddControllerActivity.this.getCacheDir(), "portrait");
            f.createNewFile();

            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {

        }
        return f;
    }

}