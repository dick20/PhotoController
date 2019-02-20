package dick.android.remotecontrol;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddControllerActivity extends AppCompatActivity {
    private ImageView addPhoto;
    private Bitmap photo;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontroller);

        init();

        setImageButton();
    }

    private void init(){
        addPhoto = findViewById(R.id.img_button);
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}