package br.com.luizarn.todolistapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {

        var user = this.userRepository.findByemail(userModel.getEmail());
        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
 
        var passwordHashred = BCrypt.withDefaults()
        .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

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
    
       return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
