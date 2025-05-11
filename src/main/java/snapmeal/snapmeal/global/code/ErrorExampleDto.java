package snapmeal.snapmeal.global.code;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class ErrorExampleDto extends ErrorResponseDto {

    private ErrorExampleDto(HttpStatus httpStatus, String code, String message) {
        super(httpStatus, false, code, message);
    }

    public static ErrorExampleDto of(ErrorCode errorCode) {
        return new ErrorExampleDto(
                errorCode.getHttpStatus(),
                errorCode.getErrorCode(),
                errorCode.getErrorMessage()
        );
    }

    public static ErrorExampleDto of(ErrorCode errorCode, String additionalMessage) {
        return new ErrorExampleDto(
                errorCode.getHttpStatus(),
                errorCode.getErrorCode(),
                errorCode.getErrorMessage() + " - " + additionalMessage
        );
    }

    public static ErrorExampleDto of(ErrorCode errorCode, Exception e) {
        return new ErrorExampleDto(
                errorCode.getHttpStatus(),
                errorCode.getErrorCode(),
                errorCode.getErrorMessage() + " - " + e.getMessage()
        );
    }
}

