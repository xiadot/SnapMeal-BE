package snapmeal.snapmeal.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import snapmeal.snapmeal.domain.enums.Status;

@Getter
@Setter

@Builder
public class FriendRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendRequest {
        private Long receiverId;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChangeStatusDto {

        @Schema(description = "변경할 친구 요청 상태 (PENDING, ACCEPTED, REJECTED)")
        private Status status;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteRequestDto {
        private Long friendId;
    }


}
