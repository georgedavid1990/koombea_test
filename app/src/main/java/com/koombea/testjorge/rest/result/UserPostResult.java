package com.koombea.testjorge.rest.result;

public class UserPostResult {

    private String uid;
    private String name;
    private String email;
    private String profile_pic;
    private PostResult post;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public PostResult getPost() {
        return post;
    }

    public void setPost(PostResult post) {
        this.post = post;
    }
}
