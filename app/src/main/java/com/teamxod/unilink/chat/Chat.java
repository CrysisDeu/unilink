package com.teamxod.unilink.chat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class Chat {
    private String mName;
    private String mMessage;
    private String mUid;
    private long mTimestamp;

    public Chat() {
        // Needed for Firebase
    }

    Chat(String mName, String mMessage, String mUid, long timestamp) {
        this.mName = mName;
        this.mMessage = mMessage;
        this.mUid = mUid;
        this.mTimestamp = timestamp;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        mName = name;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String message) {
        mMessage = message;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String uid) {
        mUid = uid;
    }

    public long getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
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
