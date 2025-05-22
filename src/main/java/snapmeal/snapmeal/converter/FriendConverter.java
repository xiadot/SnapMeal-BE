package snapmeal.snapmeal.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import snapmeal.snapmeal.domain.Friends;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Status;
import snapmeal.snapmeal.web.dto.FriendRequestDto;
@RequiredArgsConstructor
@Component
public class FriendConverter {
    public static Friends toEntity(FriendRequestDto.ChangeStatusDto dto,User receiver) {
        return Friends.builder()
                .receiver(receiver)
                .status(dto.getStatus())
                .build();
    }
}
