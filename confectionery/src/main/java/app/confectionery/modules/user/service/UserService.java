package app.confectionery.modules.user.service;

import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.modules.user.model.AccountStatus;
import app.confectionery.modules.user.model.User;
import app.confectionery.modules.user.model.DTO.UserSummaryDTO;
import app.confectionery.modules.user.model.DTO.UserUpdateInfoDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserSummaryDTO> getAllUserSummaries();
    User updateUserProfile(UUID id, UserUpdateInfoDTO userUpdateInfoDTO);
    UserSummaryDTO updateUserStatus(UUID userId, AccountStatus accountStatus);
    UserDetails loadUserByUsername(String username);
    AuthenticationResponse updateUserProfileAndGenerateToken(UUID id, UserUpdateInfoDTO userUpdateInfoDTO);
    String generateTokenForUser(User updatedUser);
    void deleteUser(UUID userId);
}
