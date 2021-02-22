package com.koombea.testjorge.sync;

import android.text.TextUtils;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.MutableDocument;
import com.koombea.testjorge.common.ActivityBase;
import com.koombea.testjorge.common.Utilities;
import com.koombea.testjorge.data.dao.DataBaseManager;
import com.koombea.testjorge.data.dao.DbType;
import com.koombea.testjorge.rest.RestClient;
import com.koombea.testjorge.rest.result.DataResult;
import com.koombea.testjorge.rest.result.PostResult;
import com.koombea.testjorge.rest.result.UserPostResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncPosts implements ISyncData {

    @Override
    public void download(ActivityBase context) throws Exception {
        RestClient restClient = RestClient.getRestClient();
        DataResult data = restClient.getPosts().execute().body();

        if(data != null){

            List<UserPostResult> posts = data.getData();
            if(posts != null && !posts.isEmpty()){

                DataBaseManager dataBaseManager = DataBaseManager.getSharedInstance();
                String currentUser = dataBaseManager.getCurrentUser();
                if(TextUtils.isEmpty(currentUser)){
                    dataBaseManager.initCouchbaseLite(context);
                    dataBaseManager.openOrCreateDatabaseForUser(context);
                }

                Database database = DataBaseManager.getDatabase();

                for (UserPostResult userPost : posts) {
                    Map<String, Object> _userPost = new HashMap<>();
                    _userPost.put("uid", userPost.getUid());
                    _userPost.put("name", userPost.getName());
                    _userPost.put("email", userPost.getEmail());
                    _userPost.put("profile_pic", userPost.getProfile_pic());
                    _userPost.put("type", DbType.USER_POST.toString());

                    if(userPost.getPost() != null){
                        PostResult post = userPost.getPost();
                        Map<String, Object> _post = new HashMap<>();
                        _post.put("id", post.getId());
                        String datePattern = "EEE MMM dd yyyy HH:mm:ss zzzz ";
                        Date date = Utilities.getDateFromString(datePattern, post.getDate());
                        _post.put("date", date);
                        _post.put("pics", post.getPics());
                        _userPost.put("post", _post);
                    }

                    try {
                        MutableDocument mutableDocument = new MutableDocument(userPost.getUid(), _userPost);
                        database.save(mutableDocument);
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
