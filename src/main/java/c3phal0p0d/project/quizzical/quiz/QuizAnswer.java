package c3phal0p0d.project.quizzical.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.*;

import java.util.Arrays;
import java.util.List;

public class QuizAnswer {
    @NotNull
    private List<Integer> answer;

    public QuizAnswer(@JsonProperty("answer") Integer[] answer) {
        this.answer = Arrays.asList(answer);
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(Integer[] answer) {
        this.answer = Arrays.asList(answer);
    }
}