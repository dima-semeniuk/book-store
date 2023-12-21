package mate.academy.webapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(

        @Email(message = "was formed not well.")
        @NotBlank(message = "can't be blank.")
        String email,
        @NotBlank(message = "can't be blank.")
        @Length(min = 6, max = 30)
        String password) {
}
