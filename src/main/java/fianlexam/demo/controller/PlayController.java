package fianlexam.demo.controller;

import fianlexam.demo.entity.ResultEntity;
import fianlexam.demo.service.PlayService;
import fianlexam.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 1:01.
 */

@Controller
@Slf4j
public class PlayController {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    PlayService playService;


    @RequestMapping("play")
    public String play(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null){
            request.setAttribute("username",(String)session.getAttribute("username"));
            return "hall";
        }
        return "login";
    }

    @RequestMapping("room")
    public String room(HttpServletRequest request){
        request.setAttribute("host",request.getParameter("host"));
        request.setAttribute("username",(String)request.getSession().getAttribute("username"));
        redisTemplate.opsForValue().set((String)request.getSession().getAttribute("username") + "status","0");
        return "room";
    }

    @RequestMapping(value = "createRoom",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity create(HttpServletRequest request,String host,String password){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(host);
        redisTemplate.opsForValue().set(host + "host",stringBuffer.toString());
        log.info("controller hostName " + host);
        if(password != null && !"".equals(password)){
            redisTemplate.opsForValue().set(host + "password" ,password);
        }
        return ResultEntity.success(1,"创建成功");
    }

    @RequestMapping(value = "checkRoom",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity check(HttpServletRequest request, HttpServletResponse response) {
        String host = request.getParameter("host");
        log.info("the host`s name is " + host);
        String username = (String) request.getSession().getAttribute("username");
        StringBuffer stringBuffer = new StringBuffer(redisTemplate.opsForValue().get(host + "host"));
        stringBuffer.append("," + username);
        redisTemplate.opsForValue().set(host + "host",stringBuffer.toString());
        return ResultEntity.success(2,"加入成功");
    }

    @RequestMapping(value = "ready",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity ready(HttpServletRequest request){
        String username = (String)request.getSession().getAttribute("username");
        return ResultEntity.success(1,JsonUtil.toJson(playService.playStatus(username)));
    }

    @RequestMapping("/clear")
    public void clear(HttpServletRequest request){
        playService.clear();
    }

    @RequestMapping(value = "/regret",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String regret(String username){
        log.info(username + "： 悔棋");
        return JsonUtil.toJson(playService.regret(username));
    }

    @RequestMapping(value = "/readyNum",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String readyNum(){
        return JsonUtil.toJson(ResultEntity.success(1,playService.getUserSetSize() + ""));
    }
    @RequestMapping(value = "checkPassword",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkPassword(String host){
        String password = redisTemplate.opsForValue().get(host + "password");
        log.info(host + "房间的密码是" + password);
        if(password != null && !"".equals(password)){
            return JsonUtil.toJson(ResultEntity.success(1,password));
        }
        return JsonUtil.toJson(ResultEntity.success(1,""));
    }
}
