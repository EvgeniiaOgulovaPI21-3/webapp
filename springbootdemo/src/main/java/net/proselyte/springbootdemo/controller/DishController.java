package net.proselyte.springbootdemo.controller;

import net.proselyte.springbootdemo.model.Dish;
import net.proselyte.springbootdemo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
/**
 *  Класс DishController управляет данными в архитектуре паттерна Model-View-Controller;
 *  связывает модель Dish и виртуальную таблицу, гарантируя передачу информации между ними;
 *  получает данные из виртуальной табоицы, обрабатывает и возвращает их;
 *  обрабатывает запросы от клиента, выполняет необходимые операции и возвращает результат.
 */
@Controller
public class DishController {
    @ModelAttribute
    public void interceptor(Authentication authentication, Model model) {
        if (authentication == null) {
            model.addAttribute("authorized",false);
            model.addAttribute("isAdmin",false);
        }
        else {
            if (!authentication.getAuthorities().stream().map(d -> d.getAuthority()).toList().contains("USER")){
                model.addAttribute("isAdmin",true);}
            else {
                model.addAttribute("isAdmin",false);
            }
            model.addAttribute("authorized",true);
        }
    }
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/dish")
    public String findByKey(Model model, @Param("keyword") String keyword){
        List<Dish> dish = dishService.listAll(keyword);
        model.addAttribute("dish",dish);
        model.addAttribute("keyword",keyword);
        return "dish-list";
    }
    @GetMapping("/dish-create")
    public String createDishForm(Dish dish){
        return "dish-create";
    }
    @PostMapping("/dish-create")
    public String createDish(Dish dish){
        dishService.saveDish(dish);
        return "redirect:/dish";
    }
    @GetMapping("dish-delete/{id}")
    public String deleteDish(@PathVariable("id") Long id){
        dishService.deleteById(id);
        return "redirect:/dish";
    }
    @GetMapping("dish-update/{id}")
    public String updateDishForm(@PathVariable("id") Long id,Model model){
        Dish dish = dishService.findById(id);
        model.addAttribute("dish",dish);
        return "/dish-update";
    }
    @PostMapping("/dish-update")
    public String updateDish(Dish dish){
        dishService.saveDish(dish);
        return "redirect:/dish";
    }
}
