package group6.tcss450.uw.edu.tonejudge;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import org.json.JSONException;
import org.json.JSONObject;

public class ElementTones {

    public static JSONObject elementToneToSimpleJson(ElementTone elementTone) {
        JSONObject json = new JSONObject();
        try {
            for (ToneCategory toneCategory : elementTone.getTones()) {
                for (ToneScore toneScore : toneCategory.getTones()) {
                    json.put(toneScore.getId(), toneScore.getScore());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
