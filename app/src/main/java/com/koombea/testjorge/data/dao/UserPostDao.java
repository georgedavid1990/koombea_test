package com.koombea.testjorge.data.dao;

import android.content.Context;
import android.text.TextUtils;

import com.couchbase.lite.Array;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.koombea.testjorge.data.model.Post;
import com.koombea.testjorge.data.model.UserPost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPostDao {

    private static UserPostDao mInstance = null;

    protected UserPostDao(Context context) {
        DataBaseManager dataBaseManager = DataBaseManager.getSharedInstance();
        String currentUser = dataBaseManager.getCurrentUser();
        if(TextUtils.isEmpty(currentUser)){
            dataBaseManager.initCouchbaseLite(context);
            dataBaseManager.openOrCreateDatabaseForUser(context);
        }
    }

    public static UserPostDao getSharedInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserPostDao(context);
        }

        return mInstance;
    }

    public List<UserPost> getUsersPosts(){

        final List<UserPost> _posts = new ArrayList<>();

        try {
            final ResultSet results = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(DataBaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string(DbType.USER_POST.toString())))
                .orderBy(Ordering.property("post.date").descending())
                .execute();

            //final ResultSet results = QueryBuilder.select(SelectResult.all())
                    //.from(DataSource.database(DataBaseManager.getDatabase()))
                    //.execute();

            for (Result result : results.allResults()) {
                final Dictionary dic = result.getDictionary("koombeadb");
                final String uid = dic.getString("uid");
                final String email = dic.getString("email");
                final String name = dic.getString("name");
                final String profilePic = dic.getString("profile_pic");
                final Dictionary postPics = dic.getDictionary("post");

                UserPost userPost = new UserPost();
                userPost.setUid(uid);
                userPost.setName(name);
                userPost.setEmail(email);
                userPost.setProfilePic(profilePic);

                if (postPics != null) {
                    final int id = postPics.getInt("id");
                    final Date date = postPics.getDate("date");
                    final Array pics = postPics.getArray("pics");
                    Post post = new Post();
                    post.setId(id);
                    post.setDate(date);
                    ArrayList<String> _pics = new ArrayList<>();
                    for (Object pic: pics) {
                        _pics.add(pic.toString());
                    }

                    post.setPics(_pics);
                    userPost.setPost(post);
                }

                _posts.add(userPost);
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return _posts;
    }

}
