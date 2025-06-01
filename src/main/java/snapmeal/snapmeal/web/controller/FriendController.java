package snapmeal.snapmeal.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.multipart.MultipartFile;
import snapmeal.snapmeal.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.swagger.ApiErrorCodeExamples;
import snapmeal.snapmeal.service.FriendCommandService;
import snapmeal.snapmeal.service.FriendCommandServiceImpl;
import snapmeal.snapmeal.web.dto.FriendRequestDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendCommandService friendCommandService;



    @PostMapping
    @Operation(summary = "친구 요청", description = "다른 사용자에게 친구 요청을 보냅니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "친구 요청 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    public ResponseEntity<ApiResponse<Void>> requestFriend(@RequestBody FriendRequestDto.SendRequest dto) {
        friendCommandService.sendFriendRequest(dto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @PatchMapping("/{friendId}")
    @Operation(
            summary = "친구 요청 응답",
            description = "수신자가 친구 요청을 수락 또는 거절합니다.\n\n" +
                    "**가능한 상태 값:**\n" +
                    "- PENDING: 요청 대기 중\n" +
                    "- ACCEPTED: 수락됨\n" +
                    "- REJECTED: 거절됨"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "친구 응답 처리 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @ApiErrorCodeExamples({
            ErrorCode.FRIEND_REQUEST_NOT_FOUND,
            ErrorCode.UNAUTHORIZED_ACTION,
            ErrorCode.ALREADY_PROCESSED_REQUEST
    })
    public ResponseEntity<ApiResponse<Void>> respondToFriendRequest(
            @PathVariable Long friendId,
            @RequestBody FriendRequestDto.ChangeStatusDto dto) {
        friendCommandService.changeFriendRequestStatus(friendId, dto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @DeleteMapping
    @Operation(summary = "친구 삭제", description = "친구 관계를 삭제합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "친구 삭제 성공"
    )
    @ApiErrorCodeExamples({
            ErrorCode.FRIEND_NOT_FOUND,
            ErrorCode.UNAUTHORIZED_ACTION
    })
    public ResponseEntity<Void> deleteFriend(@RequestBody FriendRequestDto.DeleteRequestDto dto) {
        friendCommandService.deleteFriend(dto);
        return ResponseEntity.noContent().build();
    }
}
