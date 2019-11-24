package com.example.nomorekickout_teacher;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

class myAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<MyItem> mylist=new ArrayList<>();

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            if (s.first.equals("wakeAll"));
            else if (s.first.equals("getRoomAwake"));
        }
    });

    public myAdapter(ArrayList<MyItem> itemArray,Context mContext) {
        super();
        this.mContext = mContext;
        mylist=itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onItemSelected(int position) {
    }

    public class ViewHolder {
        public TextView nametext;
        public CheckBox tick;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder view = null;
        LayoutInflater inflator = ((Activity) mContext).getLayoutInflater();
        if (view == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.myadapter, null);
            view.nametext = (TextView) convertView.findViewById(R.id.adaptertextview);
            view.tick=(CheckBox)convertView.findViewById(R.id.adaptercheckbox);
            view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    mylist.get(getPosition).setChecked(buttonView.isChecked());
                    if (isChecked) {
                        serverManager.execute(
                                Pair.create("qtype", "setRoomAwake"),
                                Pair.create("ID", mylist.get(getPosition).getID().toString()),
                                Pair.create("isawake", "1")
                        );
                        statics.putmap(mylist.get(getPosition).getID(), true);
                    }
                    else {
                        serverManager.execute(
                                Pair.create("qtype", "setRoomAwake"),
                                Pair.create("ID", mylist.get(getPosition).getID().toString()),
                                Pair.create("isawake", "0")
                        );
                        statics.putmap(mylist.get(getPosition).getID(), false);
                    }
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        view.tick.setTag(position);
        view.nametext.setText("" + mylist.get(position).getTitle());
        view.tick.setChecked(mylist.get(position).isChecked());
        return convertView;
    }
}
