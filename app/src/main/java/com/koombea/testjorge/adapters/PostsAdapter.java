package com.koombea.testjorge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.koombea.testjorge.R;
import com.koombea.testjorge.common.OnClickListener;
import com.koombea.testjorge.common.Utilities;
import com.koombea.testjorge.data.model.Post;
import com.koombea.testjorge.data.model.UserPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private static final int VIEW_TYPE_POSTS = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    private Context mContext;
    private List<UserPost> mPosts;
    private OnClickListener mClickListener;

    public void setClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public OnClickListener getClickListener() {
        return mClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView name;
        TextView date;
        TextView email;
        CircleImageView imgUserPhoto;

        ImageView imgOnePic;
        ImageView imgTwoPicsF;
        ImageView imgTwoPicsS;
        ImageView imgThreePicsF;
        ImageView imgThreePicsS;
        ImageView imgThreePicsT;
        ImageView imgFourPicsF;

        ConstraintLayout layoutImgOnePic;
        LinearLayout layoutImgTwoPics;
        LinearLayout layoutImgThreePics;
        LinearLayout layoutImgFourPics;

        //View pager
        ViewPager viewPager;
        ViewPagerAdapter viewPagerAdapter;

        ViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.contentCard);
            name = view.findViewById(R.id.tv_name);
            date = view.findViewById(R.id.tv_date);
            email = view.findViewById(R.id.tv_email);
            imgUserPhoto = view.findViewById(R.id.profile_image);

            imgOnePic = view.findViewById(R.id.img_one_pic);
            imgTwoPicsF = view.findViewById(R.id.img_two_pics_f);
            imgTwoPicsS = view.findViewById(R.id.img_two_pics_s);
            imgThreePicsF = view.findViewById(R.id.img_three_pics_f);
            imgThreePicsS = view.findViewById(R.id.img_three_pics_s);
            imgThreePicsT = view.findViewById(R.id.img_three_pics_t);
            imgFourPicsF = view.findViewById(R.id.img_four_pics_f);

            layoutImgOnePic = view.findViewById(R.id.layout_img_one_pic);
            layoutImgTwoPics = view.findViewById(R.id.layout_img_two_pics);
            layoutImgThreePics = view.findViewById(R.id.layout_img_three_pics);
            layoutImgFourPics = view.findViewById(R.id.layout_img_four_pics);
        }
    }

    static class ViewHolderEmpty extends ViewHolder {
        ViewHolderEmpty(View v) {
            super(v);
        }
    }

    public PostsAdapter(Context context, ArrayList<UserPost> users) {
        mPosts = users;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public @NonNull
    PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case VIEW_TYPE_EMPTY:
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.empty_view, parent, false);
                return new PostsAdapter.ViewHolderEmpty(rl);
            case VIEW_TYPE_POSTS:
            default:
                CardView cvd = (CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_post_list_item, parent, false);
                return new PostsAdapter.ViewHolder(cvd);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsAdapter.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_POSTS) {

            UserPost user = mPosts.get(position);
            Post post = user.getPost();
            holder.name.setText(user.getName());
            holder.email.setText(user.getEmail().toLowerCase());
            holder.date.setText(Utilities.getOrdinalDateFormat(post.getDate()));
            Picasso.get().load(user.getProfilePic()).noFade().into(holder.imgUserPhoto);

            holder.layoutImgOnePic.setVisibility(View.GONE);
            holder.layoutImgTwoPics.setVisibility(View.GONE);
            holder.layoutImgThreePics.setVisibility(View.GONE);
            holder.layoutImgFourPics.setVisibility(View.GONE);

            boolean onePic = post.getPics().size() == 1;
            boolean twoPics = post.getPics().size() == 2;
            boolean threePics = post.getPics().size() == 3;
            boolean fourOrMorePics = post.getPics().size() >= 4;

            if(onePic){
                holder.layoutImgOnePic.setVisibility(View.VISIBLE);
                String url = post.getPics().get(0);
                Picasso.get().load(url).fit().noFade().into(holder.imgOnePic);
                holder.imgOnePic.setOnClickListener(v ->
                        mClickListener.onClick(v, holder.imgOnePic, holder.getAdapterPosition()));
            }else if(twoPics){
                holder.layoutImgTwoPics.setVisibility(View.VISIBLE);
                String url1 = post.getPics().get(0);
                String url2 = post.getPics().get(1);
                Picasso.get().load(url1).fit().noFade().into(holder.imgTwoPicsF);
                Picasso.get().load(url2).fit().noFade().into(holder.imgTwoPicsS);
                holder.imgTwoPicsF.setOnClickListener(v -> mClickListener.onClick(v, holder.imgTwoPicsF, holder.getAdapterPosition()));
                holder.imgTwoPicsS.setOnClickListener(v -> mClickListener.onClick(v, holder.imgTwoPicsS, holder.getAdapterPosition()));
            }else if(threePics){
                holder.layoutImgThreePics.setVisibility(View.VISIBLE);
                String url1 = post.getPics().get(0);
                String url2 = post.getPics().get(1);
                String url3 = post.getPics().get(2);
                Picasso.get().load(url1).fit().noFade().into(holder.imgThreePicsF);
                Picasso.get().load(url2).fit().noFade().into(holder.imgThreePicsS);
                Picasso.get().load(url3).fit().noFade().into(holder.imgThreePicsT);
                holder.imgThreePicsF.setOnClickListener(v ->
                        mClickListener.onClick(v, holder.imgThreePicsF, holder.getAdapterPosition()));
                holder.imgThreePicsS.setOnClickListener(v ->
                        mClickListener.onClick(v, holder.imgThreePicsS, holder.getAdapterPosition()));
                holder.imgThreePicsT.setOnClickListener(v ->
                        mClickListener.onClick(v, holder.imgThreePicsT, holder.getAdapterPosition()));
            }else if(fourOrMorePics){
                holder.layoutImgFourPics.setVisibility(View.VISIBLE);
                Picasso.get().load(post.getPics().get(0)).fit().noFade().into(holder.imgFourPicsF);
                holder.imgFourPicsF.setOnClickListener(v ->
                        mClickListener.onClick(v, holder.imgFourPicsF, holder.getAdapterPosition()));

                holder.viewPager = (ViewPager)holder.layoutImgFourPics.findViewById(R.id.viewPagerMain);
                holder.viewPagerAdapter = new ViewPagerAdapter(mContext, getPagerViews(post));
                holder.viewPagerAdapter.setClickListener((v, image, pagerPosition) -> mClickListener.onClick(v, image, pagerPosition));
                holder.viewPager.setAdapter(holder.viewPagerAdapter);
            }
        }
    }

    public void addItems(List<UserPost> posts) {
        this.mPosts = posts;
        notifyDataSetChanged();
    }

    public List<UserPost> getItems() {
        return mPosts;
    }

    @Override
    public int getItemViewType(int position) {
        if (mPosts.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }else{
            return VIEW_TYPE_POSTS;
        }
    }

    @Override
    public int getItemCount() {
        if(mPosts.size() == 0){
            return 1;
        }else {
            return mPosts.size();
        }
    }

    private Dictionary<Integer, List<String>> getPagerViews(Post post){
        Dictionary<Integer, List<String>> images = new Hashtable<>();
        int key = 0;
        for (int i = 1; i < post.getPics().size(); i++) {
            ArrayList<String> pageImages = new ArrayList<>();
            String img1 = post.getPics().get(i);
            pageImages.add(img1);

            if((i+1) < post.getPics().size()){
                String img2 = post.getPics().get(i+1);
                pageImages.add(img2);
            }

            images.put(key, pageImages);
            key++;
            i++;
        }
        return images;
    }
}
