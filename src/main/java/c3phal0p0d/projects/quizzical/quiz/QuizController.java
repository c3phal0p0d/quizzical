package c3phal0p0d.projects.quizzical.quiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
public class QuizController {
    private final ArrayList<Quiz> quizzes = new ArrayList<Quiz>();

    @PostMapping("/api/quizzes")
    public ResponseEntity<String> createQuiz(@RequestBody String quizJson){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);

        // create new quiz from input
        @Valid Quiz quiz;
        try {
            quiz = objectMapper.readValue(quizJson, Quiz.class);

            if (quiz.getTitle()==null || quiz.getTitle()=="" || quiz.getText()==null || quiz.getText()=="" || quiz.getOptions()==null || quiz.getOptions().length<2){
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }

            quiz.setId(quizzes.size());
            quizzes.add(quiz);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        // return new quiz
        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(quiz), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return null;
        }

    }

    @GetMapping("/api/quizzes")
    public ArrayList<Quiz> getQuizzes(){
        return quizzes;
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuiz(@PathVariable int id) {
        ObjectMapper objectMapper = new ObjectMapper();

        // return specified quiz
        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(quizzes.get(id)), HttpStatus.OK);
        } catch (IndexOutOfBoundsException e){
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<String> postResponse(@PathVariable int id, @RequestBody String quizAnswerJson){
        ObjectMapper objectMapper = new ObjectMapper();

        // get specified quiz
        Quiz quiz;
        try {
            quiz = quizzes.get(id);
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

        // get quiz answer input
        QuizAnswer quizAnswer = null;
        try {
            quizAnswer = objectMapper.readValue(quizAnswerJson, QuizAnswer.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        // check if input answer is equal to correct answer
        boolean success = Arrays.equals(quizAnswer.getAnswer(), quiz.getAnswer());
        String feedback = success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";

        Response response = new Response(success, feedback);

        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}