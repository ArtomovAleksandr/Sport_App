package application.controller;

import application.entity.Administrator;
import application.service.helper.HashHelper;
import application.service.implementations.AdministratorService;
import application.service.implementations.CardService;
import application.service.implementations.UserService;
import application.service.implementations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Controller
public class LoginController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;
    @Autowired
    private FiliationService filiationService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("filiations",filiationService.getAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "filiation";
    }

    //Need replace this functions to another controller ?!?!?
    @GetMapping("/users")
    public String users(Model model) {
        try {
            model.addAttribute("users",userService.getAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "users";
    }

    @GetMapping("/userEdit/{id}")
    public String userEdit(@PathVariable int id,Model model){
        try {
            model.addAttribute("user",userService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user_details";
    }

    @GetMapping("/userCreate")
    public String userCreate(){
        return "user_create";
    }
    //-------------------------------------------------------

    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest req){
        HttpSession session = req.getSession();
        session.removeAttribute("identifier");
        return "login";
    }

    @GetMapping("/login")
    public String loginGet(){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(HttpServletRequest req){
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            List<Administrator> admins = administratorService.getAll();

            for(Administrator a:admins){
                //Если такой логин существует - проверяем пароль
                if(login.equals(a.getLogin())) {
                    String adminHash = a.getAdminHash();
                    String saltHash = adminHash.substring(40);
                    System.out.println(saltHash);
                    String checkHash = HashHelper.makeSHA1Hash(saltHash+password)+saltHash;
                    if(checkHash.equals(adminHash)){
                        HttpSession session = req.getSession();
                        try {
                            session.setAttribute("identifier",HashHelper.makeSHA1Hash(adminHash));
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "filiation";
    }
}