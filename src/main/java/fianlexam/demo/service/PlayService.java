package fianlexam.demo.service;

import fianlexam.demo.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 15:50.
 */

@Service
public class PlayService<T> {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    public Map playStatus(String username){
        int status = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(username)));
        int flag = status^1;
        Map<String, Integer> map = new HashMap<>();
        List<String> userList = new ArrayList<>();
        if(flag == 1){
            userList.add(username);
        }else{
            userList.remove(username);
        }
        redisTemplate.opsForValue().set(username,flag + "");
        redisTemplate.opsForValue().set("ready", JsonUtil.toJson(userList));
        map.put("status",flag);
        map.put("readyNum",userList.size());
        return map;
    }
}
