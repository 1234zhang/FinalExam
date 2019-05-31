package fianlexam.demo.entity;

import lombok.Data;

/**
 * @author Brandon.
 * @date 2019/5/31.
 * @time 23:34.
 */

@Data
public class UserInfoEntity {
    private String username;
    private String password;
    public UserInfoEntity(String username, String password){
        this.username = username;
        this.password = password;
    }
}
