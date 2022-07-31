package com.onlineshop.hishop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.service.MemberService;
import com.onlineshop.hishop.utils.ResultUtil;
import com.onlineshop.hishop.utils.addDateMinut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(description= "会员管理")
public class MemberController {

    final static Logger log= LoggerFactory.getLogger(MemberController.class);

    @Reference
    private MemberService memberService;

    @RequestMapping(value = "/member/list",method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索获取会员列表")
    public DataTablesResult getMemberList(int draw, int start, int length, String searchKey,
                                          String minDate, String maxDate,
                                          @RequestParam("search[value]") String search,
                                          @RequestParam("order[0][column]") int orderCol,
                                          @RequestParam("order[0][dir]") String orderDir){

        //获取客户端需要排序的列
        String[] cols = {"checkbox","id", "username","sex", "phone", "email", "address", "created", "updated", "state"};
        String orderColumn = cols[orderCol];
        //默认排序列
        if(orderColumn == null) {
            orderColumn = "created";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        if(!search.isEmpty()){
            searchKey=search;
        }
        DataTablesResult result=memberService.getMemberList(draw,start,length,searchKey,minDate,maxDate,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/member/list/remove",method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索已删除会员列表")
    public DataTablesResult getDelMemberList(int draw, int start, int length, String searchKey,
                                             String minDate, String maxDate,
                                             @RequestParam("search[value]") String search,
                                             @RequestParam("order[0][column]") int orderCol,
                                             @RequestParam("order[0][dir]") String orderDir){

        //获取客户端需要排序的列
        String[] cols = {"checkbox","id", "username","sex", "phone", "email", "address", "created", "updated", "state"};
        String orderColumn = cols[orderCol];
        //默认排序列
        if(orderColumn == null) {
            orderColumn = "created";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        if(!search.isEmpty()){
            searchKey=search;
        }
        DataTablesResult result=memberService.getRemoveMemberList(draw,start,length,searchKey,minDate,maxDate,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/member/count",method = RequestMethod.GET)
    @ApiOperation(value = "获得总会员数目")
    public DataTablesResult getMemberCount(){

        return memberService.getMemberCount();
    }

    @RequestMapping(value = "/member/count/remove",method = RequestMethod.GET)
    @ApiOperation(value = "获得移除总会员数目")
    public DataTablesResult getRemoveMemberCount(){

        return memberService.getRemoveMemberCount();
    }

    @RequestMapping(value = "/member/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加会员")
    public Result<TbMember> createMember(@ModelAttribute MemberDto memberDto){

        TbMember newTbMember = memberService.addMember(memberDto);
        return new ResultUtil<TbMember>().setData(newTbMember);
    }
    @RequestMapping(value = "/member/freeze/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "冻结会员")
    public Result<TbMember> stopMember(@PathVariable Long id){

        TbMember tbMember = memberService.alertMemberState(id,1);
        return new ResultUtil<TbMember>().setData(tbMember);
    }
    @RequestMapping(value = "/member/block/{id}/{duration}",method = RequestMethod.PUT)
    @ApiOperation(value = "封号会员")
    public Result<Object> blockMember(@PathVariable Long id,@PathVariable String duration){
        Date date = new Date();
        Date date1 = new Date();
        if (!("".equals(duration) || duration==null)){
            if (duration.equals("30m"))
                date1 = addDateMinut.addMinute(date,30);
            if (duration.equals("1h"))
                date1 = addDateMinut.addHour(date,1);
            if (duration.equals("1d"))
                date1 = addDateMinut.addDay(date,1);
            if (duration.equals("7d"))
                date1 = addDateMinut.addDay(date,7);
            if (duration.equals("30M"))
                date1 = addDateMinut.addDay(date,30);
        }
        if (memberService.blockMember(id,date,date1)!=1)
            return new ResultUtil<Object>().setErrorMsg("封号失败");
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/member/start/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "解除冻结用户")
    public Result<TbMember> startMember(@PathVariable Long id){
        memberService.alertMemberState(id,3);
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/member/unblock/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "解除封号用户")
    public Result<TbMember> unblock(@PathVariable Long id){
        memberService.alertMemberState(id,3);
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/member/remove/{ids}",method = RequestMethod.PUT)
    @ApiOperation(value = "移除会员")
    public Result<TbMember> removeMember(@PathVariable Long[] ids){

        for(Long id:ids){
            memberService.alertMemberState(id,2);
        }
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/member/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "彻底删除会员")
    public Result<TbMember> deleteMember(@PathVariable Long[] ids){

        for(Long id:ids){
            memberService.deleteMember(id);
        }
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/member/changePass/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "修改会员密码")
    public Result<TbMember> changeMemberPassword(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        TbMember tbMember = memberService.changePassMember(id,memberDto);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/member/update/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "修改会员信息")
    public Result<TbMember> updateMember(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        TbMember tbMember = memberService.updateMember(id,memberDto);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/member/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "通过ID获取会员信息")
    public Result<TbMember> getMemberById(@PathVariable Long id){

        TbMember tbMember = memberService.getMemberById(id);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/member/username",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册名是否存在")
    public Boolean validateUsername(String username){

        if(memberService.getMemberByUsername(username)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/member/phone",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册手机是否存在")
    public Boolean validatePhone(String phone){

        if(memberService.getMemberByPhone(phone)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/member/email",method = RequestMethod.GET)
    @ApiOperation(value = "验证注册邮箱是否存在")
    public Boolean validateEmail(String email){

        if(memberService.getMemberByEmail(email)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/member/edit/{id}/username",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑用户名是否存在")
    public Boolean validateEditUsername(@PathVariable Long id,String username){

        if(memberService.getMemberByEditUsername(id,username)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/member/edit/{id}/phone",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑手机是否存在")
    public Boolean validateEditPhone(@PathVariable Long id,String phone){

        if(memberService.getMemberByEditPhone(id,phone)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/member/edit/{id}/email",method = RequestMethod.GET)
    @ApiOperation(value = "验证编辑邮箱是否存在")
    public Boolean validateEditEmail(@PathVariable Long id,String email){

        if(memberService.getMemberByEditEmail(id,email)!=null){
            return false;
        }
        return true;
    }
}
