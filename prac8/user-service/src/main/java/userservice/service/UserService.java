package userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.entity.User;
import userservice.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UniqueStringGenerator uniqueStringGenerator;

    public String getId(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            String id = uniqueStringGenerator.generateUniqueString(login);
            User user1 = new User();
            user1.setLogin(login);
            user1.setGeneratedId(id);
            userRepository.save(user1);
            return id;
        } else {
            return user.get().getGeneratedId();
        }
    }
}
