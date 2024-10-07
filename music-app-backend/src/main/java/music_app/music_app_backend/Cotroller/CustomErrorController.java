package music_app.music_app_backend.Cotroller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("status", status);
        return "public/error/404";
    }
}
