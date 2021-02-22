package com.koombea.testjorge.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koombea.testjorge.R;
import com.koombea.testjorge.adapters.PostsAdapter;
import com.koombea.testjorge.common.ActivityBase;
import com.koombea.testjorge.common.CustomProgressBar;
import com.koombea.testjorge.common.SimpleGestureFilter;
import com.koombea.testjorge.data.model.UserPost;
import com.koombea.testjorge.data.viewmodel.UserPostsViewModel;
import com.koombea.testjorge.sync.SyncPosts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityBase implements SimpleGestureFilter.SimpleGestureListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewSearchResults;
    private FrameLayout mBlurStack;
    private View mBlurBackground;
    private PostsAdapter mRecyclerViewAdapter;
    private Resources mResources;
    protected static CustomProgressBar mProgressBar;
    private UserPostsViewModel mUserPostsViewModel;
    private List<UserPost> mPostsCopy;
    private List<UserPost> mFilteredPostsCopy;
    private ImageView mBlurImage;
    private Dialog mImagePopUpDialog;
    private SimpleGestureFilter mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControls();
        setAdapter(new ArrayList<>());
        observeForData();
        setOnclickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void setControls() {
        mGestureDetector = new SimpleGestureFilter(MainActivity.this, this);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        mBlurBackground = findViewById(R.id.blur_background);
        mBlurStack = findViewById(R.id.blur_stack);
        mBlurImage = findViewById(R.id.blur_image);
        mResources = getResources();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewSearchResults.setLayoutManager(mLayoutManager);
        mUserPostsViewModel = ViewModelProviders.of(this).get(UserPostsViewModel.class);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(isNetworkConnected()) new LoadDataWS(MainActivity.this).execute();
            else {
                noInternetMsg();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if(!isNetworkConnected()) noInternetMsg();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.mGestureDetector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        hideImagePopUp();
    }

    @Override
    public void onDoubleTap() {
        hideImagePopUp();
    }

    private void setAdapter(ArrayList<UserPost> posts){
        mRecyclerViewAdapter = new PostsAdapter(MainActivity.this, posts);
        mRecyclerViewSearchResults.setAdapter(mRecyclerViewAdapter);
    }

    private void observeForData(){
        mUserPostsViewModel.getPosts().observe(MainActivity.this, users -> {
            if(users == null || users.size() == 0) {
                if(isNetworkConnected()) new LoadDataWS(MainActivity.this).execute();
                else noInternetMsg();
            }else {
                mPostsCopy = users;
                mFilteredPostsCopy = new ArrayList<>(mPostsCopy);
                mRecyclerViewAdapter.addItems(mFilteredPostsCopy);
            }
        });
    }

    private void setOnclickListener(){
        mRecyclerViewAdapter.setClickListener((v, image, position) -> openPosts(image));
    }

    private void openPosts(ImageView image) {
        Bitmap bitmap = blur(MainActivity.this, captureScreenShot(mSwipeRefreshLayout));
        mBlurImage.setImageBitmap(bitmap);
        mBlurStack.setVisibility(View.VISIBLE);
        mRecyclerViewSearchResults.setVisibility(View.GONE);
        mBlurBackground.setBackgroundColor(Color.argb(180, 20, 20, 20));

        mImagePopUpDialog = new Dialog(this);
        mImagePopUpDialog.setContentView(R.layout.dialog_post);
        ImageView img_one_pic = mImagePopUpDialog.findViewById(R.id.img_one_pic);
        img_one_pic.setImageDrawable(image.getDrawable());
        img_one_pic.setOnClickListener(v -> hideImagePopUp());
        img_one_pic.setOnLongClickListener(v -> {
            hideImagePopUp();
            return false;
        });

        if(mImagePopUpDialog.getWindow() != null)
            mImagePopUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mImagePopUpDialog.show();
    }

    private void hideImagePopUp(){
        if(mImagePopUpDialog != null){
            mImagePopUpDialog.dismiss();
            mBlurStack.setVisibility(View.GONE);
            mRecyclerViewSearchResults.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadDataWS extends AsyncTask<String, String, String> {

        String msgError = "";
        ActivityBase context;

        protected void onPreExecute() {
            mProgressBar = new CustomProgressBar();
            mProgressBar.show(context, mResources.getString(R.string.generic_message_progress));
        }

        private LoadDataWS(ActivityBase context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                new SyncPosts().download(context);
            } catch (Exception e) {
                //todo logCaughtException(e);
                msgError = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.getDialog().dismiss();
            mUserPostsViewModel.loadPosts();
            mSwipeRefreshLayout.setRefreshing(false);
            if (!msgError.equals("")) {
                makeErrorDialog(msgError, MainActivity.this);
            }
        }
    }
}
