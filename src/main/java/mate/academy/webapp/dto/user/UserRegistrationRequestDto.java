package mate.academy.webapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.webapp.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch
public class UserRegistrationRequestDto {
    private static final String NOT_NULL = "can't be null.";
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

    @NotNull(message = NOT_NULL)
    private String firstName;

    @NotNull(message = NOT_NULL)
    private String lastName;

    private String shippingAddress;
}
