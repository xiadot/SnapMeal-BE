package snapmeal.snapmeal.global.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String errorCode;
    private final boolean isSuccess;

    public ErrorResponseDto(HttpStatus httpStatus, boolean isSuccess, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ErrorResponseDto of(HttpStatus status, String code, String message) {
        return new ErrorResponseDto(status, false, code, message);
    }
    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(
                errorCode.getHttpStatus(),
                false,
                errorCode.getErrorCode(),
                errorCode.getErrorMessage()
        );
    }
}