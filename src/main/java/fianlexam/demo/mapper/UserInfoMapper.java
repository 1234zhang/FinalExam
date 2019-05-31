package fianlexam.demo.mapper;

import fianlexam.demo.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Brandon.
 * @date 2019/5/31.
 * @time 23:33.
 */

@Mapper
@Repository
public interface UserInfoMapper {
    @Insert("insert into user_info (username,password) value(#{username},#{password})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    boolean addUser(UserInfoEntity userInfoEntity);
    @Select("select * from user_info where(username = #{username})")
    UserInfoEntity getUser(String username);
}
