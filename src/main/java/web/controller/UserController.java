package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.User;
import web.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Validated
public class UserController {

    final
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String printWelcome(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("/addUser")
    public String addUser(ModelMap model) {
        model.addAttribute("user", new User());
        return ("/adduser");
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage" , result.getAllErrors().toString());
            return "redirect:/error";
        }
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/edituser")
    public String edit(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edituser";
    }

    @PostMapping("/save_edit_user")
    public String updateUser(@RequestParam(value = "id") Long id, @Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage" , result.getAllErrors().toString());

            return "redirect:/error";
        }
        userService.updateUser(id, user);
        return "redirect:/";
    }

    @GetMapping(value = "/delete_user")
    public String deleteUser(@RequestParam(value = "id") Long id, Model model) {
        userService.deleteUser(id);
        return "redirect:/";

    }
    @GetMapping(value = "/error")
    public String printError(Model model, String errorMessage) {
        if (errorMessage != null){
            model.addAttribute("errorMessage",errorMessage);
        }
        return "error";
    }
}