package net.proselyte.springbootdemo.repository;

import net.proselyte.springbootdemo.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 *  Интерфейс DrinkRepository представляет собой репозиторий для хранения данных о напитках в MVC паттерне.
 *  @Query реализует метод пользовательского запроса на языке SQL.
 */
@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
    Optional<Drink> findById(Long id);
    @Query("SELECT dr FROM Drink dr WHERE CONCAT(dr.id, '', dr.name, '', dr.price) LIKE %?1%")
    List<Drink> search(String keyword);

}
