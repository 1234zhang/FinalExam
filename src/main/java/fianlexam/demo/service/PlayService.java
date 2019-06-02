package fianlexam.demo.service;

import fianlexam.demo.entity.ResultEntity;
import fianlexam.demo.util.ChessUtil;
import fianlexam.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 15:50.
 */

@Service
@Slf4j
public class PlayService<T> {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    public static Set<String> userSet = new HashSet<>();

    private AtomicInteger count = new AtomicInteger();

    private Map<String,String> step = new HashMap<>();

    public void clear(){
        ChessUtil.clearArr();
    }
    public Map playStatus(String username){
        int status = Integer.parseInt(Objects.requireNonNull((String)redisTemplate.opsForValue().get(username + "status")));
        int flag = status^1;
        Map<String, Integer> map = new HashMap<>();
        if(flag == 1) {
            userSet.add(username);
        }else{
            userSet.remove(username);
        }
        redisTemplate.opsForValue().set(username + "status",flag + "");
        redisTemplate.opsForValue().set("ready", JsonUtil.toJson(userSet));
        map.put("status",flag);
        map.put("readyNum",userSet.size());
        return map;
    }

    public  int getUserSetSize() {
        return userSet.size();
    }

    public ResultEntity moveChess(String username, String location){
        List userList = JsonUtil.fromJson((String)redisTemplate.opsForValue().get("ready"),List.class);
        String[] strs = location.split(",");
        if(isNumeric(strs)) {
            if (userList.get(count.get() % 2).equals(username)) {
                step.put(username,location);
                redisTemplate.opsForValue().set(username,location);
                int sum = ChessUtil.play(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),count.get()%2 + 1);
                if(sum == 5){
                    count.set(0);
                    System.out.println("hello world");
                    return ResultEntity.success(10,username + " win");
                }else if(sum > 0){
                    count.getAndIncrement();
                    return ResultEntity.success(1,username + " 走的位置是：" + "(" + location + ")");
                }else{
                    return ResultEntity.error(-2,"输入错误");
                }
            }
            return ResultEntity.error(-1,"时间错误");
        }
        return ResultEntity.error(-2,"输入错误");
    }

    private boolean isNumeric(String[] strs){
        if(strs.length < 2){
            return false;
        }
        for (int i = 0; i < strs.length; i++) {
            for (int j = 0; j < strs[i].length(); j++) {
                if(!Character.isDigit(strs[i].charAt(j))){
                    return false;
                }
            }
        }
        return true;
    }

    public ResultEntity regret(String username){
        List userList = JsonUtil.fromJson((String)redisTemplate.opsForValue().get("ready"),List.class);
        String location = (String)redisTemplate.opsForValue().get(username);
        if(count.get()>0 && username.equals(userList.get(count.get()%2 - 1)) && location.equals(step.get(userList.get(count.get()%2 - 1)))){
            log.info(username + "想要撤回这一步 ：" + location);
            String[] locations = location.split(",");
            ChessUtil.regret(Integer.parseInt(locations[0]),Integer.parseInt(locations[1]));
            count.decrementAndGet();
            return ResultEntity.success(1,username + " remove the location " + location);
        }
        return ResultEntity.error(-1,"you can`t do it without you decide the location");
    }
}
