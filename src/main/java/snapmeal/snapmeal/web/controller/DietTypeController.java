package snapmeal.snapmeal.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snapmeal.snapmeal.service.DietTypeService;
import snapmeal.snapmeal.web.dto.DietTypeRequestDto;
import snapmeal.snapmeal.web.dto.DietTypeResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diet-type")
public class DietTypeController {

    private final DietTypeService dietTypeService;



    @PostMapping
    public ResponseEntity<DietTypeResponseDto> analyze(@RequestBody DietTypeRequestDto request) {
        return ResponseEntity.ok(dietTypeService.analyzeDietType(request));
    }
}
