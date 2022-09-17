package com.onlineshop.hishop.shiro;

import com.onlineshop.hishop.exception.UserLockException;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Log4j2
public class UserRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;

    /**
     * 返回权限信息
     *
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {

        log.warn("用户授权--");
        //获取用户名
        TbUser user = (TbUser) principal.getPrimaryPrincipal();
        log.warn("获取用户名------ "+user.getUsername());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        log.warn("获得授权角色------");
        //获得授权角色
        authorizationInfo.setRoles(userService.getRoles(user.getUsername()));
        log.warn("获得授权权限------");
        //获得授权权限
        authorizationInfo.setStringPermissions(userService.getPermissions(user.getUsername()));
        return authorizationInfo;
    }

    /**
     * 先执行登录验证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException{
        log.warn("用户认证--");
        //获取用户名密码
        log.warn("用户名密码");
        UsernamePasswordToken Token = (UsernamePasswordToken) token;
        TbUser tbUser = userService.getUserByUsername(Token.getUsername());
        if (tbUser != null) {
            if (tbUser.getState() == 0)
                throw  new UserLockException("用户已锁定，请联系管理员");
            singlePortLogin(Token);
            //得到用户账号和密码存放到authenticationInfo中用于Controller层的权限判断 第三个参数随意不能为null
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(tbUser, tbUser.getPassword(),
                    tbUser.getUsername());
            return authenticationInfo;
        } else {
            return null;
        }
    }

    /**
     * 实现单用户登录
     * 若用户已经登录则挤掉前个登录状态
     *
     * @param token 身份认证令牌
     */
    private void singlePortLogin(UsernamePasswordToken token) {
        // 清除认证缓存信息
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        SessionManager sessionManager = (SessionManager) securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        TbUser user;
        for (Session session : sessions) {
            Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }

            user = (TbUser) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            //清除该用户以前登录时保存的session，强制退出
            if (token.getUsername().equals(user.getUsername())) {
                sessionManager.getSessionDAO().delete(session);
            }
        }
    }

}
