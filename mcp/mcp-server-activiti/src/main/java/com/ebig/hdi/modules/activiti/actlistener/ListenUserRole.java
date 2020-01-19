
package com.ebig.hdi.modules.activiti.actlistener;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.sys.entity.SysRoleEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysUserRoleService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author wenchao
 * @date 2019/11/11.
 * @email 154040976@qq.com
 * <p>
 * 切入实现系统用户 角色 用户-角色 同步到 activiti 用户 组 用户组 同步到工作流,模块化 无侵入
 */
@Aspect
@Component
public class ListenUserRole {

    @Autowired
    IdentityService identityService;

    @Autowired
    SysUserRoleService userRoleService;

    /**********************用户处理begin***************************/
    /**
     * 明确切入方法的参数
     *
     * @param joinPoint
     */
    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysUserController.update(*))")
    public Object listenerUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o;
        //更新前拿到用户-角色数据
        Object[] args = joinPoint.getArgs();
        Long userId = ((SysUserEntity) args[0]).getUserId();
        List<Long> strings = userRoleService.queryRoleIdList(userId);
        o = joinPoint.proceed(joinPoint.getArgs());
        Hdi hdi = (Hdi) o;
        if ("0".equals(hdi.get("code"))) {
            changeUser(args, strings);
        }
        return o;
    }

    /**
     * 新增用户监听 同步工作流用户表 环绕注解能得到 插入用户id
     *
     * @param joinPoint
     */
    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysUserController.save(*))")
    public Object listenerUserInsert(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed(joinPoint.getArgs());
        Object[] args = joinPoint.getArgs();
        if (args.length == 2) {
            Hdi hdi = (Hdi) o;
            if ("0".equals(hdi.get("code"))) {
                changeUser(args, Arrays.asList((Long[]) args[1]));
            }
        }
        return o;
    }

    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysUserController.delete(Long[]))")
    public Object listenDelUser(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed(point.getArgs());
        Hdi hdi = (Hdi) o;
        if ("0".equals(hdi.get("code"))) {
            Object[] args = point.getArgs();
            identityService.deleteUser((String) args[0]);
        }
        return o;
    }


    /**
     * 保存进 activiti 用户 角色 用户角色中间表
     *
     * @param obj
     */
    private void changeUser(Object[] obj, List<Long> strings) {
        SysUserEntity user = (SysUserEntity) obj[0];
        identityService.deleteUser(user.getUserId().toString());
        User au = new UserEntity();
        au.setId(user.getUserId().toString());
        au.setFirstName(user.getUsername());
        au.setEmail(user.getEmail());
        identityService.saveUser(au);

        //删除用户-组关联
        for (Long roleId : strings) {
            identityService.deleteMembership(user.getUserId().toString(), roleId.toString());
        }
        //再次关联
        if (!strings.isEmpty()) {
            for (Long roleId : strings) {
                identityService.createMembership(user.getUserId().toString(), roleId.toString());
            }
        }
    }

    /**********************用户处理end***************************/


    /**********************
     * 角色处理begin
     ***************************/
    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysRoleController.save(*))")
    public Object listenRoleInsert(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed(joinPoint.getArgs());
        Hdi hdi = (Hdi) o;
        if ("0".equals(hdi.get("code"))) {
            Object[] args = joinPoint.getArgs();
            changeRole(args);
        }
        return o;
    }

    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysRoleController.update(*))")
    public Object listenRoleUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed(joinPoint.getArgs());
        Object[] args = joinPoint.getArgs();
        if (args.length == 2) {
            Hdi hdi = (Hdi) o;
            if ("0".equals(hdi.get("code"))) {
                changeRole(args);
            }
        }

        return o;
    }

    @Around("execution(com.ebig.hdi.common.utils.Hdi com.ebig.hdi.modules.sys.controller.SysRoleController.delete(..))")
    public Object listenDelRole(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed(point.getArgs());
        Hdi hdi = (Hdi) o;
        if ("0".equals(hdi.get("code"))) {
            Object[] args = point.getArgs();
            identityService.deleteGroup((String) args[0]);
        }
        return o;
    }

    /**
     * 更新进组
     *
     * @param obj
     */
    public void changeRole(Object[] obj) {
        SysRoleEntity role = (SysRoleEntity) obj[0];
        identityService.deleteGroup(role.getRoleId().toString());
        Group group = new GroupEntity();
        group.setId(role.getRoleId().toString());
        group.setName(role.getRoleName());
        identityService.saveGroup(group);
    }
    /**********************角色处理end***************************/
}
