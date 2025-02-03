package net.proselyte.springbootdemo.service;

import net.proselyte.springbootdemo.model.User;
import net.proselyte.springbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Класс UserService представляет сервисный класс, который взаимодействует с репозиторием пользователей UserRepository.
 * @Service позволяет Spring-фреймворку автоматически внедрять этот класс в другие компоненты приложения;
 * findByUsername(String username) возвращает пользовательский объект из репозитория по заданному имени пользователя;
 * registrate(String username, String password,String confirmPassword) проверяет, существует ли пользователь с таким именем в базе данных,
 * и если нет, создает нового пользователя с заданными параметрами и сохраняет его в репозитории.
 * В случае, если пользователь существует или пароли не совпадают, метод вернет ResponseEntity с соответствующим статусом;
 * existsByUsername(String username) проверяет, существует ли пользователь с заданным именем в базе данных.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) { this.userRepository = userRepository;}
    public User findByUsername(String username) {return userRepository.findByLogin(username);}
    public ResponseEntity registrate(String username, String password,String confirmPassword) {
        if (existsByUsername(username)) return new ResponseEntity("Такой пользователь уже существует", HttpStatus.BAD_REQUEST);
        if (!password.equals(confirmPassword)) return new ResponseEntity("Пароли не совпадают", HttpStatus.BAD_REQUEST);
        User user = new User(username, password, "USER");
        userRepository.save(user);
        return new ResponseEntity("Пользователь зарегистрирован", HttpStatus.OK);
    }
    private boolean existsByUsername(String username) {return userRepository.existsByLogin(username);}
}
