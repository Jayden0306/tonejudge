package group6.tcss450.uw.edu.tonejudge.server;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import group6.tcss450.uw.edu.tonejudge.model.Tone;

/**
 * Utility class for manipulating data between the Watson API and the ToneJudge API.
 *
 * @author Hunter Bennett
 */
public class ElementTones {

    /**
     * 0.984534 -> "98.4"
     * 0.33000 -> "33"
     *
     * @param score from API
     * @return score formatted as a percentage with one digit past the decimal or none if unneeded.
     */
    public static String scoreToString(double score) {
        BigDecimal bd = BigDecimal.valueOf(score);
        bd = bd.multiply(BigDecimal.valueOf(100));
        bd = bd.setScale(1, BigDecimal.ROUND_DOWN);
        try {
            bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
        } catch (ArithmeticException e) {

        }
        return bd.toPlainString();
    }

    /**
     *
     * @param elementTone from Watson API
     * @return JSON compatible with ToneJudge API
     */
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

    /**
     *
     * @param json from ToneJudge API
     * @return ElementTone compatible with Watson API
     */
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
