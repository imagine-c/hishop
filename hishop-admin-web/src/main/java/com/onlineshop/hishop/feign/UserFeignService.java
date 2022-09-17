package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbPermission;
import com.onlineshop.hishop.pojo.TbRole;
import com.onlineshop.hishop.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
@FeignClient(value = "hishop-admin")
public interface UserFeignService {

    @RequestMapping(value = "/admin/geetestInit",method = RequestMethod.GET)
    public String geetestInit(HttpServletRequest request);

    @RequestMapping(value = "/admin/user/unlock",method = RequestMethod.POST)
    public Result<Object> unLock(@RequestParam String username, @RequestParam String password);

    @RequestMapping(value = "/admin/user/roleList",method = RequestMethod.GET)
    public DataTablesResult getRoleList();

    @RequestMapping(value = "/admin/user/getAllRoles",method = RequestMethod.GET)
    public Result<List<TbRole>> getAllRoles();

    @RequestMapping(value = "/admin/user/roleName",method = RequestMethod.GET)
    public boolean roleName(@RequestParam String name);

    @RequestMapping(value = "/admin/user/edit/roleName/{id}",method = RequestMethod.GET)
    public boolean roleName(@PathVariable int id, @RequestParam String name);

    @RequestMapping(value = "/admin/user/addRole",method = RequestMethod.POST)
    public Result<Object> addRole(@ModelAttribute TbRole tbRole);

    @RequestMapping(value = "/admin/user/updateRole",method = RequestMethod.POST)
    public Result<Object> updateRole(@ModelAttribute TbRole tbRole);

    @RequestMapping(value = "/admin/user/delRole/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delRole(@PathVariable int[] ids);

    @RequestMapping(value = "/admin/user/roleCount",method = RequestMethod.GET)
    public Result<Object> getRoleCount();

    @RequestMapping(value = "/admin/user/permissionList",method = RequestMethod.GET)
    public DataTablesResult getPermissionList();

    @RequestMapping(value = "/admin/user/addPermission",method = RequestMethod.POST)
    public Result<Object> addPermission(@ModelAttribute TbPermission tbPermission);

    @RequestMapping(value = "/admin/user/updatePermission",method = RequestMethod.POST)
    public Result<Object> updatePermission(@ModelAttribute TbPermission tbPermission);

    @RequestMapping(value = "/admin/user/delPermission/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delPermission(@PathVariable int[] ids);

    @RequestMapping(value = "/admin/user/permissionCount",method = RequestMethod.GET)
    public Result<Object> getPermissionCount();

    @RequestMapping(value = "/admin/user/userList",method = RequestMethod.GET)
    public DataTablesResult getUserList();

    @RequestMapping(value = "/admin/user/username",method = RequestMethod.GET)
    public boolean getUserByName(@RequestParam String username);

    @RequestMapping(value = "/admin/user/phone",method = RequestMethod.GET)
    public boolean getUserByPhone(@RequestParam String phone);

    @RequestMapping(value = "/admin/user/email",method = RequestMethod.GET)
    public boolean getUserByEmail(@RequestParam String email);

    @RequestMapping(value = "/admin/user/addUser",method = RequestMethod.POST)
    public Result<Object> addUser(@ModelAttribute TbUser tbUser);

    @RequestMapping(value = "/admin/user/updateUser",method = RequestMethod.POST)
    public Result<Object> updateUser(@ModelAttribute TbUser tbUser);

    @RequestMapping(value = "/admin/user/edit/username/{id}",method = RequestMethod.GET)
    public boolean getUserByEditName(@PathVariable Long id, @RequestParam String username);

    @RequestMapping(value = "/admin/user/edit/phone/{id}",method = RequestMethod.GET)
    public boolean getUserByEditPhone(@PathVariable Long id, @RequestParam String phone);

    @RequestMapping(value = "/admin/user/edit/email/{id}",method = RequestMethod.GET)
    public boolean getUserByEditEmail(@PathVariable Long id, @RequestParam String email);

    @RequestMapping(value = "/admin/user/stop/{id}",method = RequestMethod.PUT)
    public Result<Object> stopUser(@PathVariable Long id);

    @RequestMapping(value = "/admin/user/start/{id}",method = RequestMethod.PUT)
    public Result<Object> startUser(@PathVariable Long id);

    @RequestMapping(value = "/admin/user/changePass",method = RequestMethod.POST)
    public Result<Object> changePass(@ModelAttribute TbUser tbUser);

    @RequestMapping(value = "/admin/user/delUser/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delUser(@PathVariable Long[] ids);

    @RequestMapping(value = "/admin/user/userCount",method = RequestMethod.GET)
    public Result<Object> getUserCount();
}
