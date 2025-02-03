package net.proselyte.springbootdemo.service;

import net.proselyte.springbootdemo.model.Dish;
import net.proselyte.springbootdemo.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс DishService связывает репозиторий DishRepository и модель Dish.
 * @Autowired - аннотация для автоматического внедрения зависимости (в данном случае - DishRepository);
 * findById(Long id) - метод для получения сущности Dish по ее идентификатору;
 * findAll() - метод для получения списка всех сущностей Dish;
 * saveDish(Dish dish) - метод для сохранения сущности Dish в базе данных;
 * deleteById(Long id) - метод для удаления сущности Dish из базы данных по ее идентификатору;
 * listAll(String keyword) - метод для получения списка всех сущностей Dish из базы данных, либо списка сущностей,
 * в имени которых содержится заданный ключевой слово (keyword).
 */
@Service
public class DishService {
    private final DishRepository dishRepository;

    @Autowired

    public DishService(DishRepository dishRepository) { this.dishRepository = dishRepository;}


    public Dish findById(Long id){
        return dishRepository.findById(id).get();
    }

    public List<Dish> findAll(){
        return dishRepository.findAll();
    }

    public Dish saveDish(Dish dish){
        return dishRepository.save(dish);
    }

    public void deleteById(Long id){
        dishRepository.deleteById(id);
    }
    public List<Dish> listAll(String keyword) {
        if (keyword != null) {
            return dishRepository.search(keyword);
        } else {
            return dishRepository.findAll();
        }
    }
}
