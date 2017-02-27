package group6.tcss450.uw.edu.tonejudge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Tone {

    anger("Anger"),
    disgust("Disgust"),
    fear("Fear"),
    joy("Joy"),
    sadness("Sadness"),

    analytical("Analytical"),
    confident("Confident"),
    tentative("Tentative"),

    openness_big5("Openness"),
    conscientiousness_big5("Conscientiousness"),
    extraversion_big5("Extraversion"),
    agreeableness_big5("Agreeableness"),
    emotional_range_big5("Emotional Range");

    private String name;

    public String getName() {
        return name;
    }

    public String getId() {
        return name();
    }

    Tone(String name) {
        this.name = name;
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
