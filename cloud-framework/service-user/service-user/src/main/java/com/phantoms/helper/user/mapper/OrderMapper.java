package com.phantoms.helper.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.phantoms.helper.user.model.Order;
import com.phantoms.helper.user.model.User;

@Mapper
public interface OrderMapper {
    
   
    @Insert("insert into ph_order_info (order_id, user_id, user_name, amount, hash_val) values "
    		+ "(#{orderId}, #{userId}, #{userName}, #{amount}, #{hashVal}) ")
    void insert(Order order);
     
    @Select("select order_id as orderId, user_id as user_id from ph_order_info where amount > 1 limit 1")
    Order selectOne(); 
    
    @Select("select u.mobile as mobile from ph_order_info o left join ph_user_info  u on o.user_id = u.user_id where o.order_id = #{orderId} ")
    User selectUserByOrderId(long orderId);
}
