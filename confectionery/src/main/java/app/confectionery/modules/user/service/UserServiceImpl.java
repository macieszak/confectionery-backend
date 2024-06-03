package app.confectionery.modules.user.service;

import app.confectionery.modules.address.model.Address;
import app.confectionery.modules.address.repository.AddressRepository;
import app.confectionery.modules.cart.model.ShoppingCart;
import app.confectionery.modules.cart.repository.ShoppingCartRepository;
import app.confectionery.modules.cart_item.model.CartItem;
import app.confectionery.modules.cart_item.repository.CartItemRepository;
import app.confectionery.modules.favorite_products.model.Favorite;
import app.confectionery.modules.favorite_products.repository.FavoriteRepository;
import app.confectionery.modules.order.repository.OrderRepository;
import app.confectionery.modules.user.model.AccountStatus;
import app.confectionery.modules.user.model.DTO.UserSummaryDTO;
import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.configuration.jwt.JwtService;
import app.confectionery.modules.user.model.User;
import app.confectionery.modules.user.model.DTO.UserUpdateInfoDTO;
import app.confectionery.modules.user.repository.UserRepository;
import app.confectionery.modules.wallet.model.Transaction;
import app.confectionery.modules.wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(\\d{3}-?){3}$";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final TransactionRepository transactionRepository;
    private final AddressRepository addressRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public AuthenticationResponse updateUserProfileAndGenerateToken(UUID id, UserUpdateInfoDTO userUpdateInfoDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

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

        if (userUpdateInfoDTO.getPhoneNumber() != null && !userUpdateInfoDTO.getPhoneNumber().isEmpty()) {
            if (!Pattern.matches(PHONE_REGEX, userUpdateInfoDTO.getPhoneNumber())) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
        user.setPhoneNumber(userUpdateInfoDTO.getPhoneNumber());
        user.setEmail(userUpdateInfoDTO.getEmail());

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

    public List<UserSummaryDTO> getAllUserSummaries() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            long orderCount = orderRepository.countByUserId(user.getId());
            String accountStatus = Optional.ofNullable(user.getAccountStatus())
                    .map(AccountStatus::name)
                    .orElse("UNKNOWN");

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

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.getOrders().forEach(order -> {
            cartItemRepository.deleteAllInBatch(order.getItems());
        });
        orderRepository.deleteAllInBatch(user.getOrders());

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);

        if (shoppingCart != null) {
            List<CartItem> cartItems = cartItemRepository.findAllByCart(shoppingCart);
            cartItemRepository.deleteAllInBatch(cartItems);
            shoppingCartRepository.deleteAllInBatch(Collections.singleton(shoppingCart));
        }

        List<Address> addresses = addressRepository.findByUserId(userId);
        addressRepository.deleteAllInBatch(addresses);

        List<Favorite> favorites = favoriteRepository.findAllByUserId(userId);
        favoriteRepository.deleteAllInBatch(favorites);

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        transactionRepository.deleteAllInBatch(transactions);

        userRepository.deleteAllInBatch(Collections.singleton(user));
    }

}
