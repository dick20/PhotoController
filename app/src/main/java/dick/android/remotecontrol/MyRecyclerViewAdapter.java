package dick.android.remotecontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyViewHolder>{
    private List<Mycontroller> data;
    private Context context;
    private int layoutId;
    private OnItemClickListener onItemClickListener;
    //点击事件的接口
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    //删除数据
    public void deleteData(int position){
        data.remove(position);
    }

    public MyRecyclerViewAdapter(Context _context, int _layoutId, List<Mycontroller> _data){
        context = _context;
        layoutId = _layoutId;
        data = _data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.controller_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ((ImageView)holder.getView(R.id.head)).setImageBitmap(data.get(position).getIcon());
        ((TextView)holder.getView(R.id.name)).setText(data.get(position).getName());
        ((TextView)holder.getView(R.id.description)).setText(data.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if(!data.isEmpty())
            return data.size();
        return 0;
    }

}