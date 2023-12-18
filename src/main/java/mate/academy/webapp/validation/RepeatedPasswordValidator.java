package mate.academy.webapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.webapp.dto.user.UserRegistrationRequestDto;

public class RepeatedPasswordValidator implements ConstraintValidator<FieldMatch,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword() != null && user.getPassword().equals(user.getRepeatPassword());
    }
}
