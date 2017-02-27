package group6.tcss450.uw.edu.tonejudge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Tone {

    anger("Anger", R.color.very_light_red),
    disgust("Disgust", R.color.very_light_green),
    fear("Fear", R.color.very_light_grey),
    joy("Joy", R.color.very_light_cyan),
    sadness("Sadness", R.color.very_light_yellow),

    analytical("Analytical", R.color.very_light_orange),
    confident("Confident", R.color.very_light_cyan),
    tentative("Tentative", R.color.very_light_purple),

    openness_big5("Openness", R.color.very_light_purple),
    conscientiousness_big5("Conscientiousness", R.color.very_light_green),
    extraversion_big5("Extraversion", R.color.very_light_yellow),
    agreeableness_big5("Agreeableness", R.color.very_light_blue),
    emotional_range_big5("Emotional Range", R.color.very_light_red);

    private String name;

    private int colorId;

    public String getName() {
        return name;
    }

    public int getColorId() {
        return colorId;
    }

    public String getId() {
        return name();
    }

    Tone(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
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
