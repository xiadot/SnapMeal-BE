package snapmeal.snapmeal.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DietTypeRequestDto {
    private List<String> selectedTypes;

}
