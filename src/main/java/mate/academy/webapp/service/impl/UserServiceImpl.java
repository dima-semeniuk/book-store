package mate.academy.webapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.user.UserRegistrationRequestDto;
import mate.academy.webapp.dto.user.UserResponseDto;
import mate.academy.webapp.exception.RegistrationException;
import mate.academy.webapp.mapper.UserMapper;
import mate.academy.webapp.model.Role;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.RoleRepository;
import mate.academy.webapp.repository.UserRepository;
import mate.academy.webapp.service.ShoppingCartService;
import mate.academy.webapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toModel(requestDto);
        Role role = roleRepository.findByRoleName(Role.RoleName.USER);
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        shoppingCartService.createNewShoppingCart(userRepository.save(user));
        return userMapper.toDto(user);
    }
}
