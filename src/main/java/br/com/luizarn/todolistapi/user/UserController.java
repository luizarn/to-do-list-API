package br.com.luizarn.todolistapi.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ISessionRepository sessionRepository;

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByemail(userModel.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

    // @PostMapping("/login")
    // public ResponseEntity login(@RequestBody LoginModel loginModel) {
    // var user = this.userRepository.findByemail(loginModel.getEmail());
    // if (user == null) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    // }

    // var passwordVerify =
    // BCrypt.verifyer().verify(loginModel.getPassword().toCharArray(),
    // user.getPassword());
    // if (!passwordVerify.verified) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
    // password");
    // }

    // String token = generateToken();
    // var session = this.sessionRepository.findFirstByUser(user.getId());
    // if (session == null) {
    // session = new SessionModel();
    // session.set(user);
    // session.setToken(token);
    // this.sessionRepository.save(session);
    // } else {
    // session.setToken(token);
    // this.sessionRepository.save(session);
    // }

    // return ResponseEntity.status(HttpStatus.OK).body(token);
    // }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginModel loginModel) {
        var user = this.userRepository.findByemail(loginModel.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var passwordVerify = BCrypt.verifyer().verify(loginModel.getPassword().toCharArray(), user.getPassword());
        if (!passwordVerify.verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = generateToken();
        List<SessionModel> sessions = this.sessionRepository.findFirstByUser_Id(user.getId());
        SessionModel session = null;
        if (sessions.isEmpty()) {
            session = new SessionModel();
            session.setUser(user);
            session.setToken(token);
            this.sessionRepository.save(session);
        } else {
            session = sessions.get(0);
            session.setToken(token);
            this.sessionRepository.save(session);
        }

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
