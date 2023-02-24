package c3phal0p0d.projects.quizzical.quiz;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Quiz {
    @Id
    private Integer id;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @NotEmpty
    @Column(nullable = false)
    private String text;

    @NotNull
    @Size(min=2)
    @Column(nullable = false)
    @ElementCollection
    private List<String> options;

    @Column
    @ElementCollection
    private List<Integer> answer;

    protected Quiz(){}

    public Quiz(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("options") String[] options,
            @JsonProperty("answer") Integer[] answer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = Arrays.asList(options);
        this.answer = answer == null ? List.of() : Arrays.asList(answer);
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

    public @NotNull @Size(min = 2) List<String> getOptions() {
        return options;
    }

    @JsonProperty
    public void setOptions(String[] options) {
        this.options = Arrays.asList(options);
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Integer> getAnswer() {
        return answer;
    }

    @JsonProperty
    public void setAnswer(int answer) {
        this.answer = Arrays.asList(answer);
    }
}