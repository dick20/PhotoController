package dick.android.remotecontrol;
import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.LinkedHashMap;
        import java.util.List;

public class ElectricalApplianceAdapter extends BaseAdapter {
    private Context mContext;
    private List<ElectricalAppliance> mList = new ArrayList<>();

    public ElectricalApplianceAdapter(Context context, List<ElectricalAppliance> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item_1, null);
            viewHolder.icon = view.findViewById(R.id.image_view1);
            viewHolder.name = view.findViewById(R.id.text_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageBitmap(mList.get(i).getImage());
        viewHolder.name.setText(mList.get(i).getName());
        return view;
    }

    /**
     * 点赞按钮的监听接口
     */
    public interface onItemLikeListener {
        void onLikeClick(int i);
    }

    private onItemLikeListener mOnItemLikeListener;

    public void setOnItemLikeClickListener(onItemLikeListener mOnItemLikeListener) {
        this.mOnItemLikeListener = mOnItemLikeListener;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
    }

}