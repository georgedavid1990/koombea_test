package com.koombea.testjorge.data.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.koombea.testjorge.data.dao.UserPostDao;
import com.koombea.testjorge.data.model.UserPost;

import java.util.List;

public class UserPostsViewModel extends AndroidViewModel {

    private final UserPostDao mUserPostDao;
    private final MutableLiveData<List<UserPost>> mPosts;

    public UserPostsViewModel(Application application) {
        super(application);
        mUserPostDao = UserPostDao.getSharedInstance(this.getApplication());
        mPosts = new MutableLiveData<>();
        loadPosts();
    }

    public LiveData<List<UserPost>> getPosts() {
        return mPosts;
    }

    public void loadPosts(){
        final List<UserPost> _posts = mUserPostDao.getUsersPosts();
        mPosts.setValue(_posts);
    }
}
