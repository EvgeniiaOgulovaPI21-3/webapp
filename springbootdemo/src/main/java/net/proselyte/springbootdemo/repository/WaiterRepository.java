package net.proselyte.springbootdemo.repository;

import net.proselyte.springbootdemo.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 *  Интерфейс WaiterRepository представляет собой репозиторий для хранения данных об официантах в MVC паттерне.
 *  @Query реализует метод пользовательского запроса на языке SQL.
 */
@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findById(Long id);
    @Query("SELECT w FROM Waiter w WHERE CONCAT(w.id, '', w.lastName, '', w.firstName) LIKE %?1%")
    List<Waiter> search(String keyword);

}
