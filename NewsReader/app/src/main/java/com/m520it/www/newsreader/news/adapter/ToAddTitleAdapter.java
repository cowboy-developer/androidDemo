package com.m520it.www.newsreader.news.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.base.CommonBaseAdapter;

import java.util.List;

/**
 * Created by xmg on 2017/1/7.
 */

public class ToAddTitleAdapter extends CommonBaseAdapter<String> {
    public ToAddTitleAdapter(List<String> datas) {
        super(datas);
    }

    //添加一个item到集合最后面
    public void addItem(String item){
        mDatas.add(item);
        notifyDataSetChanged();
    }

    //删除一个item,删除的东西需要放到另一个Adapter,需要拿到删除的数据
    public String deleteItem(int position){
        String remove = mDatas.remove(position);
        notifyDataSetChanged();
        return remove;
    }

    public List<String> getData(){
        return mDatas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitleHolder holder;
        if(convertView==null){
            convertView = View.inflate(parent.getContext(), R.layout.item_grid_title,null);
            holder = new TitleHolder();
            holder.tv_change_title = (TextView) convertView.findViewById(R.id.tv_change_title);
            convertView.setTag(holder);
        }else{
            holder = (TitleHolder) convertView.getTag();
        }
        holder.tv_change_title.setText(mDatas.get(position));
        return convertView;
    }

    private class TitleHolder{
        TextView tv_change_title;
    }
}
