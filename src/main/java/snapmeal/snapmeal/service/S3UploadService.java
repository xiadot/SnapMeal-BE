package snapmeal.snapmeal.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;
import snapmeal.snapmeal.config.S3Configure;
import snapmeal.snapmeal.domain.Images;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.repository.ImageRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final S3Configure s3Configure;
    private final ImageRepository imagesRepository;

    @Transactional
    public String uploadAndSave(MultipartFile file, User user) throws IOException {
        // 1. 파일 이름 생성 (UUID-원본파일명)
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // 2. 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // 3. S3에 업로드
        String bucket = s3Configure.getBucket();
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

        // 4. 업로드된 파일 URL 얻기
        String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

        // 5. DB에 저장
        Images image = Images.builder()
                .imageUrl(fileUrl)
                .user(user)   // 로그인한 사용자 정보 저장
                .build();

        imagesRepository.save(image);

        // 6. 파일 URL 리턴
        return fileUrl;
    }
}
