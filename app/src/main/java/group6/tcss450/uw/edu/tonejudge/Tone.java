package group6.tcss450.uw.edu.tonejudge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Tone {

    anger("Anger", R.color.very_light_red, R.string.anger_definition),
    disgust("Disgust", R.color.very_light_green, R.string.disgust_definition),
    fear("Fear", R.color.very_light_grey, R.string.fear_definition),
    joy("Joy", R.color.very_light_cyan, R.string.joy_definition),
    sadness("Sadness", R.color.very_light_yellow, R.string.sadness_definition),

    analytical("Analytical", R.color.very_light_orange, R.string.analytic_definition),
    confident("Confident", R.color.very_light_cyan, R.string.confidence_definition),
    tentative("Tentative", R.color.very_light_purple, R.string.tentative_definition),

    openness_big5("Openness", R.color.very_light_purple, R.string.openness_definition),
    conscientiousness_big5("Conscientiousness", R.color.very_light_green, R.string.conscientiousness_definition),
    extraversion_big5("Extraversion", R.color.very_light_yellow, R.string.extraversion_definition),
    agreeableness_big5("Agreeableness", R.color.very_light_blue, R.string.agreeableness_definition),
    emotional_range_big5("Emotional Range", R.color.very_light_red, R.string.emotional_range_definition);

    private String name;
    private int definitionId;
    private int colorId;

    public String getName() {
        return name;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public int getColorId() {
        return colorId;
    }

    public String getId() {
        return name();
    }

    Tone(String name, int colorId, int definitionId) {
        this.name = name;
        this.colorId = colorId;
        this.definitionId = definitionId;
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
