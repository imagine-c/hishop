package com.onlineshop.hishop.controller;
import com.onlineshop.hishop.annotation.SystemControllerLog;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbPermission;
import com.onlineshop.hishop.pojo.TbRole;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.service.UserService;
import com.onlineshop.hishop.utils.GeetestLib;
import com.onlineshop.hishop.utils.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@RestController
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin/geetestInit",method = RequestMethod.GET)
    public String geetestInit(HttpServletRequest request){

        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);

        resStr = gtSdk.getResponseStr();

        return resStr;
    }


    @RequestMapping(value = "/admin/user/unlock",method = RequestMethod.POST)
    public Result<Object> unLock(@RequestParam String username, @RequestParam String password){
        String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
        if (userService.getUserByUsername(username).getPassword().equals(md5Pass))
            return new ResultUtil<Object>().setData(null);
        return new ResultUtil<Object>().setErrorMsg("密码错误");
    }




    @RequestMapping(value = "/admin/user/roleList",method = RequestMethod.GET)
    public DataTablesResult getRoleList(){

        DataTablesResult result=userService.getRoleList();
        return result;
    }

    @RequestMapping(value = "/admin/user/getAllRoles",method = RequestMethod.GET)
    public Result<List<TbRole>> getAllRoles(){

        List<TbRole> list=userService.getAllRoles();
        return new ResultUtil<List<TbRole>>().setData(list);
    }

    @RequestMapping(value = "/admin/user/roleName",method = RequestMethod.GET)
    public boolean roleName(@RequestParam String name){

        if(userService.getRoleByRoleName(name)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/user/edit/roleName/{id}",method = RequestMethod.GET)
    public boolean roleName(@PathVariable int id, @RequestParam String name){

        return userService.getRoleByEditName(id,name);
    }

    @RequestMapping(value = "/admin/user/addRole",method = RequestMethod.POST)
    public Result<Object> addRole(@ModelAttribute TbRole tbRole){

        userService.addRole(tbRole);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/updateRole",method = RequestMethod.POST)
    public Result<Object> updateRole(@ModelAttribute TbRole tbRole){

        userService.updateRole(tbRole);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/delRole/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delRole(@PathVariable int[] ids){

        for(int id:ids){
            int result=userService.deleteRole(id);
            if(result==0){
                return new ResultUtil<Object>().setErrorMsg("id为"+id+"的角色被使用中，不能删除！");
            }
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/roleCount",method = RequestMethod.GET)
    public Result<Object> getRoleCount(){

        Long result=userService.countRole();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/user/permissionList",method = RequestMethod.GET)
    public DataTablesResult getPermissionList(){

        DataTablesResult result=userService.getPermissionList();
        return result;
    }

    @RequestMapping(value = "/admin/user/addPermission",method = RequestMethod.POST)
    public Result<Object> addPermission(@ModelAttribute TbPermission tbPermission){

        userService.addPermission(tbPermission);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/updatePermission",method = RequestMethod.POST)
    public Result<Object> updatePermission(@ModelAttribute TbPermission tbPermission){

        userService.updatePermission(tbPermission);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/delPermission/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delPermission(@PathVariable int[] ids){

        for(int id:ids){
            userService.deletePermission(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/permissionCount",method = RequestMethod.GET)
    public Result<Object> getPermissionCount(){

        Long result=userService.countPermission();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/user/userList",method = RequestMethod.GET)
    public DataTablesResult getUserList(){

        DataTablesResult result=userService.getUserList();
        return result;
    }

    @RequestMapping(value = "/admin/user/username",method = RequestMethod.GET)
    public boolean getUserByName(@RequestParam String username){

        return userService.getUserByName(username);
    }

    @RequestMapping(value = "/admin/user/phone",method = RequestMethod.GET)
    public boolean getUserByPhone(@RequestParam String phone){

        return userService.getUserByPhone(phone);
    }

    @RequestMapping(value = "/admin/user/email",method = RequestMethod.GET)
    public boolean getUserByEmail(@RequestParam String email){

        return userService.getUserByEmail(email);
    }

    @RequestMapping(value = "/admin/user/addUser",method = RequestMethod.POST)
    public Result<Object> addUser(@ModelAttribute TbUser tbUser){

        userService.addUser(tbUser);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/updateUser",method = RequestMethod.POST)
    public Result<Object> updateUser(@ModelAttribute TbUser tbUser){

        userService.updateUser(tbUser);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/edit/username/{id}",method = RequestMethod.GET)
    public boolean getUserByEditName(@PathVariable Long id, @RequestParam String username){

        return userService.getUserByEditName(id,username);
    }

    @RequestMapping(value = "/admin/user/edit/phone/{id}",method = RequestMethod.GET)
    public boolean getUserByEditPhone(@PathVariable Long id, @RequestParam String phone){

        return userService.getUserByEditPhone(id,phone);
    }

    @RequestMapping(value = "/admin/user/edit/email/{id}",method = RequestMethod.GET)
    public boolean getUserByEditEmail(@PathVariable Long id, @RequestParam String email){

        return userService.getUserByEditEmail(id,email);
    }

    @RequestMapping(value = "/admin/user/stop/{id}",method = RequestMethod.PUT)
    public Result<Object> stopUser(@PathVariable Long id){

        userService.changeUserState(id,0);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/start/{id}",method = RequestMethod.PUT)
    public Result<Object> startUser(@PathVariable Long id){

        userService.changeUserState(id,1);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/changePass",method = RequestMethod.POST)
    public Result<Object> changePass(@ModelAttribute TbUser tbUser){

        userService.changePassword(tbUser);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/delUser/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delUser(@PathVariable Long[] ids){

        for(Long id:ids){
            userService.deleteUser(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/user/userCount",method = RequestMethod.GET)
    public Result<Object> getUserCount(){

        Long result=userService.countUser();
        return new ResultUtil<Object>().setData(result);
    }
}
