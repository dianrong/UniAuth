package com.dianrong.common.uniauth.server.data.mapper;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.UserPwdLog;

public interface UserPwdLogMapper {
    /**.
     * 插入新的记录
     * @param log
     * @return
     */
    int insert(UserPwdLog log);
    
    /**.
     * 根据条件查询数据
     * @param condition
     * @return
     */
    List<UserPwdLog> queryUserPwdLogs(UserPwdLog condition);
}