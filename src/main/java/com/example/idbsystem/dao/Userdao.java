package com.example.idbsystem.dao;

import com.example.idbsystem.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;


@Mapper
@EnableCaching
public interface Userdao {

    @Update("update user set password=#{password} where username=#{username}")
    boolean updateuser(User user);

    @Select("select username from user where username=#{username} and password=#{password}")
    String selectuser(User user);

    /**
     * @Cacheable ，作用在方法上，触发缓存读取操作
     * @CacheEvict ，作用在方法上，触发缓存失效操作
     * @CachePut ，作用在方法上，触发缓存更新操作。
     * @Cache ，作用在方法上，综合上面的各种操作，在有些场景下 ，调用业务会触发多种缓存操作。
     * @CacheConfig ，在类上设置当前缓存 一些公共设置。
     */
//    @Cacheable(value="user", key="'selectByName_'+#name")   //value：往哪一块缓存中存数据
    @Cacheable(value="user", keyGenerator="cacheKeyGenerator")   //value：往哪一块缓存中存数据
    @Select("select password from user where username=#{name}")
    String selectByName(String name);
}
