package snapmeal.snapmeal.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictController {

    @Operation(summary = "이미지 예측", description = "이미지 파일을 업로드하면 class_id를 반환합니다.")
    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> predict(
            @Parameter(description = "이미지 파일", required = true)
            @RequestPart("file") MultipartFile file
    ) {
        try {
            File tempFile = File.createTempFile("upload-", ".jpg");
            file.transferTo(tempFile);

            ProcessBuilder pb = new ProcessBuilder("python", "predict.py", tempFile.getAbsolutePath());
            pb.directory(new File("C:/Users/jy829/Downloads/ssnapmeal/src"));  // ⬅ predict.py 있는 폴더
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            int exitCode = process.waitFor();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            System.out.println("=== Python stderr ===");
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);  // 에러 출력 로그 확인
            }
            System.out.println("=== End stderr ===");

            if (exitCode == 0) {
                return ResponseEntity.ok(Map.of("class_id", result));
            } else {
                return ResponseEntity.status(500).body("예측 실패");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류: " + e.getMessage());
        }
    }
}
