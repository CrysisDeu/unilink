package com.teamxod.unilink;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat extends AbstractChat {
    private String mName;
    private String mMessage;
    private String mUid;
    private long mTimestamp;

    public Chat() {
        // Needed for Firebase
    }

    public Chat(String name, String message, String uid, long timestamp) {
        mName = name;
        mMessage = message;
        mUid = uid;
        mTimestamp = timestamp;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public void setmTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public long getmTimestamp() {
        return mTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return mUid.equals(chat.mUid)
                && (mName == null ? chat.mName == null : mName.equals(chat.mName))
                && (mMessage == null ? chat.mMessage == null : mMessage.equals(chat.mMessage));
    }

    @Override
    public int hashCode() {
        int result = mName == null ? 0 : mName.hashCode();
        result = 31 * result + (mMessage == null ? 0 : mMessage.hashCode());
        result = 31 * result + mUid.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "mName='" + mName + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mUid='" + mUid + '\'' +
                '}';
    }
}
