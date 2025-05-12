package snapmeal.snapmeal.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import snapmeal.snapmeal.global.code.BaseErrorCode;
import snapmeal.snapmeal.global.code.ErrorResponseDto;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public ErrorResponseDto getErrorResponse() {
        return errorCode.getErrorResponse();
    }

    public ErrorResponseDto getErrorResponseHttpStatus() {
        return errorCode.getErrorResponseHttpStatus();
    }
}
