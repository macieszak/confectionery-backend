package app.confectionery.authorization.controller;

import app.confectionery.user.model.User;
import app.confectionery.authorization.model.UserRegistrationDto;
import app.confectionery.authorization.service.RegistrationUserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthorizationController {

    private final RegistrationUserServiceImpl userService;

    @Autowired
    public AuthorizationController(RegistrationUserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User user = userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok(user);
    }

}
