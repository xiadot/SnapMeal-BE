package snapmeal.snapmeal.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements BaseErrorCode {

    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON001", "잘못된 입력값입니다."),


    //유저 관련 응답
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"AUTH001" ,"비밀번호가 일치하지 않습니다." ),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH002", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "AUTH003", "이미 가입된 이메일입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "AUTH004", "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;


    @Override
    public ErrorResponseDto getErrorResponse() {
        return ErrorResponseDto.builder()
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorResponseDto getErrorResponseHttpStatus() {
        return ErrorResponseDto.builder()
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
