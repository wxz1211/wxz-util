package com.github.wxz.common.test;

import java.util.Date;

/**
 * @author xianzhi.wang
 * @date 2018/4/20 -14:05
 */
public class UserFollowing {
    /**
     * srcType
     */
    private String srcType;
    /**
     * uid
     */
    private String uid;
    /**
     * followingUID
     */
    private String followingUID;

    private Date optDateTime;

    private long mongoUpdateTimestamp;

    private boolean delete;

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFollowingUID() {
        return followingUID;
    }

    public void setFollowingUID(String followingUID) {
        this.followingUID = followingUID;
    }

    public Date getOptDateTime() {
        return optDateTime;
    }

    public void setOptDateTime(Date optDateTime) {
        this.optDateTime = optDateTime;
    }

    public long getMongoUpdateTimestamp() {
        return mongoUpdateTimestamp;
    }

    public void setMongoUpdateTimestamp(long mongoUpdateTimestamp) {
        this.mongoUpdateTimestamp = mongoUpdateTimestamp;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
