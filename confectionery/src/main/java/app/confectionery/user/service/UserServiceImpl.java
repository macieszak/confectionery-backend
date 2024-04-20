package app.confectionery.user.service;

import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.configuration.jwt.JwtService;
import app.confectionery.user.model.User;
import app.confectionery.user.model.UserUpdateInfoDTO;
import app.confectionery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(\\d{3}-?){3}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;



    @Override
    public AuthenticationResponse updateUserProfileAndGenerateToken(UUID id, UserUpdateInfoDTO userUpdateInfoDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the new email is different from the current and already taken by another user
        Optional<User> existingUserWithNewEmail = userRepository.findByEmail(userUpdateInfoDTO.getEmail());
        if (existingUserWithNewEmail.isPresent() && !existingUserWithNewEmail.get().getId().equals(user.getId())) {
            throw new IllegalStateException("Email already in use. Please use a different email.");
        }

        User updatedUser = updateUserProfile(id, userUpdateInfoDTO);
        String newToken = generateTokenForUser(updatedUser);

        return AuthenticationResponse.builder()
                .accessToken(newToken)
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .phoneNumber(updatedUser.getPhoneNumber())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .build();
    }

    @Override
    public User updateUserProfile(UUID id, UserUpdateInfoDTO userUpdateInfoDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFirstName(userUpdateInfoDTO.getFirstName());
        user.setLastName(userUpdateInfoDTO.getLastName());
        // Validate phone number if it is not null or empty
        if (userUpdateInfoDTO.getPhoneNumber() != null && !userUpdateInfoDTO.getPhoneNumber().isEmpty()) {
            if (!Pattern.matches(PHONE_REGEX, userUpdateInfoDTO.getPhoneNumber())) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
        user.setPhoneNumber(userUpdateInfoDTO.getPhoneNumber());  // Set the phone number whether it is valid or not (empty is allowed)
        user.setEmail(userUpdateInfoDTO.getEmail());
        // Check password if it is not null, empty and at least 8 characters long
        if (userUpdateInfoDTO.getPassword() != null && !userUpdateInfoDTO.getPassword().isEmpty()) {
            if (userUpdateInfoDTO.getPassword().length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters long");
            }
            user.setPassword(passwordEncoder.encode(userUpdateInfoDTO.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public String generateTokenForUser(User updatedUser) {
        UserDetails userDetails = this.loadUserByUsername(updatedUser.getEmail());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + username));
    }


}
