package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.*;
import vn.edu.iuh.fit.olachatbackend.enums.RequestStatus;

import java.util.Date;

@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private Date sentAt;

    private Date responseAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @PrePersist
    public void prePersist() {
        this.sentAt = new Date();
        this.status = RequestStatus.PENDING;
    }
}
