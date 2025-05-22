package snapmeal.snapmeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snapmeal.snapmeal.domain.Friends;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Status;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.handler.FriendHandler;
import snapmeal.snapmeal.global.handler.UserHandler;
import snapmeal.snapmeal.global.util.AuthService;
import snapmeal.snapmeal.repository.FriendRepository;
import snapmeal.snapmeal.repository.UserRepository;
import snapmeal.snapmeal.web.dto.FriendRequestDto;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendCommandServiceImpl implements FriendCommandService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public void sendFriendRequest(FriendRequestDto.SendRequest dto) {
        User requester = authService.getCurrentUser();

        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new UserHandler(ErrorCode.USER_NOT_FOUND));

        if (friendRepository.existsByRequesterAndReceiver(requester, receiver)) {
            throw new FriendHandler(ErrorCode.ALREADY_SENT_REQUEST);
        }

        Friends friend = Friends.builder()
                .requester(requester)
                .receiver(receiver)
                .status(Status.PENDING)
                .build();

        friendRepository.save(friend);
    }

    @Transactional
    public void changeFriendRequestStatus(Long friendId, FriendRequestDto.ChangeStatusDto dto) {
        User receiver = authService.getCurrentUser();

        Friends friendRequest = friendRepository.findById(friendId)
                .orElseThrow(() -> new FriendHandler(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friendRequest.getReceiver().equals(receiver)) {
            throw new FriendHandler(ErrorCode.UNAUTHORIZED_ACTION);
        }

        if (friendRequest.getStatus() != Status.PENDING) {
            throw new FriendHandler(ErrorCode.ALREADY_PROCESSED_REQUEST);
        }

        friendRequest.setStatus(dto.getStatus());
    }

    @Override
    public void deleteFriend(FriendRequestDto.DeleteRequestDto dto) {
        User currentUser = authService.getCurrentUser();

        Friends friend = friendRepository.findById(dto.getFriendId())
                .orElseThrow(() -> new FriendHandler(ErrorCode.FRIEND_NOT_FOUND));

        // 본인이 친구 요청한 사람이거나 수락한 사람이 아닐 경우
        if (!friend.getRequester().equals(currentUser) && !friend.getReceiver().equals(currentUser)) {
            throw new FriendHandler(ErrorCode.UNAUTHORIZED_ACTION);
        }

        friendRepository.delete(friend);
    }

}
