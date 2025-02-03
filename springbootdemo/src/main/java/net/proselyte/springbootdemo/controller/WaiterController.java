package net.proselyte.springbootdemo.controller;

import net.proselyte.springbootdemo.model.Waiter;
import net.proselyte.springbootdemo.service.WaiterService;
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
 *  Класс WaiterController управляет данными в архитектуре паттерна Model-View-Controller;
 *  связывает модель Waiter и виртуальную таблицу, гарантируя передачу информации между ними;
 *  получает данные из виртуальной табоицы, обрабатывает и возвращает их;
 *  обрабатывает запросы от клиента, выполняет необходимые операции и возвращает результат.
 */
@Controller
public class WaiterController {
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
    private final WaiterService waiterService;

    @Autowired
    public WaiterController(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @GetMapping("/waiter")
    public String findByKey(Model model, @Param("keyword") String keyword){
        List<Waiter> waiter = waiterService.listAll(keyword);
        model.addAttribute("waiter",waiter);
        model.addAttribute("keyword",keyword);
        return "waiter-list";
    }
    @GetMapping("/waiter-create")
    public String createWaiterForm(Waiter waiter){
        return "waiter-create";
    }
    @PostMapping("/waiter-create")
    public String createWaiter(Waiter waiter){
        waiterService.saveWaiter(waiter);
        return "redirect:/waiter";
    }
    @GetMapping("waiter-delete/{id}")
    public String deleteWaiter(@PathVariable("id") Long id){
        waiterService.deleteById(id);
        return "redirect:/waiter";
    }
    @GetMapping("waiter-update/{id}")
    public String updateWaiterForm(@PathVariable("id") Long id,Model model){
        Waiter waiter = waiterService.findById(id);
        model.addAttribute("waiter",waiter);
        return "/waiter-update";
    }
    @PostMapping("/waiter-update")
    public String updateWaiter(Waiter waiter){
        waiterService.saveWaiter(waiter);
        return "redirect:/waiter";
    }
}
