package app.confectionery.user.service;

import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.user.model.User;
import app.confectionery.user.model.UserSummaryDTO;
import app.confectionery.user.model.UserUpdateInfoDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService {

    AuthenticationResponse updateUserProfileAndGenerateToken(UUID id, UserUpdateInfoDTO userUpdateInfoDTO);
    User updateUserProfile(UUID id, UserUpdateInfoDTO userUpdateInfoDTO);
    String generateTokenForUser(User updatedUser);
    UserDetails loadUserByUsername(String username);
    List<UserSummaryDTO> getAllUserSummaries();
}
