package com.phantoms.helper.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.phantoms.helper.user.model.Order;
import com.phantoms.helper.user.model.User;

@Mapper
public interface UserMapper {
    
	/**
	 * 这里的sql 实现写在 sqlmap-user.xml
	 * @return
	 */
    User selectOne();
    
    @Insert("insert into ph_user_info (user_id, user_name, mobile, hash_val) values "
    		+ "(#{userId}, #{userName}, #{mobile}, #{hashVal} ) ")
    void insert(User user);
    
    @Insert("insert into ph_user_info (user_id, user_name, mobile, hash_val) values "
    		+ "(#{userId}, #{userName}, #{mobile}, #{hashVal} ) ")
    void insertOrder(User user);
    
    @Select("select user_name as userName from ph_user_info where user_name like CONCAT('%',#{userName},'%') and hash_val = #{hashVal}")
    List<User> selectByLikeName(String userName, int hashVal);
    
    
    
    
}
