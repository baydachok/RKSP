package messageservice.repository;

import java.util.List;
import messageservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findMessagesByModel(String model);

}
