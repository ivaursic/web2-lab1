package web2.lab1.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Postavite odgovarajuće atribute u model, ako je potrebno
        return "error"; // Vraća ime Thymeleaf predloška za prikazivanje greške
    }


}