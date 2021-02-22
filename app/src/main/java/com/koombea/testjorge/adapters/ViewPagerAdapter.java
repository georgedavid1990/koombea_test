package com.koombea.testjorge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.koombea.testjorge.R;
import com.koombea.testjorge.common.OnClickListener;
import com.squareup.picasso.Picasso;

import java.util.Dictionary;
import java.util.List;
import java.util.Objects;

class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Dictionary<Integer, List<String>> mImages;
    private LayoutInflater mLayoutInflater;
    private OnClickListener mClickListener;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, Dictionary<Integer, List<String>> images) {
        mContext = context;
        mImages = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getCount() {
        // return the number of images
        return mImages.size();
    }

    @Override
    public float getPageWidth(int position) {
        //return super.getPageWidth(position);
        return(0.9f);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.user_post_picture_pager_item, container, false);
        itemView.setVisibility(View.VISIBLE);

        // referencing the image view from the item.xml file
        ImageView imageView1 = (ImageView) itemView.findViewById(R.id.img_pager_pic_1);
        ImageView imageView2 = (ImageView) itemView.findViewById(R.id.img_pager_pic_2);

        // setting the image in the imageView
        List<String> images = mImages.get(position);
        if(!images.isEmpty()){
            Picasso.get().load(images.get(0)).fit().noFade().into(imageView1);
            imageView1.setOnClickListener(v -> mClickListener.onClick(v, imageView1, position));
            if(images.size()>1){
                Picasso.get().load(images.get(1)).fit().noFade().into(imageView2);
                imageView2.setOnClickListener(v -> mClickListener.onClick(v, imageView2, position));
            }
        }

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout) object);
    }
}

