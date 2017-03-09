package group6.tcss450.uw.edu.tonejudge;

/**
 * This is the tone model class with the property from emotion, language style, and social tendencies.
 * It is use for the database. It is easier to manage result's data and store in the local database.
 * Created by Jayden on 2/28/17.
 */

public class ToneModel {
    /**
     * the unique ID as primary key for database
     */
    private long mID;
    /**
     * the message from the user enter
     */
    private String mMessage;
    /**
     * the attribute from emotion category
     */
    private String mAnger;
    /**
     * the attribute from emotion category
     */
    private String mDisgust;
    /**
     * the attribute from emotion category
     */
    private String mFear;
    /**
     * the attribute from emotion category
     */
    private String mJoy;
    /**
     * the attribute from emotion category
     */
    private String mSadness;
    /**
     * the attribute from language style category
     */
    private String mAnalytical;
    /**
     * the attribute from language style category
     */
    private String mConfident;
    /**
     * the attribute from language style category
     */
    private String mTentative;
    /**
     * the attribute from social tendencies category
     */
    private String mOpenness;
    /**
     * the attribute from social tendencies category
     */
    private String mConscientiousness;
    /**
     * the attribute from social tendencies category
     */
    private String mExtraversion;
    /**
     * the attribute from social tendencies category
     */
    private String mAgreeableness;
    /**
     * the attribute from social tendencies category
     */
    private String mEmotionalRange;

    /**
     * this is the empty constructor for create tone model object
     */
    public ToneModel() {

    }

    /**
     * this is the constructor with different attributes for one result to create the tone model object
     * @param mID the id
     * @param mMessage the user message
     * @param mAnger the attribute from emotion category
     * @param mDisgust the attribute from emotion category
     * @param mFear the attribute from emotion category
     * @param mJoy the attribute from emotion category
     * @param mSadness the attribute from emotion category
     * @param mAnalytical the attribute from language style category
     * @param mConfident the attribute from language style category
     * @param mTentative the attribute from language style category
     * @param mOpenness the attribute from social tendencies category
     * @param mConscientiousness the attribute from social tendencies category
     * @param mExtraversion the attribute from social tendencies category
     * @param mAgreeableness the attribute from social tendencies category
     * @param mEmotionalRange the attribute from social tendencies category
     */
    public ToneModel(long mID, String mMessage, String mAnger, String mDisgust, String mFear,
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


    /**
     * return id
     * @return id
     */
    public long getID() {
        return mID;
    }

    /**
     * return message
     * @return message
     */
    public String getmMessage() {
        return mMessage;
    }

    /**
     * return the attribute from emotion category
     * @return the attribute from emotion category
     */
    public String getAnger() {
        return mAnger;
    }

    /**
     * return the attribute from emotion category
     * @return the attribute from emotion category
     */
    public String getDisgust() {
        return mDisgust;
    }

    /**
     * return the attribute from emotion category
     * @return the attribute from emotion category
     */
    public String getFear() {
        return mFear;
    }

    /**
     * return the attribute from emotion category
     * @return the attribute from emotion category
     */
    public String getJoy() {
        return mJoy;
    }

    /**
     * return the attribute from emotion category
     * @return the attribute from emotion category
     */
    public String getSadness() {
        return mSadness;
    }

    /**
     * return the attribute from language style category
     * @return the attribute from language style category
     */
    public String getAnalytical() {
        return mAnalytical;
    }

    /**
     * return the attribute from language style category
     * @return the attribute from language style category
     */
    public String getConfident() {
        return mConfident;
    }

    /**
     * return the attribute from language style category
     * @return the attribute from language style category
     */
    public String getTentative() {
        return mTentative;
    }

    /**
     * return the attribute from social tendencies category
     * @return the attribute from social tendencies category
     */
    public String getOpenness() {
        return mOpenness;
    }

    /**
     * return the attribute from social tendencies category
     * @return the attribute from social tendencies category
     */
    public String getConscientiousness() {
        return mConscientiousness;
    }

    /**
     * return the attribute from social tendencies category
     * @return the attribute from social tendencies category
     */
    public String getExtraversion() {
        return mExtraversion;
    }

    /**
     * return the attribute from social tendencies category
     * @return the attribute from social tendencies category
     */
    public String getAgreeableness() {
        return mAgreeableness;
    }

    /**
     * return the attribute from social tendencies category
     * @return the attribute from social tendencies category
     */
    public String getEmotionalRange() {
        return mEmotionalRange;
    }


    /**
     * set the value of the id
    * @param id the value of id
     */
    public void setID(long id) {
        this.mID = id;
    }

    /**
     * set the string the message user enter
     * @param message the message the user enter
     */
    public void setmMessage(String message) {
        this.mMessage = message;
    }

    /**
     * set the value of the attribute from emotion category
     * @param anger the value of the attribute from emotion category
     */
    public void setAnger(String anger) {
        this.mAnger = anger;
    }

    /**
     * set the value of the attribute from emotion category
     * @param disgust the value of the attribute from emotion category
     */
    public void setDisgust(String disgust) {
        this.mDisgust = disgust;
    }

    /**
     * set the value of the attribute from emotion category
     * @param fear the value of the attribute from emotion category
     */
    public void setFear(String fear) {
        this.mFear = fear;
    }

    /**
     * set the value of the attribute from emotion category
     * @param joy the value of the attribute from emotion category
     */
    public void setJoy(String joy) {
        this.mJoy = joy;
    }

    /**
     * set the value of the attribute from emotion category
     * @param sadness the value of the attribute from emotion category
     */
    public void setSadness(String sadness) {
        this.mSadness = sadness;
    }

    /**
     * set the value of the attribute from language category
     * @param analytical the value of the attribute from language category
     */
    public void setAnalytical(String analytical) {
        this.mAnalytical = analytical;
    }

    /**
     * set the value of the attribute from language category
     * @param confident the value of the attribute from language category
     */
    public void setConfident(String confident) {
        this.mConfident = confident;
    }

    /**
     * set the value of the attribute from language category
     * @param tentative the value of the attribute from language category
     */
    public void setTentative(String tentative) {
        this.mTentative = tentative;
    }

    /**
     * set the value of the attribute from social tendencies category
     * @param openness the value of the attribute from social tendencies category
     */
    public void setOpenness(String openness) {
        this.mOpenness = openness;
    }

    /**
     * set the value of the attribute from social tendencies category
     * @param conscientiousness the value of the attribute from social tendencies category
     */
    public void setConscientiousness(String conscientiousness) {
        this.mConscientiousness = conscientiousness;
    }

    /**
     * set the value of the attribute from social tendencies category
     * @param extraversion the value of the attribute from social tendencies category
     */
    public void setExtraversion(String extraversion) {
        this.mExtraversion = extraversion;
    }

    /**
     * set the value of the attribute from social tendencies category
     * @param agreeableness the value of the attribute from social tendencies category
     */
    public void setAgreeableness(String agreeableness) {
        this.mAgreeableness = agreeableness;
    }

    /**
     * set the value of the attribute from social tendencies category
     * @param emotionalRange the value of the attribute from social tendencies category
     */
    public void setEmotionalRange(String emotionalRange) {
        this.mEmotionalRange = emotionalRange;
    }

}
