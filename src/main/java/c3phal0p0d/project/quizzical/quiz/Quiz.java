package c3phal0p0d.project.quizzical.quiz;

import c3phal0p0d.project.quizzical.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Arrays;
import java.util.List;

@Entity
public class Quiz {
    @Id
    private Long quizId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User creator;

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
            @JsonProperty("id") Long quizId,
            @JsonProperty("creator") User creator,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("options") String[] options,
            @JsonProperty("answer") Integer[] answer) {
        this.quizId = quizId;
        this.creator = creator;
        this.title = title;
        this.text = text;
        this.options = Arrays.asList(options);
        this.answer = answer == null ? List.of() : Arrays.asList(answer);
    }

    public Long getId() {
        return quizId;
    }

    public void setId(Long id) {
        this.quizId = id;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator){
        this.creator = creator;
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
        this.answer = List.of(answer);
    }
}
