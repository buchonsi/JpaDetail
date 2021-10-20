package jpastudy.jpashop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/frag")
    public String home() {
        log.info("home controller");
        return "home";
    }

    @RequestMapping("/")
    public String index(){

        return "index";
    }

}
