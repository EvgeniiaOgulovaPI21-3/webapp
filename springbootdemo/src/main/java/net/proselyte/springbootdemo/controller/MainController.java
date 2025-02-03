package net.proselyte.springbootdemo.controller;

import net.proselyte.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *  Класс MainController управляет данными в архитектуре паттерна Model-View-Controller;
 *  обрабатывает запросы от клиента для входа на страницу, выполняет необходимые операции и возвращает результат.
 */
@Controller
public class MainController {
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
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {return "home";}

    @GetMapping("/author")
    public String author(Model model) {return "author";}

    @GetMapping("/login")
    public String login(Model model) {return "login";}

    @GetMapping("/signup")
    public String signup(Model model) {return "signup";}

    @GetMapping("/registration")
    public String registrationPage() {return "signup";}

    @PostMapping("/registration")
    public String doRegistration(@RequestParam("username") String username, @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword) {
        ResponseEntity response = userService.registrate(username, password, confirmPassword);
        if (response.getStatusCodeValue() == 400) return "signup";
        return "login";
    }

}
