package fianlexam.demo.controller;

import fianlexam.demo.entity.ResultEntity;
import fianlexam.demo.entity.UserInfoEntity;
import fianlexam.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Brandon.
 * @date 2019/5/31.
 * @time 22:37.
 */

@Controller
@Slf4j
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        return "login";
    }
    @RequestMapping(value = "check",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity check(HttpServletRequest request, HttpServletResponse response) {
        if(loginService.check(new UserInfoEntity(
                request.getParameter("username"),
                request.getParameter("password")
        ),request.getParameter("type"), request.getSession())){
            return ResultEntity.success(1,"success");
        }else{

            return ResultEntity.error(0,"fail");
        }
    }
    @RequestMapping(value = "register")
    public String register(){
        return "register";
    }
}
