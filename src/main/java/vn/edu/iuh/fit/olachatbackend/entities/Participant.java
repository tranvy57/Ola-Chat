package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.olachatbackend.controllers.ParticipantRole;

import java.util.Date;

@Document(collection = "participants")
public class Participant {
    @Id
    private String id;

    private String conversationId;
    private String userId;
    private ParticipantRole role;
    private Date joinedAt;
}
