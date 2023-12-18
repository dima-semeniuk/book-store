package mate.academy.webapp.service;

import mate.academy.webapp.dto.user.UserRegistrationRequestDto;
import mate.academy.webapp.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
