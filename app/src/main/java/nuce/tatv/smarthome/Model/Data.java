package nuce.tatv.smarthome.Model;

public class Data {
    String mTopic;
    String mMessage;

    public Data(String mTopic, String mMessage) {
        this.mTopic = mTopic;
        this.mMessage = mMessage;
    }

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
