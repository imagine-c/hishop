package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.annotation.SystemControllerLog;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.AuthFeignService;
import com.onlineshop.hishop.feign.UserFeignService;
import com.onlineshop.hishop.pojo.TbPermission;
import com.onlineshop.hishop.pojo.TbRole;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.utils.GeetestLib;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@Log4j2
@Api(description= "管理员管理")
public class UserController {

    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private AuthFeignService authFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/geetestInit",method = RequestMethod.GET)
    @ApiOperation(value = "极验初始化")
    public String geetestInit(){

        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到session中

        redisTemplate.opsForValue().set(gtSdk.gtServerStatusSessionKey, String.valueOf(gtServerStatus), 30, TimeUnit.MINUTES);

        resStr = gtSdk.getResponseStr();

        return resStr;
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    @SystemControllerLog(description="登录系统")
    public Result<TbUser> login(@RequestParam String username, @RequestParam String password,
                                @RequestParam String challenge, @RequestParam String validate,
                                @RequestParam String seccode){

        return authFeignService.login(username,password,challenge,validate,seccode);
    }

    @RequestMapping(value = "/user/unlock",method = RequestMethod.POST)
    @ApiOperation(value = "解除锁屏")
    public Result<Object> unLock(@RequestParam String username, @RequestParam String password){

        return userFeignService.unLock(username,password);
    }


    @RequestMapping(value = "/user/logout",method = RequestMethod.GET)
    @ApiOperation(value = "退出登录")
    public Result<Object> logout(){

        return authFeignService.logout();
    }

    @RequestMapping(value = "/user/userInfo",method = RequestMethod.GET)
    @ApiOperation(value = "获取登录用户信息")
    public Result<TbUser> getUserInfo(@RequestHeader("Authorization") String token){
        return authFeignService.getUserInfo();
    }

    @RequestMapping(value = "/user/roleList",method = RequestMethod.GET)
    @ApiOperation(value = "获取角色列表")
    public DataTablesResult getRoleList(){

        return userFeignService.getRoleList();
    }

    @RequestMapping(value = "/user/getAllRoles",method = RequestMethod.GET)
    @ApiOperation(value = "获取所有角色")
    public Result<List<TbRole>> getAllRoles(){

        return userFeignService.getAllRoles();
    }

    @RequestMapping(value = "/user/roleName",method = RequestMethod.GET)
    @ApiOperation(value = "判断角色是否已存在")
    public boolean roleName(@RequestParam String name){

        return userFeignService.roleName(name);
    }

    @RequestMapping(value = "/user/edit/roleName/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "判断编辑角色是否已存在")
    public boolean roleName(@PathVariable int id, @RequestParam String name){

        return userFeignService.roleName(id,name);
    }

    @RequestMapping(value = "/user/addRole",method = RequestMethod.POST)
    @ApiOperation(value = "添加角色")
    public Result<Object> addRole(@ModelAttribute TbRole tbRole){

        return userFeignService.addRole(tbRole);
    }

    @RequestMapping(value = "/user/updateRole",method = RequestMethod.POST)
    @ApiOperation(value = "更新角色")
    public Result<Object> updateRole(@ModelAttribute TbRole tbRole){

        return userFeignService.updateRole(tbRole);
    }

    @RequestMapping(value = "/user/delRole/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除角色")
    public Result<Object> delRole(@PathVariable int[] ids){

        return userFeignService.delRole(ids);
    }

    @RequestMapping(value = "/user/roleCount",method = RequestMethod.GET)
    @ApiOperation(value = "统计角色数")
    public Result<Object> getRoleCount(){

        return userFeignService.getRoleCount();
    }

    @RequestMapping(value = "/user/permissionList",method = RequestMethod.GET)
    @ApiOperation(value = "获取权限列表")
    public DataTablesResult getPermissionList(){

        return userFeignService.getPermissionList();
    }

    @RequestMapping(value = "/user/addPermission",method = RequestMethod.POST)
    @ApiOperation(value = "添加权限")
    public Result<Object> addPermission(@ModelAttribute TbPermission tbPermission){

        return userFeignService.addPermission(tbPermission);
    }

    @RequestMapping(value = "/user/updatePermission",method = RequestMethod.POST)
    @ApiOperation(value = "更新权限")
    public Result<Object> updatePermission(@ModelAttribute TbPermission tbPermission){

        return userFeignService.updatePermission(tbPermission);
    }

    @RequestMapping(value = "/user/delPermission/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除权限")
    public Result<Object> delPermission(@PathVariable int[] ids){

        return userFeignService.delPermission(ids);
    }

    @RequestMapping(value = "/user/permissionCount",method = RequestMethod.GET)
    @ApiOperation(value = "统计权限数")
    public Result<Object> getPermissionCount(){

        return userFeignService.getPermissionCount();
    }

    @RequestMapping(value = "/user/userList",method = RequestMethod.GET)
    @ApiOperation(value = "获取用户列表")
    public DataTablesResult getUserList(){

        return userFeignService.getUserList();
    }

    @RequestMapping(value = "/user/username",method = RequestMethod.GET)
    @ApiOperation(value = "判断用户名是否存在")
    public boolean getUserByName(@RequestParam String username){

        return userFeignService.getUserByName(username);
    }

    @RequestMapping(value = "/user/phone",method = RequestMethod.GET)
    @ApiOperation(value = "判断手机是否存在")
    public boolean getUserByPhone(@RequestParam String phone){

        return userFeignService.getUserByPhone(phone);
    }

    @RequestMapping(value = "/user/email",method = RequestMethod.GET)
    @ApiOperation(value = "判断邮箱是否存在")
    public boolean getUserByEmail(@RequestParam String email){

        return userFeignService.getUserByEmail(email);
    }

    @RequestMapping(value = "/user/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    public Result<Object> addUser(@ModelAttribute TbUser tbUser){

        return userFeignService.addUser(tbUser);
    }

    @RequestMapping(value = "/user/updateUser",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户")
    public Result<Object> updateUser(@ModelAttribute TbUser tbUser){

        userFeignService.updateUser(tbUser);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/edit/username/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "判断编辑用户名是否存在")
    public boolean getUserByEditName(@PathVariable Long id, @RequestParam String username){

        return userFeignService.getUserByEditName(id,username);
    }

    @RequestMapping(value = "/user/edit/phone/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "判断编辑手机是否存在")
    public boolean getUserByEditPhone(@PathVariable Long id, @RequestParam String phone){

        return userFeignService.getUserByEditPhone(id,phone);
    }

    @RequestMapping(value = "/user/edit/email/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "判断编辑用户名是否存在")
    public boolean getUserByEditEmail(@PathVariable Long id, @RequestParam String email){

        return userFeignService.getUserByEditEmail(id,email);
    }

    @RequestMapping(value = "/user/stop/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "停用用户")
    public Result<Object> stopUser(@PathVariable Long id){

        return userFeignService.stopUser(id);
    }

    @RequestMapping(value = "/user/start/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "启用用户")
    public Result<Object> startUser(@PathVariable Long id){

        return userFeignService.startUser(id);
    }

    @RequestMapping(value = "/user/changePass",method = RequestMethod.POST)
    @ApiOperation(value = "修改用户密码")
    public Result<Object> changePass(@ModelAttribute TbUser tbUser){

        return userFeignService.changePass(tbUser);
    }

    @RequestMapping(value = "/user/delUser/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户")
    public Result<Object> delUser(@PathVariable Long[] ids){

        return userFeignService.delUser(ids);
    }

    @RequestMapping(value = "/user/userCount",method = RequestMethod.GET)
    @ApiOperation(value = "统计用户数")
    public Result<Object> getUserCount(){

        return userFeignService.getUserCount();
    }
}
