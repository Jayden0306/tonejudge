package group6.tcss450.uw.edu.tonejudge;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is convert tone model's data into the JSON string
 * so the result activity can recognize the data from database
 * and able to make request to the IBM web service.
 * Created by Jayden on 3/5/17.
 */

public class ConvertToJSON {
    /**
     * the tone model
     */
    private ToneModel mTone;
    /**
     * a list of the key name for the database
     */
    private String[] mKeyArray;
    /**
     * the json object of the final part JSON object string
     */
    private JSONObject mToneJSONObject;

    /**
     * initialize the final JSON Object that match IBM's tone analyzer's JSON file's format
     * @param tone the tone model object
     * @param context the current activity context
     */
    public ConvertToJSON(ToneModel tone, Context context) {
        this.mTone = tone;
        mKeyArray = context.getResources().getStringArray(R.array.JSON_KEY_NAMES);

        mToneJSONObject = new JSONObject();
        try {
            mToneJSONObject.put("tone_categories", createToneCategoryArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the final JSON object
     */
    public JSONObject getToneJSONObject() {
        return mToneJSONObject;
    }


    /**
     * add category id and name with corresponding tone category JSONArray
     * into corresponding tone category JSONArray
     * @return
     */
    public JSONArray createToneCategoryArray() {
        JSONObject emotionCategory = new JSONObject();
        JSONObject languageCategory = new JSONObject();
        JSONObject socialCategory = new JSONObject();
        try {
            emotionCategory.put(mKeyArray[19], createEmotionArray());
            emotionCategory.put(mKeyArray[20], mKeyArray[21]);
            addCategory(emotionCategory, mKeyArray[16]);

            languageCategory.put(mKeyArray[19], createLanguageArray());
            languageCategory.put(mKeyArray[20], mKeyArray[21]);
            addCategory(languageCategory, mKeyArray[17]);

            socialCategory.put(mKeyArray[19], createSocialArray());
            socialCategory.put(mKeyArray[20], mKeyArray[22]);
            addCategory(socialCategory, mKeyArray[18]);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray toneCategoryArray = new JSONArray();
        toneCategoryArray.put(emotionCategory);
        toneCategoryArray.put(languageCategory);
        toneCategoryArray.put(socialCategory);

        return toneCategoryArray;
    }

    /**
     * add each attribute JSON object into the emotion array
     * @return emotion array
     */
    public JSONArray createEmotionArray() {
        JSONObject anger = new JSONObject();
        addElement(anger, mTone.getAnger(), mKeyArray[2]);
        JSONObject disgust= new JSONObject();
        addElement(disgust, mTone.getDisgust(), mKeyArray[3]);
        JSONObject fear= new JSONObject();
        addElement(fear, mTone.getFear(), mKeyArray[4]);
        JSONObject joy= new JSONObject();
        addElement(joy, mTone.getJoy(), mKeyArray[5]);
        JSONObject sadness= new JSONObject();
        addElement(sadness, mTone.getSadness(), mKeyArray[6]);

        JSONArray emotionArray = new JSONArray();
        emotionArray.put(anger);
        emotionArray.put(disgust);
        emotionArray.put(fear);
        emotionArray.put(joy);
        emotionArray.put(sadness);

        return emotionArray;
    }

    /**
     * add each attribute JSON object into the language style array
     * @return the language style array
     */
    public JSONArray createLanguageArray() {
        JSONObject analytical= new JSONObject();
        addElement(analytical, mTone.getAnalytical(), mKeyArray[7]);
        JSONObject confident= new JSONObject();
        addElement(confident, mTone.getConfident(), mKeyArray[8]);
        JSONObject tentative = new JSONObject();
        addElement(tentative, mTone.getTentative(), mKeyArray[9]);

        JSONArray languageArray = new JSONArray();
        languageArray.put(analytical);
        languageArray.put(confident);
        languageArray.put(tentative);

        return languageArray;
    }

    /**
     * add each attribute JSON object into the social array
     * @return social array
     */
    public JSONArray createSocialArray() {
        JSONObject openness= new JSONObject();
        addElement(openness, mTone.getOpenness(), mKeyArray[10]);
        JSONObject conscientiousness= new JSONObject();
        addElement(conscientiousness, mTone.getConscientiousness(), mKeyArray[11]);
        JSONObject extraversion= new JSONObject();
        addElement(extraversion, mTone.getExtraversion(), mKeyArray[12]);
        JSONObject agreeableness= new JSONObject();
        addElement(agreeableness, mTone.getAgreeableness(), mKeyArray[13]);
        JSONObject emotionalRange= new JSONObject();
        addElement(emotionalRange, mTone.getEmotionalRange(), mKeyArray[14]);

        JSONArray socialArray = new JSONArray();
        socialArray.put(openness);
        socialArray.put(conscientiousness);
        socialArray.put(extraversion);
        socialArray.put(agreeableness);
        socialArray.put(emotionalRange);

        return socialArray;
    }

    /**
     * add score and name to the JSON object
     * @param json the json object
     * @param score the score of the attribute
     * @param toneName the name of the tone
     */
    public void addElement(JSONObject json, String score, String toneName) {
        try {
            json.put(mKeyArray[0], score);
            json.put(mKeyArray[1], toneName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * add tone category name into the JSON object
     * @param json the JSON object
     * @param categoryName the category name
     */
    public void addCategory(JSONObject json, String categoryName) {
        try {
            json.put(mKeyArray[15], categoryName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
