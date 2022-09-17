package com.onlineshop.hishop.feign;


import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.pojo.TbMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "hishop-admin")
public interface MemberFeignService {

    @RequestMapping(value = "/admin/member/list",method = RequestMethod.GET)
    public DataTablesResult getMemberList(@RequestParam int draw, @RequestParam int start,
                                          @RequestParam int length, @RequestParam String searchKey,
                                          @RequestParam String minDate, @RequestParam String maxDate,
                                          @RequestParam("search[value]") String search,
                                          @RequestParam("order[0][column]") int orderCol,
                                          @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/member/list/remove",method = RequestMethod.GET)
    public DataTablesResult getDelMemberList(@RequestParam int draw, @RequestParam int start,
                                             @RequestParam int length, @RequestParam String searchKey,
                                             @RequestParam String minDate, @RequestParam String maxDate,
                                             @RequestParam("search[value]") String search,
                                             @RequestParam("order[0][column]") int orderCol,
                                             @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/member/count",method = RequestMethod.GET)
    public DataTablesResult getMemberCount();

    @RequestMapping(value = "/admin/member/count/remove",method = RequestMethod.GET)
    public DataTablesResult getRemoveMemberCount();

    @RequestMapping(value = "/admin/member/add",method = RequestMethod.POST)
    public Result<TbMember> createMember(@ModelAttribute MemberDto memberDto);

    @RequestMapping(value = "/admin/member/freeze/{id}",method = RequestMethod.PUT)
    public Result<TbMember> stopMember(@PathVariable Long id);

    @RequestMapping(value = "/admin/member/block/{id}/{duration}",method = RequestMethod.PUT)
    public Result<Object> blockMember(@PathVariable Long id,@PathVariable String duration);

    @RequestMapping(value = "/admin/member/start/{id}",method = RequestMethod.PUT)
    public Result<TbMember> startMember(@PathVariable Long id);

    @RequestMapping(value = "/admin/member/unblock/{id}",method = RequestMethod.PUT)
    public Result<TbMember> unblock(@PathVariable Long id);

    @RequestMapping(value = "/admin/member/remove/{ids}",method = RequestMethod.PUT)
    public Result<TbMember> removeMember(@PathVariable Long[] ids);

    @RequestMapping(value = "/admin/member/del/{ids}",method = RequestMethod.DELETE)
    public Result<TbMember> deleteMember(@PathVariable Long[] ids);

    @RequestMapping(value = "/admin/member/changePass/{id}",method = RequestMethod.POST)
    public Result<TbMember> changeMemberPassword(@PathVariable Long id,@ModelAttribute MemberDto memberDto);

    @RequestMapping(value = "/admin/member/update/{id}",method = RequestMethod.POST)
    public Result<TbMember> updateMember(@PathVariable Long id,@ModelAttribute MemberDto memberDto);

    @RequestMapping(value = "/admin/member/{id}",method = RequestMethod.GET)
    public Result<TbMember> getMemberById(@PathVariable Long id);

    @RequestMapping(value = "/admin/member/username",method = RequestMethod.GET)
    public Boolean validateUsername(String username);

    @RequestMapping(value = "/admin/member/phone",method = RequestMethod.GET)
    public Boolean validatePhone(String phone);

    @RequestMapping(value = "/admin/member/email",method = RequestMethod.GET)
    public Boolean validateEmail(String email);

    @RequestMapping(value = "/admin/member/edit/{id}/username",method = RequestMethod.GET)
    public Boolean validateEditUsername(@PathVariable Long id,String username);

    @RequestMapping(value = "/admin/member/edit/{id}/phone",method = RequestMethod.GET)
    public Boolean validateEditPhone(@PathVariable Long id,String phone);

    @RequestMapping(value = "/admin/member/edit/{id}/email",method = RequestMethod.GET)
    public Boolean validateEditEmail(@PathVariable Long id,String email);

}
