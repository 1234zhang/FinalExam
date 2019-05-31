package fianlexam.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 1:01.
 */

@Controller
@Slf4j
public class PlayController {
    @RequestMapping("play")
    public String play(HttpServletRequest request){
        if(request.getSession().getAttribute("username") != null){
            log.info("登录成功  ");
        }
        return "play";
    }
}
