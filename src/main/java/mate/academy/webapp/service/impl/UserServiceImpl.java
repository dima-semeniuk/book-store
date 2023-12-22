package mate.academy.webapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.user.UserRegistrationRequestDto;
import mate.academy.webapp.dto.user.UserResponseDto;
import mate.academy.webapp.exception.RegistrationException;
import mate.academy.webapp.mapper.UserMapper;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.UserRepository;
import mate.academy.webapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.toDto(savedUser);
    }
}
