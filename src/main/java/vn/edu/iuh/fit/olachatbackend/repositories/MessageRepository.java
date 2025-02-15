package vn.edu.iuh.fit.olachatbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.iuh.fit.olachatbackend.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
