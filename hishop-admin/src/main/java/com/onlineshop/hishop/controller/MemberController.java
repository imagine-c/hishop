package com.onlineshop.hishop.controller;


import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.service.MemberService;
import com.onlineshop.hishop.utils.ResultUtil;
import com.onlineshop.hishop.utils.AddDateMinute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class MemberController {

    final static Logger log= LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/admin/member/list",method = RequestMethod.GET)
    public DataTablesResult getMemberList(@RequestParam int draw, @RequestParam int start,
                                          @RequestParam int length, @RequestParam String searchKey,
                                          @RequestParam String minDate, @RequestParam String maxDate,
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

    @RequestMapping(value = "/admin/member/list/remove",method = RequestMethod.GET)
    public DataTablesResult getDelMemberList(@RequestParam int draw, @RequestParam int start,
                                             @RequestParam int length, @RequestParam String searchKey,
                                             @RequestParam String minDate, @RequestParam String maxDate,
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

    @RequestMapping(value = "/admin/member/count",method = RequestMethod.GET)
    public DataTablesResult getMemberCount(){

        return memberService.getMemberCount();
    }

    @RequestMapping(value = "/admin/member/count/remove",method = RequestMethod.GET)
    public DataTablesResult getRemoveMemberCount(){

        return memberService.getRemoveMemberCount();
    }

    @RequestMapping(value = "/admin/member/add",method = RequestMethod.POST)
    public Result<TbMember> createMember(@ModelAttribute MemberDto memberDto){

        TbMember newTbMember = memberService.addMember(memberDto);
        return new ResultUtil<TbMember>().setData(newTbMember);
    }
    @RequestMapping(value = "/admin/member/freeze/{id}",method = RequestMethod.PUT)
    public Result<TbMember> stopMember(@PathVariable Long id){

        TbMember tbMember = memberService.alertMemberState(id,1);
        return new ResultUtil<TbMember>().setData(tbMember);
    }
    @RequestMapping(value = "/admin/member/block/{id}/{duration}",method = RequestMethod.PUT)
    public Result<Object> blockMember(@PathVariable Long id,@PathVariable String duration){
        Date date = new Date();
        Date date1 = new Date();
        if (!("".equals(duration) || duration==null)){
            if (duration.equals("30m"))
                date1 = AddDateMinute.addMinute(date,30);
            if (duration.equals("1h"))
                date1 = AddDateMinute.addHour(date,1);
            if (duration.equals("1d"))
                date1 = AddDateMinute.addDay(date,1);
            if (duration.equals("7d"))
                date1 = AddDateMinute.addDay(date,7);
            if (duration.equals("30M"))
                date1 = AddDateMinute.addDay(date,30);
        }
        if (memberService.blockMember(id,date,date1)!=1)
            return new ResultUtil<Object>().setErrorMsg("封号失败");
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/member/start/{id}",method = RequestMethod.PUT)
    public Result<TbMember> startMember(@PathVariable Long id){
        memberService.alertMemberState(id,3);
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/admin/member/unblock/{id}",method = RequestMethod.PUT)
    public Result<TbMember> unblock(@PathVariable Long id){
        memberService.alertMemberState(id,3);
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/admin/member/remove/{ids}",method = RequestMethod.PUT)
    public Result<TbMember> removeMember(@PathVariable Long[] ids){

        for(Long id:ids){
            memberService.alertMemberState(id,2);
        }
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/admin/member/del/{ids}",method = RequestMethod.DELETE)
    public Result<TbMember> deleteMember(@PathVariable Long[] ids){

        for(Long id:ids){
            memberService.deleteMember(id);
        }
        return new ResultUtil<TbMember>().setData(null);
    }

    @RequestMapping(value = "/admin/member/changePass/{id}",method = RequestMethod.POST)
    public Result<TbMember> changeMemberPassword(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        TbMember tbMember = memberService.changePassMember(id,memberDto);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/admin/member/update/{id}",method = RequestMethod.POST)
    public Result<TbMember> updateMember(@PathVariable Long id,@ModelAttribute MemberDto memberDto){

        TbMember tbMember = memberService.updateMember(id,memberDto);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/admin/member/{id}",method = RequestMethod.GET)
    public Result<TbMember> getMemberById(@PathVariable Long id){

        TbMember tbMember = memberService.getMemberById(id);
        return new ResultUtil<TbMember>().setData(tbMember);
    }

    @RequestMapping(value = "/admin/member/username",method = RequestMethod.GET)
    public Boolean validateUsername(String username){

        if(memberService.getMemberByUsername(username)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/member/phone",method = RequestMethod.GET)
    public Boolean validatePhone(String phone){

        if(memberService.getMemberByPhone(phone)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/member/email",method = RequestMethod.GET)
    public Boolean validateEmail(String email){

        if(memberService.getMemberByEmail(email)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/member/edit/{id}/username",method = RequestMethod.GET)
    public Boolean validateEditUsername(@PathVariable Long id,String username){

        if(memberService.getMemberByEditUsername(id,username)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/member/edit/{id}/phone",method = RequestMethod.GET)
    public Boolean validateEditPhone(@PathVariable Long id,String phone){

        if(memberService.getMemberByEditPhone(id,phone)!=null){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin/member/edit/{id}/email",method = RequestMethod.GET)
    public Boolean validateEditEmail(@PathVariable Long id,String email){

        if(memberService.getMemberByEditEmail(id,email)!=null){
            return false;
        }
        return true;
    }
}
