package snapmeal.snapmeal.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.repository.UserRepository;
import snapmeal.snapmeal.service.S3UploadService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
@Tag(name = "Image API", description = "이미지 업로드 API")
@RequiredArgsConstructor
public class S3Controller {

    private final S3UploadService s3UploadService;
    private final UserRepository userRepository; // ✅ UserRepository 주입

    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 S3에 업로드하고 DB에 URL, user_id를 저장합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "multipart/form-data"
                    )
            )
    )
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImage(
            @RequestPart MultipartFile file
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // email로 받아와짐

        // 이메일 기준 User 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = s3UploadService.uploadAndSave(file, user);

        return ResponseEntity.ok(Map.of(
                "imageUrl", imageUrl,
                "userId", user.getId()
        ));
    }
}