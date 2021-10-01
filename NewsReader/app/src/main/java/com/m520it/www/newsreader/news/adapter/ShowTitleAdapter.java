package com.m520it.www.newsreader.news.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.base.CommonBaseAdapter;

import java.util.List;

/**
 * Created by xmg on 2017/1/7.
 */

public class ShowTitleAdapter extends CommonBaseAdapter<String> {


    public ShowTitleAdapter(List<String> datas) {
        super(datas);
    }

    public boolean isShowDelete(){
        return isShowDelete;
    }

    public List<String> getData(){
        return mDatas;
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

    public void setShowDelete(boolean isShow){
        //判断如果跟以前的值一样,什么事都不做
        if(isShowDelete==isShow){
            return;
        }
        this.isShowDelete = isShow;
        notifyDataSetChanged();
    }

    private boolean isShowDelete = false;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitleHolder holder;
        if(convertView==null){
            convertView = View.inflate(parent.getContext(), R.layout.item_grid_title,null);
            holder = new TitleHolder();
            holder.tv_change_title = (TextView) convertView.findViewById(R.id.tv_change_title);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        }else{
            holder = (TitleHolder) convertView.getTag();
        }
        holder.tv_change_title.setText(mDatas.get(position));
        //让图片控件是否展示
        if(isShowDelete){
            if(position==0){
                holder.iv_delete.setVisibility(View.GONE);
            }else{
                holder.iv_delete.setVisibility(View.VISIBLE);
            }
        }else{
            holder.iv_delete.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class TitleHolder{
        TextView tv_change_title;
        ImageView iv_delete;
    }
}
