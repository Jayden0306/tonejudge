package group6.tcss450.uw.edu.tonejudge;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElementTones {

    public static JSONObject elementToneToDbJson(ElementTone elementTone) {
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

    public static ElementTone dbJsonToElementTone(JSONObject json) {
        ElementTone elementTone = new ElementTone();
        try {
            List<ToneCategory> toneCategories = new ArrayList<>();
            for (Tone.Category category : Tone.Category.values()) {
                ToneCategory toneCategory = new ToneCategory();
                toneCategory.setName(category.getName());
                toneCategory.setId(category.getId());
                List<ToneScore> tones = new ArrayList<>();
                for (Tone tone : category.getTones()) {
                    ToneScore toneScore = new ToneScore();
                    toneScore.setScore(json.getDouble(tone.getId()));
                    toneScore.setId(tone.getId());
                    toneScore.setName(tone.getName());
                    tones.add(toneScore);
                }
                toneCategory.setTones(tones);
                toneCategories.add(toneCategory);
            }
            elementTone.setTones(toneCategories);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return elementTone;
    }
}
