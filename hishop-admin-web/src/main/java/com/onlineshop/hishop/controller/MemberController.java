package com.onlineshop.hishop.controller;


import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.feign.MemberFeignService;
import com.onlineshop.hishop.pojo.TbMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Log4j2
@Api(description= "会员管理")
public class MemberController {

    @Autowired
    private MemberFeignService memberFeignService;

    @RequestMapping(value = "/member/list",method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索获取会员列表")
    public DataTablesResult getMemberList(@RequestParam int draw, @RequestParam int start,
                                          @RequestParam int length, @RequestParam String searchKey,
                                          @RequestParam String minDate, @RequestParam String maxDate,
                                          @RequestParam("search[value]") String search,
                                          @RequestParam("order[0][column]") int orderCol,
                                          @RequestParam("order[0][dir]") String orderDir){

        return memberFeignService.getMemberList(draw,start,length,searchKey,minDate,maxDate,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/member/list/remove",method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索已删除会员列表")
    public DataTablesResult getDelMemberList(@RequestParam int draw, @RequestParam int start,
                                             @RequestParam int length, @RequestParam String searchKey,
                                             @RequestParam String minDate, @RequestParam String maxDate,
                                             @RequestParam("search[value]") String search,
                                             @RequestParam("order[0][column]") int orderCol,
                                             @RequestParam("order[0][dir]") String orderDir){

        return memberFeignService.getDelMemberList(draw,start,length,searchKey,minDate,maxDate,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/member/count",method = RequestMethod.GET)
    @ApiOperation(value = "获得总会员数目")
    public DataTablesResult getMemberCount(){

        return memberFeignService.getMemberCount();
    }

    @RequestMapping(value = "/member/count/remove",method = RequestMethod.GET)
    @ApiOperation(value = "获得移除总会员数目")
    public DataTablesResult getRemoveMemberCount(){

        return memberFeignService.getRemoveMemberCount();
    }

    @RequestMapping(value = "/member/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加会员")
    public Result<TbMember> createMember(@ModelAttribute MemberDto memberDto){

        return  memberFeignService.createMember(memberDto);
    }
    @RequestMapping(value = "/member/freeze/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "冻结会员")
    public Result<TbMember> stopMember(@PathVariable Long id){

        return memberFeignService.stopMember(id);
    }
    @RequestMapping(value = "/member/block/{id}/{duration}",method = RequestMethod.PUT)
    @ApiOperation(value = "封号会员")
    public Result<Object> blockMember(@PathVariable Long id,@PathVariable String duration){

        return memberFeignService.blockMember(id,duration);
    }

    @RequestMapping(value = "/member/start/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "解除冻结用户")
    public Result<TbMember> startMember(@PathVariable Long id){

        return memberFeignService.startMember(id);
    }

    @RequestMapping(value = "/member/unblock/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "解除封号用户")
    public Result<TbMember> unblock(@PathVariable Long id){

        return  memberFeignService.unblock(id);
    }

    @RequestMapping(value = "/member/remove/{ids}",method = RequestMethod.PUT)
    @ApiOperation(value = "移除会员")
    public Result<TbMember> removeMember(@PathVariable Long[] ids){

        return memberFeignService.removeMember(ids);
    }

    @RequestMapping(value = "/member/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "彻底删除会员")
    public Result<TbMember> deleteMember(@PathVariable Long[] ids){

        return memberFeignService.deleteMember(ids);
    }

    @RequestMapping(value = "/member/changePass/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "修改会员密码")
    public Result<TbMember> changeMemberPassword(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        return memberFeignService.changeMemberPassword(id,memberDto);
    }

    @RequestMapping(value = "/member/update/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "修改会员信息")
    public Result<TbMember> updateMember(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        return memberFeignService.updateMember(id,memberDto);
    }

    @RequestMapping(value = "/member/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "通过ID获取会员信息")
    public Result<TbMember> getMemberById(@PathVariable Long id){

        return memberFeignService.getMemberById(id);
    }

    @RequestMapping(value = "/member/username",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册名是否存在")
    public Boolean validateUsername(String username){

       return memberFeignService.validateUsername(username);
    }

    @RequestMapping(value = "/member/phone",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册手机是否存在")
    public Boolean validatePhone(String phone){

        return memberFeignService.validatePhone(phone);
    }

    @RequestMapping(value = "/member/email",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册邮箱是否存在")
    public Boolean validateEmail(String email){

        return memberFeignService.validateEmail(email);
    }

    @RequestMapping(value = "/member/edit/{id}/username",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑用户名是否存在")
    public Boolean validateEditUsername(@PathVariable Long id,String username){

        return memberFeignService.validateEditUsername(id,username);
    }

    @RequestMapping(value = "/member/edit/{id}/phone",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑手机是否存在")
    public Boolean validateEditPhone(@PathVariable Long id,String phone){

        return memberFeignService.validateEditPhone(id,phone);
    }

    @RequestMapping(value = "/member/edit/{id}/email",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑邮箱是否存在")
    public Boolean validateEditEmail(@PathVariable Long id,String email){

        return memberFeignService.validateEditEmail(id,email);
    }
}
