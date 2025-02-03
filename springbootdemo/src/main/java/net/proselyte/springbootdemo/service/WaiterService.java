package net.proselyte.springbootdemo.service;

import net.proselyte.springbootdemo.model.Waiter;
import net.proselyte.springbootdemo.repository.WaiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Класс WaiterService связывает репозиторий WaiterRepository и модель Waiter.
 * @Autowired - аннотация для автоматического внедрения зависимости (в данном случае - WaiterRepository);
 * findById(Long id) - метод для получения сущности Waiter по ее идентификатору;
 * findAll() - метод для получения списка всех сущностей Waiter;
 * saveWaiter(Waiter waiter) - метод для сохранения сущности Waiter в базе данных;
 * deleteById(Long id) - метод для удаления сущности Waiter из базы данных по ее идентификатору;
 * listAll(String keyword) - метод для получения списка всех сущностей Waiter из базы данных, либо списка сущностей,
 * в имени которых содержится заданный ключевой слово (keyword).
 */
@Service
public class WaiterService {
    private final WaiterRepository waiterRepository;
    @Autowired
    public WaiterService(WaiterRepository waiterRepository){
        this.waiterRepository=waiterRepository;
    }

    public Waiter findById(Long id){
        return waiterRepository.findById(id).get();
    }

    public List<Waiter> findAll(){
        return waiterRepository.findAll();
    }

    public Waiter saveWaiter(Waiter waiter){
        return waiterRepository.save(waiter);
    }

    public void deleteById(Long id){
        waiterRepository.deleteById(id);
    }

    public List<Waiter> listAll(String keyword) {
        if (keyword != null) {
            return waiterRepository.search(keyword);
        } else {
            return waiterRepository.findAll();
        }
    }
}
