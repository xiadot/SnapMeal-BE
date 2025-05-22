package snapmeal.snapmeal.service;

import snapmeal.snapmeal.web.dto.FriendRequestDto;

public interface FriendCommandService  {
    void sendFriendRequest(FriendRequestDto.SendRequest dto);
    void changeFriendRequestStatus(Long friendId, FriendRequestDto.ChangeStatusDto dto);
    void deleteFriend(FriendRequestDto.DeleteRequestDto dto);
}
