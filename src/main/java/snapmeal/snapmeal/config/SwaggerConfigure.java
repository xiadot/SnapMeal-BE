package snapmeal.snapmeal.config;

import ch.qos.logback.core.status.ErrorStatus;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.code.ErrorResponseDto;
import snapmeal.snapmeal.global.swagger.ApiErrorCodeExamples;
import snapmeal.snapmeal.global.swagger.ExampleHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfigure {

    @Bean
    public OpenAPI SnapMealAPI() {
        Info info = new Info()
                .title("SnapMeal API")
                .description("SnapMeal API 명세서")
                .version("1.0.0");

        String jwtSchemeName = "JWT TOKEN";

        // API 요청 헤더에 인증 정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);


        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("http://localhost:8080").description("Local server"))// 서버 URL 설정
                .addSecurityItem(securityRequirement)
                .components(components);
    }


    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            ApiErrorCodeExamples errorCodeExamples = handlerMethod.getMethodAnnotation(ApiErrorCodeExamples.class);
            if (errorCodeExamples != null) {
                generateErrorCodeResponseExample(operation, errorCodeExamples.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
                .map(errorCode -> ExampleHolder.builder()
                        .holder(getSwaggerExample(errorCode))
                        .code(errorCode.getHttpStatus().value())
                        .build())
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }



    private Example getSwaggerExample(ErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(errorCode);

        Example example = new Example();
        example.setValue(errorResponseDto);
        return example;
    }


    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach((status, holders) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();

            holders.forEach(holder -> {
                String name = holder.getName() != null ? holder.getName() : "example";
                mediaType.addExamples(name, holder.getHolder());
            });

            content.addMediaType("application/json", mediaType);
            apiResponse.setContent(content);
            responses.addApiResponse(String.valueOf(status), apiResponse);
        });
    }


    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        String name = exampleHolder.getName() != null ? exampleHolder.getName() : "example";
        mediaType.addExamples(name, exampleHolder.getHolder());

        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }


}
