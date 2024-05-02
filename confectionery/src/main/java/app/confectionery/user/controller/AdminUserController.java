package app.confectionery.user.controller;

import app.confectionery.user.model.UserSummaryDTO;
import app.confectionery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserSummaryDTO>> getUserSummaries() {
        List<UserSummaryDTO> userSummaries = userService.getAllUserSummaries();
        return ResponseEntity.ok(userSummaries);
    }

}
