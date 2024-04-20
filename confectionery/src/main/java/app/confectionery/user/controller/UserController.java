package app.confectionery.user.controller;

import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.user.model.UserUpdateInfoDTO;
import app.confectionery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/profile-info/{id}")
    public ResponseEntity<AuthenticationResponse> updateProfileInfo(@PathVariable UUID id, @Valid @RequestBody UserUpdateInfoDTO userUpdateInfoDTO) {
        AuthenticationResponse authenticationResponse = userService.updateUserProfileAndGenerateToken(id, userUpdateInfoDTO);

        return ResponseEntity.ok(authenticationResponse);
    }


}
