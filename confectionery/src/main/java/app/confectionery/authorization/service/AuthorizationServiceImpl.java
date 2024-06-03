package app.confectionery.authorization.service;

import app.confectionery.authorization.model.request.AuthenticationRequest;
import app.confectionery.authorization.model.response.AuthenticationResponse;
import app.confectionery.authorization.model.request.RegisterRequest;
import app.confectionery.modules.cart.model.ShoppingCart;
import app.confectionery.modules.cart.repository.ShoppingCartRepository;
import app.confectionery.configuration.jwt.JwtService;
import app.confectionery.exception.CustomAuthenticationException;
import app.confectionery.modules.user.model.AccountStatus;
import app.confectionery.modules.user.model.User;
import app.confectionery.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalStateException("Email already in use. Please use a different email.");
        }
        try {
            var user = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(registerRequest.getRole())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .balance(BigDecimal.ZERO)
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            ShoppingCart shoppingCart = ShoppingCart.builder().totalItems(0).totalPrice(0.0).build();
            shoppingCart.setUser(user);
            user.setCart(shoppingCart);

            var savedUser = userRepository.save(user);
            shoppingCartRepository.save(shoppingCart);

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .id(savedUser.getId())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole().name())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();

            if (user.getAccountStatus() == AccountStatus.BLOCK) {
                throw new IllegalStateException("Account is inactive.");
            }

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

        } catch (AuthenticationException ex) {
            throw new CustomAuthenticationException("Invalid email/password combination.");
        }
    }
}
