package com.m520it.www.newsreader.news.inner.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.base.CommonBaseAdapter;
import com.m520it.www.newsreader.news.inner.bean.NewsDetailBean;
import com.m520it.www.newsreader.util.ImageUtil;

import java.util.List;

/**
 * Created by xmg on 2017/1/3.
 */

//继承基类时,如果基类有泛型,泛型就加在基类后面
public class HotNewsListAdapter extends CommonBaseAdapter<NewsDetailBean> {


//    private final ImageLoader mImageLoader;
//    private final DisplayImageOptions mOptions;

    public HotNewsListAdapter(List<NewsDetailBean> datas) {
        super(datas);
        // Get singleton instance
//        mImageLoader = ImageLoader.getInstance();

        // 展示一个默认图片
// 是否要进行内存缓存
// 是否要进行磁盘缓存
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
//                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
//                .preProcessor(...)
//        .postProcessor(...)
//        .extraForDownloader(...)
//        .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .decodingOptions(...)
//        .displayer(new SimpleBitmapDisplayer()) // default
//                .handler(new Handler()) // default
//        mOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_default) // 展示一个默认图片
//                .cacheInMemory(true) // 是否要进行内存缓存
//                .cacheOnDisk(true) // 是否要进行磁盘缓存
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
//                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
//                .preProcessor(...)
//        .postProcessor(...)
//        .extraForDownloader(...)
//        .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .decodingOptions(...)
//        .displayer(new SimpleBitmapDisplayer()) // default
//                .handler(new Handler()) // default
//                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HotViewViewHolder holder;
        //要用缓存
        //先判断convertView==null
        if(convertView==null){
            //是null   给它赋值
            // 将layout打气出来进行赋值
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_news, parent, false);
            //缓存view为null,将holder创建出来,convertView用来保存holder  将holder通过setTag的方式设置到缓存中
            holder = new HotViewViewHolder();
            // 然后从这里面找控件,将控件设置给Holder里面(为了避免每次找控件,使用holder)
            holder.iv_hot = (ImageView) convertView.findViewById(R.id.iv_hot);
            holder.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
            holder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_top = (TextView) convertView.findViewById(R.id.tv_top);

            convertView.setTag(holder);
        }else{
            //不为null    holder 从convertView中getTag
            holder = (HotViewViewHolder) convertView.getTag();
        }
        //拿到holder   给控件设置数据
        changeUI(holder,mDatas.get(position));
        //最后将缓存view返回出去
        return convertView;
    }

    private void changeUI(HotViewViewHolder holder, NewsDetailBean newsDetailBean) {
        String specialID = newsDetailBean.getSpecialID();
        holder.tv_top.setVisibility(TextUtils.isEmpty(specialID)?View.GONE:View.VISIBLE);
        if(TextUtils.isEmpty(specialID)){
            //如果专题ID是空的,就展示跟帖的文本
            holder.tv_reply.setVisibility(View.VISIBLE);
            //注意点:如果ReplyCount写的int类型,注意转换,否则报错
            String replyCount = newsDetailBean.getReplyCount();
            if(Integer.valueOf(replyCount)==0){
                //如果0跟帖就不展示了
                holder.tv_reply.setVisibility(View.GONE);
            }else{
                holder.tv_reply.setText(replyCount+"跟帖");
            }
        }else{
            //如果专题ID不为空的,就不展示跟帖的文本
            holder.tv_reply.setVisibility(View.GONE);
        }
        holder.tv_title.setText(newsDetailBean.getTitle());
        holder.tv_source.setText(newsDetailBean.getSource());
        //todo 展示图片
//        mImageLoader.displayImage(newsDetailBean.getImgsrc(), holder.iv_hot,mOptions);
        ImageUtil.getInstance().display(newsDetailBean.getImgsrc(), holder.iv_hot);
    }

    private class HotViewViewHolder{
        public ImageView iv_hot;
        public TextView tv_title;
        public TextView tv_source;
        public TextView tv_reply;
        public TextView tv_top;
    }



    //专门用来更新当前Adapter数据

    /**
     *
     * @param datas             加载的数据
     * @param isNeedClearData   是否需要清除数据
     */
    public void updateData(List<NewsDetailBean> datas,boolean isNeedClearData){
//    public void updateData(List<NewsDetailBean> datas){
        //如果是刷新,就先移除以前的数据,展示最新的
        //否则,就不移除,直接添加
        if(isNeedClearData){
            mDatas.clear();
        }
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
}
