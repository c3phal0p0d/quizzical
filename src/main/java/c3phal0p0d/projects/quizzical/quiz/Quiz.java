package c3phal0p0d.projects.quizzical.quiz;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;

public class Quiz {
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotNull
    @Size(min=2)
    private String[] options;

    private int[] answer;

    public Quiz(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("options") String[] options,
            @JsonProperty("answer") int[] answer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer == null ? new int[]{} : answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty
    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    @JsonProperty
    public void setText(String text) {
        this.text = text;
    }

    public String[] getOptions() {
        return options;
    }

    @JsonProperty
    public void setOptions(String[] options) {
        this.options = options;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public int[] getAnswer() {
        return answer;
    }

    @JsonProperty
    public void setAnswer(int answer) {
        this.answer = new int[]{answer};
    }
}