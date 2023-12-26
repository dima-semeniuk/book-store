package mate.academy.webapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.webapp.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch
public class UserRegistrationRequestDto {
    private static final String NOT_BLANK = "can't be blank.";
    private static final String NOT_WELL_EMAIL = "was formed not well.";

    @Email(message = NOT_WELL_EMAIL)
    @NotBlank(message = NOT_BLANK)
    private String email;

    @NotBlank(message = NOT_BLANK)
    @Length(min = 6, max = 30)
    private String password;

    @NotBlank(message = NOT_BLANK)
    @Length(min = 6, max = 30)
    private String repeatPassword;

    @NotBlank(message = NOT_BLANK)
    private String firstName;

    @NotBlank(message = NOT_BLANK)
    private String lastName;

    private String shippingAddress;
}
