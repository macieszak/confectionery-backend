package app.confectionery.authorization.service;

import app.confectionery.user.model.Role;
import app.confectionery.user.model.User;
import app.confectionery.authorization.model.UserRegistrationDto;
import app.confectionery.user.repository.RoleRepository;
import app.confectionery.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationUserServiceImpl implements RegistrationUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto userRegistrationDto) {

        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setPhoneNumber(null);

        Optional<Role> defaultRole = roleRepository.findByName("USER");
        user.setRole(defaultRole.orElseThrow(() -> new RuntimeException("Default role not set")));

        return userRepository.save(user);
    }
}
