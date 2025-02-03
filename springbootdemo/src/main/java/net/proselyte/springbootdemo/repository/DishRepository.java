package net.proselyte.springbootdemo.repository;

import net.proselyte.springbootdemo.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 *  Интерфейс DishRepository представляет собой репозиторий для хранения данных о блюдах в MVC паттерне.
 *  @Query реализует метод пользовательского запроса на языке SQL.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findById(Long id);
    @Query("SELECT d FROM Dish d WHERE CONCAT(d.id, '', d.name, '', d.price) LIKE %?1%")
    List<Dish> search(String keyword);

}
