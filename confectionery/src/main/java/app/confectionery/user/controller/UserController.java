package app.confectionery.user.controller;

import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.user.model.DTO.UserUpdateInfoDTO;
import app.confectionery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/profile-info")
    public ResponseEntity<AuthenticationResponse> updateProfileInfo(@PathVariable UUID id, @Valid @RequestBody UserUpdateInfoDTO userUpdateInfoDTO) {
        AuthenticationResponse authenticationResponse = userService.updateUserProfileAndGenerateToken(id, userUpdateInfoDTO);

        return ResponseEntity.ok(authenticationResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
