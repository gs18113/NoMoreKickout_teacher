package com.example.nomorekickout_teacher;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter2 extends BaseAdapter {

    private Context mcontext;
    private ArrayList<MyItem2> mylist=new ArrayList<>();

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
        }
    });

    public myAdapter2(ArrayList<MyItem2> itemArray, Context mcontext) {
        super();
        mylist=itemArray;
        this.mcontext=mcontext;
    }

    @Override
    public int getCount() {return mylist.size();}

    @Override
    public String getItem(int i) {return mylist.get(i).toString();}

    @Override
    public long getItemId(int i) {return i;}

    public class ViewHolder2 {
        public TextView nameText;
        public Button okButton, noButton;
    }

    public void remove(int position) {
        mylist.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder2 view = null;
        LayoutInflater inflator = ((Activity)mcontext).getLayoutInflater();
        if (view == null) {
            view = new ViewHolder2();
            convertView = inflator.inflate(R.layout.myadapter2, null);
            view.nameText = (TextView) convertView.findViewById(R.id.adapter2textview);
            view.okButton = (Button) convertView.findViewById(R.id.adapter2okbutton);
            view.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serverManager.execute(
                            Pair.create("qtype", "answerRequest"),
                            Pair.create("RID", mylist.get(position).getRID().toString()),
                            Pair.create("confirm", "1")
                    );
                    remove(position);
                }
            });
            view.noButton = (Button) convertView.findViewById(R.id.adapter2nobutton);
            view.noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serverManager.execute(
                            Pair.create("qtype", "answerRequest"),
                            Pair.create("RID", mylist.get(position).getRID().toString()),
                            Pair.create("confirm", "0")
                    );
                    remove(position);
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHolder2) convertView.getTag();
        }
        view.nameText.setText("" + mylist.get(position).getTitle());
        view.noButton.setTag(position);
        view.okButton.setTag(position);
        return convertView;
    }
}
