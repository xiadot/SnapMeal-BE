package snapmeal.snapmeal.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 요청 상태")
public enum Status {

    @Schema(description = "요청 대기 중")
    PENDING,

    @Schema(description = "요청 수락됨")
    ACCEPTED,

    @Schema(description = "요청 거절됨")
    REJECTED
}
