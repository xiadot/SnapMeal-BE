package snapmeal.snapmeal.global.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@Builder
public class ResponseDto {
    private HttpStatus httpStatus;
    private final String message;
    private final String code;
    private final boolean isSuccess;

}
