package net.proselyte.springbootdemo.service;

import net.proselyte.springbootdemo.model.Drink;
import net.proselyte.springbootdemo.repository.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Класс DrinkService связывает репозиторий DrinkRepository и модель Drink.
 * @Autowired - аннотация для автоматического внедрения зависимости (в данном случае - DrinkRepository);
 * findById(Long id) - метод для получения сущности Drink по ее идентификатору;
 * findAll() - метод для получения списка всех сущностей Drink;
 * saveDrink(Drink drink) - метод для сохранения сущности Drink в базе данных;
 * deleteById(Long id) - метод для удаления сущности Drink из базы данных по ее идентификатору;
 * listAll(String keyword) - метод для получения списка всех сущностей Drink из базы данных, либо списка сущностей,
 * в имени которых содержится заданный ключевой слово (keyword).
 */
@Service
public class DrinkService {
    private final DrinkRepository drinkRepository;
    @Autowired
    public DrinkService(DrinkRepository drinkRepository){
        this.drinkRepository=drinkRepository;
    }

    public Drink findById(Long id){
        return drinkRepository.findById(id).get();
    }

    public List<Drink> findAll(){
        return drinkRepository.findAll();
    }

    public Drink saveDrink(Drink drink){
        return drinkRepository.save(drink);
    }

    public void deleteById(Long id){
        drinkRepository.deleteById(id);
    }

    public List<Drink> listAll(String keyword) {
        if (keyword != null) {
            return drinkRepository.search(keyword);
        } else {
            return drinkRepository.findAll();
        }
    }
}
