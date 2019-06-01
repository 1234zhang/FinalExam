package fianlexam.demo.controller;

import fianlexam.demo.entity.ResultEntity;
import fianlexam.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 1:01.
 */

@Controller
@Slf4j
public class PlayController {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private List<String> userList;

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
        return "room";
    }
    @RequestMapping(value = "createRoom",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity create(HttpServletRequest request){
        List<String> list = new ArrayList<>();
        list.add((String)request.getSession().getAttribute("username"));
        redisTemplate.opsForValue().set(request.getParameter("host"), JsonUtil.toJson(list));
        return ResultEntity.success(1,"创建成功");
    }

    @RequestMapping(value = "checkRoom",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity check(HttpServletRequest request, HttpServletResponse response) {
        String host = request.getParameter("host");
        String username = (String) request.getSession().getAttribute("username");
        userList = JsonUtil.fromJson(redisTemplate.opsForValue().get(host),List.class);
        if(userList.size() < 2){
            userList.add(username);
            return ResultEntity.success(2,"加入成功");
        }
        return ResultEntity.error(1,"房间已经满了！！");
    }
}
