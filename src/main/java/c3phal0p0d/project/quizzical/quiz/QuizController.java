package c3phal0p0d.project.quizzical.quiz;

import c3phal0p0d.project.quizzical.user.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class QuizController {
    private QuizRepository quizRepository;

    public QuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostMapping("/api/quizzes")
    public ResponseEntity<String> createQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String quizJson){
        ObjectMapper objectMapper = new ObjectMapper();

        // create new quiz from input
        Quiz quiz;
        try {
            quiz = objectMapper.readValue(quizJson, Quiz.class);

            // check quiz is valid
            if (quiz.getTitle()==null || quiz.getTitle()=="" || quiz.getText()==null || quiz.getText()=="" || quiz.getOptions()==null || quiz.getOptions().size()<2){
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }

            quiz.setId(quizRepository.count());
            quiz.setCreator(userDetails.getUser());
            quizRepository.save(quiz);
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
        return (ArrayList<Quiz>) quizRepository.findAll();
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuiz(@PathVariable Long id) {
        ObjectMapper objectMapper = new ObjectMapper();

        // return specified quiz
        try {
            Quiz quiz = quizRepository.findById(id).orElse(null);
            if (quiz!=null){
                return new ResponseEntity<>(objectMapper.writeValueAsString(quiz), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<String> postResponse(@PathVariable Long id, @RequestBody String quizAnswerJson){
        ObjectMapper objectMapper = new ObjectMapper();

        // get specified quiz
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz==null) {
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
        boolean success = quizAnswer.getAnswer().equals(quiz.getAnswer());
        String feedback = success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";

        Response response = new Response(success, feedback);

        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        ObjectMapper objectMapper = new ObjectMapper();

        // return specified quiz
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz!=null){
            if (!quiz.getCreator().getUsername().equals(userDetails.getUser().getUsername())){
                return new ResponseEntity<>("user: " + userDetails.getUser() + " creator: " + quiz.getCreator(), HttpStatus.FORBIDDEN);
            }
            quizRepository.delete(quiz);
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/actuator/shutdown")
    public void shutdown() {

    }
}
