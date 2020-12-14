package nuce.tatv.smarthome.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nuce.tatv.smarthome.Model.Data;
import nuce.tatv.smarthome.R;

public class SocketAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Data> listData;

    public SocketAdapter(@NonNull Context context, int resource, @NonNull List listData) {
        super(context, resource, listData);
        this.context = context;
        this.resource = resource;
        this.listData = listData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTopic =convertView.findViewById(R.id.tvTopic);
            viewHolder.tvMessage =convertView.findViewById(R.id.tvMessage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Data data = listData.get(position);
        viewHolder.tvTopic.setText(data.getmTopic());
        viewHolder.tvMessage.setText(data.getmMessage());
        return convertView;
    }
    public class ViewHolder{
        TextView tvTopic, tvMessage;
    }
}
