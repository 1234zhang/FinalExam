package fianlexam.demo.service;

import fianlexam.demo.entity.UserInfoEntity;
import fianlexam.demo.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author Brandon.
 * @date 2019/5/31.
 * @time 23:43.
 */

@Service
public class LoginService {
    @Autowired
    UserInfoMapper userInfoMapper;

    public boolean check(UserInfoEntity userInfoEntity, String type, HttpSession session){
        UserInfoEntity user = userInfoMapper.getUser(userInfoEntity.getUsername());
       if("register".equals(type)){
           if(user == null){
               return userInfoMapper.addUser(userInfoEntity);
           }else{
               return false;
           }
       }else{
           if(user != null && user.getPassword().equals(userInfoEntity.getPassword())){
               session.setAttribute("username", user.getUsername());
               return true;
           }else{
               return false;
           }
       }
    }
}
