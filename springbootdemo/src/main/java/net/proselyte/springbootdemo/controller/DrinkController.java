package net.proselyte.springbootdemo.controller;

import net.proselyte.springbootdemo.model.Drink;
import net.proselyte.springbootdemo.service.DrinkService;
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
 *  Класс DrinkController управляет данными в архитектуре паттерна Model-View-Controller;
 *  связывает модель Drink и виртуальную таблицу, гарантируя передачу информации между ними;
 *  получает данные из виртуальной табоицы, обрабатывает и возвращает их;
 *  обрабатывает запросы от клиента, выполняет необходимые операции и возвращает результат.
 */
@Controller
public class DrinkController {
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
    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/drink")
    public String findByKey(Model model, @Param("keyword") String keyword){
        List<Drink> drink = drinkService.listAll(keyword);
        model.addAttribute("drink",drink);
        model.addAttribute("keyword",keyword);
        return "drink-list";
    }
    @GetMapping("/drink-create")
    public String createDrinkForm(Drink drink){
        return "drink-create";
    }
    @PostMapping("/drink-create")
    public String createDrink(Drink drink){
        drinkService.saveDrink(drink);
        return "redirect:/drink";
    }
    @GetMapping("drink-delete/{id}")
    public String deleteDrink(@PathVariable("id") Long id){
        drinkService.deleteById(id);
        return "redirect:/drink";
    }
    @GetMapping("drink-update/{id}")
    public String updateDrinkForm(@PathVariable("id") Long id,Model model){
        Drink drink = drinkService.findById(id);
        model.addAttribute("drink",drink);
        return "/drink-update";
    }
    @PostMapping("/drink-update")
    public String updateDrink(Drink drink){
        drinkService.saveDrink(drink);
        return "redirect:/drink";
    }
}
