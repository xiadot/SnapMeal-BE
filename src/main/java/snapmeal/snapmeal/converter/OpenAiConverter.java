package snapmeal.snapmeal.converter;

import java.util.List;

public class OpenAiConverter {
    public static String buildSystemPrompt() {
        return """
            당신은 MZ세대의 식습관 유형을 재밌게 분석해주는 트렌디한 짤 생성기입니다.
            사용자가 선택한 식습관 키워드를 보고,
            이 사람의 식습관을 '#OOO 유형' 형식으로 표현해주세요.
            
            단순히 단어를 조합하지 말고,
            요즘 유행하는 말투, 밈, 감정 표현, 상황극 스타일을 섞어주세요.
            웃기거나 공감되면 더 좋습니다.
            
            출력은 반드시 한 줄, 예시로 '#디저트 집착 유형' 같은 형식으로 알려 주세요
            """;

    }

    public static String buildUserPrompt(List<String> selectedTypes) {
        return "사용자가 선택한 식습관 유형: " + String.join(", ", selectedTypes);
    }
}
