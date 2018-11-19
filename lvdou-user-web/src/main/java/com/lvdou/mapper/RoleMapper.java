package com.lvdou.mapper;

import com.lvdou.pojo.Role;
import tk.mybatis.mapper.common.Mapper;

/**
 * RoleMapper 数据访问接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2018-11-13 13:38:05
 * @version 1.0
 */
public interface RoleMapper extends Mapper<Role>{


    Role selectRoleByRole(Integer roleid);
}