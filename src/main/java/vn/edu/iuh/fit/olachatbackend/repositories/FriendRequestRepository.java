/*
 * @ (#) FriendRequestRepository.java       1.0     14/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.repositories;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/02/2025
 * @version:    1.0
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.olachatbackend.entities.FriendRequest;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT COUNT(f) > 0 FROM FriendRequest f WHERE " +
            "(f.sender = :user1 AND f.receiver = :user2 OR f.sender = :user2 AND f.receiver = :user1) " +
            "AND f.status = 'ACCEPTED'")
    boolean areFriends(@Param("user1") User user1, @Param("user2") User user2);

    // Tìm danh sách lời mời đã nhận (Pending)
    List<FriendRequest> findByReceiverAndStatus(User receiver, RequestStatus status);

    // Tìm danh sách lời mời đã gửi (Pending)
    List<FriendRequest> findBySenderAndStatus(User sender, RequestStatus status);
}
