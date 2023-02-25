package c3phal0p0d.projects.quizzical.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody String userJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        // convert input from json to User
        User user = null;
        try {
            user = objectMapper.readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (user.getPassword().length()<5 || !user.getUsername().matches(emailRegex)){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setId(userRepository.count());

        if (userRepository.findUserByUsername(user.getUsername()).isPresent()){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}