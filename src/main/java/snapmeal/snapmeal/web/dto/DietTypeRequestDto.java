package snapmeal.snapmeal.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class DietTypeRequestDto {
    private List<String> selectedTypes;

}
