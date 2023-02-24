package c3phal0p0d.projects.quizzical.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;

public class QuizAnswer {
    @NotNull
    private int[] answer;

    public QuizAnswer(@JsonProperty("answer") int[] answer) {
        this.answer = answer;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }
}