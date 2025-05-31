package snapmeal.snapmeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import snapmeal.snapmeal.util.MultipartInputStreamFileResource;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FastApiProxyService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendImageToFastApi(MultipartFile file) throws IOException {
        // FastAPI 서버 URL
        String fastApiUrl = "http://api.snapmeal.store/predict";

        // Multipart body 만들기
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename(), file.getSize()));

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 요청 Entity 만들기
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);

        // 결과 반환 (FastAPI 서버의 응답 본문)
        return response.getBody();
    }
}
