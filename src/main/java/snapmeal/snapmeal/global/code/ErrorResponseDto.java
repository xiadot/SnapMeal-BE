package snapmeal.snapmeal.global.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {
    private HttpStatus httpStatus;
    private final String errorMessage;
    private final String errorCode;
    private final boolean isSuccess;
}
