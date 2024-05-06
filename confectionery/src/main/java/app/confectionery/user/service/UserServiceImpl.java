package app.confectionery.user.service;

import app.confectionery.order.repository.OrderRepository;
import app.confectionery.user.model.AccountStatus;
import app.confectionery.user.model.UserSummaryDTO;
import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.configuration.jwt.JwtService;
import app.confectionery.user.model.User;
import app.confectionery.user.model.UserUpdateInfoDTO;
import app.confectionery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(\\d{3}-?){3}$";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OrderRepository orderRepository;



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

    //@Transactional
    public List<UserSummaryDTO> getAllUserSummaries() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            long orderCount = orderRepository.countByUserId(user.getId());
            String accountStatus = Optional.ofNullable(user.getAccountStatus())
                    .map(AccountStatus::name)
                    .orElse("UNKNOWN");  // Default to "UNKNOWN" or any other fallback value

            return new UserSummaryDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getBalance(),
                    (int) orderCount,
                    accountStatus
            );
        }).collect(Collectors.toList());
    }

    @Override
    public UserSummaryDTO updateUserStatus(UUID userId, AccountStatus accountStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        log.debug("Updating status for user: {} to {}", user.getId(), accountStatus);
        user.setAccountStatus(accountStatus);
        long orderCount = orderRepository.countByUserId(user.getId());

        try {
            userRepository.save(user);
        } catch (RuntimeException ex) {
            log.error("Error updating user status: {}", ex.getMessage(), ex);
            throw ex;
        }

        return new UserSummaryDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBalance(),
                (int) orderCount,
                user.getAccountStatus().name());
    }


}
