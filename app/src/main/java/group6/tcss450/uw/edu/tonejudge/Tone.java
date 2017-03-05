package group6.tcss450.uw.edu.tonejudge;

import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Tone {
    anger("Anger", R.color.very_light_red, R.string.anger_definition,
            R.string.generic_meaning_low, R.string.generic_meaning_high),
    disgust("Disgust", R.color.very_light_green, R.string.disgust_definition,
            R.string.generic_meaning_low, R.string.generic_meaning_high),
    fear("Fear", R.color.very_light_grey, R.string.fear_definition,
            R.string.generic_meaning_low, R.string.generic_meaning_high),
    joy("Joy", R.color.very_light_cyan, R.string.joy_definition,
            R.string.generic_meaning_low, R.string.generic_meaning_high),
    sadness("Sadness", R.color.very_light_yellow, R.string.sadness_definition,
            R.string.generic_meaning_low, R.string.generic_meaning_high),

    analytical("Analytical", R.color.very_light_orange, R.string.analytic_definition,
            R.string.analytical_meaning_low, R.string.analytical_meaning_high),
    confident("Confident", R.color.very_light_cyan, R.string.confidence_definition,
            R.string.confidence_meaning_low, R.string.confidence_meaning_high),
    tentative("Tentative", R.color.very_light_purple, R.string.tentative_definition,
            R.string.tentative_meaning_low, R.string.tentative_meaning_high),

    openness_big5("Openness", R.color.very_light_purple, R.string.openness_definition,
            R.string.openness_meaning_low, R.string.openness_meaning_high),
    conscientiousness_big5("Conscientiousness", R.color.very_light_green, R.string.conscientiousness_definition,
            R.string.conscientiousness_meaning_low, R.string.conscientiousness_meaning_high),
    extraversion_big5("Extraversion", R.color.very_light_yellow, R.string.extraversion_definition,
            R.string.extraversion_meaning_low, R.string.extraversion_meaning_high),
    agreeableness_big5("Agreeableness", R.color.very_light_blue, R.string.agreeableness_definition,
            R.string.agreeableness_meaning_low, R.string.agreeableness_meaning_high),
    emotional_range_big5("Emotional Range", R.color.very_light_red, R.string.emotional_range_definition,
            R.string.emotional_range_meaning_low, R.string.emotional_range_meaning_high);

    private String name;
    private int definitionId;
    private int meaningLowId;
    private int meaningHighId;
    private int colorId;

    public String getName() {
        return name;
    }

    public String getDescription(Context context) {
        String description = context.getString(getDefinitionId());
        description += "\n\n" + context.getString(R.string.low_score_definition) + ": " + context.getString(getMeaningLowId());
        description += "\n\n" + context.getString(R.string.high_score_definition) + ": " + context.getString(getMeaningHighId());
        return description;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public int getMeaningLowId() {
        return meaningLowId;
    }

    public int getMeaningHighId() {
        return meaningHighId;
    }

    public int getColorId() {
        return colorId;
    }

    public String getId() {
        return name();
    }

    Tone(String name, int colorId, int definitionId, int meaningLowId, int meaningHighId) {
        this.name = name;
        this.colorId = colorId;
        this.definitionId = definitionId;
        this.meaningLowId = meaningLowId;
        this.meaningHighId = meaningHighId;
    }

    public enum Category {

        emotion_tone("Emotion Tone", anger, disgust, fear, joy, sadness),
        language_tone("Language Tone", analytical, confident, tentative),
        social_tone("Social Tone", openness_big5, conscientiousness_big5, extraversion_big5, agreeableness_big5, emotional_range_big5);

        private String name;
        private List<Tone> tones;

        public List<Tone> getTones() {
            return tones;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return name();
        }

        Category(String name, Tone... tones) {
            this.name = name;
            this.tones = Collections.unmodifiableList(Arrays.asList(tones));
        }
    }
}
