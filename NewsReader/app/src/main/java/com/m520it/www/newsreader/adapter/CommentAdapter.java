package com.m520it.www.newsreader.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.base.CommonBaseAdapter;
import com.m520it.www.newsreader.bean.CommentBean;
import com.m520it.www.newsreader.bean.DeviceInfoBean;
import com.m520it.www.newsreader.bean.UserBean;
import com.m520it.www.newsreader.util.ImageUtil;

import java.util.List;

/**
 * Created by xmg on 2017/1/6.
 */

public class CommentAdapter extends CommonBaseAdapter<CommentBean> {

    public static final int TYPE_HOT = 0;
    public static final int TYPE_COMMENT = 1;


    public CommentAdapter(List<CommentBean> datas) {
        super(datas);
        //因为第一条是热门跟帖,所以在第一条位置上插入一个无关紧要的数据
        mDatas.add(0,new CommentBean());
        Log.e("xmg", "CommentAdapter: mDatas.size "+mDatas.size()+" datas.size "+datas.size());
    }

    //方法用来指定类型有多少种
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //热门跟帖类型
            return TYPE_HOT;
        } else {
            //评论类型
            return TYPE_COMMENT;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //判断一下当前的Item属于哪种类型,根据类型来搞
        int itemViewType = getItemViewType(position);
        if (itemViewType == TYPE_HOT) {
            //热门跟帖类型
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_hot_title, null);
                //因为不需要任何设置数据的逻辑,不再各种判断和取holder了
            }
        } else {
            CommentViewHolder holder;
            //item是评论类型
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_comment, null);
                holder = new CommentViewHolder();
                //开始找控件
                holder.ivUserIcon = (ImageView)convertView.findViewById(R.id.iv_user_icon);
                holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tvUserInfo = (TextView) convertView.findViewById(R.id.tv_user_info);
                holder.ivSupport = (ImageView) convertView.findViewById(R.id.iv_support);
                holder.tvVoteCount = (TextView) convertView.findViewById(R.id.tv_vote_count);
                holder.flSubFloor = (FrameLayout) convertView.findViewById(R.id.fl_sub_floor);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(holder);
            } else {
                holder = (CommentViewHolder) convertView.getTag();
            }
            //对holder中的控件进行数据设置
            changUI(holder,mDatas.get(position));
        }
        return convertView;
    }


    private void changUI(CommentViewHolder holder, CommentBean commentBean) {
        //点赞效果偷懒了
//        holder.ivSupport
        //北京市 IPhone 6s 2小时前
        UserBean user = commentBean.getUser();
        DeviceInfoBean deviceInfo = commentBean.getDeviceInfo();
        holder.tvUserInfo.setText(user.getLocation()+" "+deviceInfo.getDeviceName()+" "+commentBean.getCreateTime());
        holder.tvUserName.setText(user.getNickname());
        if(TextUtils.isEmpty(user.getAvatar())){
            holder.ivUserIcon.setImageResource(R.drawable.icon_user_default);
        }else{
            ImageUtil.getInstance().display(user.getAvatar(),holder.ivUserIcon,R.drawable.icon_user_default);
        }
        holder.tvVoteCount.setText(String.valueOf(commentBean.getVote()));
        holder.tvComment.setText(commentBean.getContent());
    }


    private class CommentViewHolder {
        ImageView ivUserIcon;
        TextView tvUserName;
        TextView tvUserInfo;
        ImageView ivSupport;
        TextView tvVoteCount;
        FrameLayout flSubFloor;
        TextView tvComment;
    }
}
