package snapmeal.snapmeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import snapmeal.snapmeal.domain.Friends;
import snapmeal.snapmeal.domain.User;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);
}
