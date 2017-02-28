package group6.tcss450.uw.edu.tonejudge;

/**
 * Created by Jayden on 2/28/17.
 */

public class ToneModel {
    private int mID;
    private String mMessage;
    private String mAnger;
    private String mDisgust;
    private String mFear;
    private String mJoy;
    private String mSadness;
    private String mAnalytical;
    private String mConfident;
    private String mTentative;
    private String mOpenness;
    private String mConscientiousness;
    private String mExtraversion;
    private String mAgreeableness;
    private String mEmotionalRange;

    public ToneModel() {

    }

    public ToneModel(int mID, String mMessage, String mAnger, String mDisgust, String mFear,
                     String mJoy, String mSadness, String mAnalytical, String mConfident,
                     String mTentative, String mOpenness, String mConscientiousness,
                     String mExtraversion, String mAgreeableness, String mEmotionalRange ) {
        this.mID = mID;
        this.mMessage = mMessage;
        this.mAnger = mAnger;
        this.mDisgust = mDisgust;
        this.mFear = mFear;
        this.mJoy = mJoy;
        this.mSadness = mSadness;
        this.mAnalytical = mAnalytical;
        this.mConfident = mConfident;
        this.mTentative = mTentative;
        this.mOpenness = mOpenness;
        this.mConscientiousness = mConscientiousness;
        this.mExtraversion = mExtraversion;
        this.mAgreeableness = mAgreeableness;
        this.mEmotionalRange = mEmotionalRange;
    }

    public ToneModel(String mMessage, String mAnger, String mDisgust, String mFear,
                     String mJoy, String mSadness, String mAnalytical, String mConfident,
                     String mTentative, String mOpenness, String mConscientiousness, String mExtraversion,
                     String mAgreeableness, String mEmotionalRange) {
        this.mMessage = mMessage;
        this.mAnger = mAnger;
        this.mDisgust = mDisgust;
        this.mFear = mFear;
        this.mJoy = mJoy;
        this.mSadness = mSadness;
        this.mAnalytical = mAnalytical;
        this.mConfident = mConfident;
        this.mTentative = mTentative;
        this.mOpenness = mOpenness;
        this.mConscientiousness = mConscientiousness;
        this.mExtraversion = mExtraversion;
        this.mAgreeableness = mAgreeableness;
        this.mEmotionalRange = mEmotionalRange;
    }

    public int getID() {
        return mID;
    }

    public String getmMessage() {
        return mMessage;
    }

    public String getAnger() {
        return mAnger;
    }

    public String getDisgust() {
        return mDisgust;
    }

    public String getFear() {
        return mFear;
    }

    public String getJoy() {
        return mJoy;
    }

    public String getSadness() {
        return mSadness;
    }

    public String getAnalytical() {
        return mAnalytical;
    }

    public String getConfident() {
        return mConfident;
    }

    public String getTentative() {
        return mTentative;
    }

    public String getOpenness() {
        return mOpenness;
    }

    public String getConscientiousness() {
        return mConscientiousness;
    }

    public String getExtraversion() {
        return mExtraversion;
    }

    public String getAgreeableness() {
        return mAgreeableness;
    }

    public String getEmotionalRange() {
        return mEmotionalRange;
    }
    //////////////////////////////////////////////////////

    public void setID(int id) {
        this.mID = id;
    }

    public void setmMessage(String message) {
        this.mMessage = message;
    }

    public void setAnger(String anger) {
        this.mAnger = anger;
    }

    public void setDisgust(String disgust) {
        this.mDisgust = disgust;
    }

    public void setFear(String fear) {
        this.mFear = fear;
    }

    public void setJoy(String joy) {
        this.mJoy = joy;
    }

    public void setSadness(String sadness) {
        this.mSadness = sadness;
    }

    public void setAnalytical(String analytical) {
        this.mAnalytical = analytical;
    }

    public void setConfident(String confident) {
        this.mConfident = confident;
    }

    public void setTentative(String tentative) {
        this.mTentative = tentative;
    }

    public void setOpenness(String openness) {
        this.mOpenness = openness;
    }

    public void setConscientiousness(String conscientiousness) {
        this.mConscientiousness = conscientiousness;
    }

    public void setExtraversion(String extraversion) {
        this.mExtraversion = extraversion;
    }

    public void setAgreeableness(String agreeableness) {
        this.mAgreeableness = agreeableness;
    }

    public void setEmotionalRange(String emotionalRange) {
        this.mEmotionalRange = emotionalRange;
    }

}
