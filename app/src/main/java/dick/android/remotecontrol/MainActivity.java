package dick.android.remotecontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Mycontroller> list;
    private MyRecyclerViewAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        init();
        // 显示recyclerView
        setRecyclerView();
        // 设置按钮
        setButton();
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView_controller);
        button = findViewById(R.id.add);
    }

    private void setRecyclerView(){
        list = new ArrayList<>();
        Bitmap head1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.tv)).getBitmap();
        Bitmap head2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ac)).getBitmap();
        list.add(new Mycontroller(head1,"长虹电视","tv","<20m"));
        list.add(new Mycontroller(head2,"美的空调","ac","<120m"));
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new MyRecyclerViewAdapter(MainActivity.this,R.layout.controller_item,list);
        recyclerView.setAdapter(adapter);
        // 设置监听器
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("repoName",list.get(position).getType());

                Log.i("tag","click~");
                // 对于空调遥控器
                if(list.get(position).getType().equals("ac")){
                    intent.setClass(MainActivity.this,AcDetailActivity.class);
                }
                // 对于电视遥控器
                else if(list.get(position).getType().equals("tv")){
                    intent.setClass(MainActivity.this,TvDetailActivity.class);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    private void setButton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"添加遥控器",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
