package com.onlineshop.hishop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbMemberMapper;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.pojo.TbMemberExample;
import com.onlineshop.hishop.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    final static Logger log= LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private TbMemberMapper tbMemberMapper;

    @Override
    public TbMember getMemberById(long memberId) {

        TbMember tbMember;
        try {
            tbMember=tbMemberMapper.selectByPrimaryKey(memberId);
        }catch (Exception e){
            throw new HiShopException("ID获取会员信息失败");
        }
        tbMember.setPassword("");
        return tbMember;
    }

    @Override
    public DataTablesResult getMemberList(int draw, int start, int length, String search,
                                          String minDate, String maxDate, String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        try{
            //分页
            PageHelper.startPage(start/length+1,length);
            List<TbMember> list = tbMemberMapper.selectByMemberInfo("%"+search+"%",minDate,maxDate,orderCol,orderDir);
            PageInfo<TbMember> pageInfo=new PageInfo<>(list);

            for(TbMember tbMember:list){
                tbMember.setPassword("");
            }

            result.setRecordsFiltered((int)pageInfo.getTotal());
            result.setRecordsTotal(getMemberCount().getRecordsTotal());

            result.setDraw(draw);
            result.setData(list);
        }catch (Exception e){
            throw new HiShopException("加载用户列表失败");
        }

        return result;
    }

    @Override
    public DataTablesResult getRemoveMemberList(int draw, int start, int length, String search,
                                                String minDate, String maxDate, String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        try{
            //分页执行查询返回结果
            PageHelper.startPage(start/length+1,length);
            List<TbMember> list = tbMemberMapper.selectByRemoveMemberInfo("%"+search+"%",minDate,maxDate,orderCol,orderDir);
            PageInfo<TbMember> pageInfo=new PageInfo<>(list);

            for(TbMember tbMember:list){
                tbMember.setPassword("");
            }

            result.setRecordsFiltered((int)pageInfo.getTotal());
            result.setRecordsTotal(getRemoveMemberCount().getRecordsTotal());

            result.setDraw(draw);
            result.setData(list);
        }catch (Exception e){
            throw new HiShopException("加载删除用户列表失败");
        }

        return result;
    }

    @Override
    public TbMember getMemberByUsername(String username) {

        List<TbMember> list;
        TbMemberExample example=new TbMemberExample();
        TbMemberExample.Criteria criteria=example.createCriteria();
        criteria.andUsernameEqualTo(username);
        try {
            list=tbMemberMapper.selectByExample(example);
        }catch (Exception e){
            throw new HiShopException("ID获取会员信息失败");
        }
        if(!list.isEmpty()){
            list.get(0).setPassword("");
            return list.get(0);
        }
        return null;
    }

    @Override
    public TbMember getMemberByPhone(String phone) {

        List<TbMember> list;
        TbMemberExample example=new TbMemberExample();
        TbMemberExample.Criteria criteria=example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        try {
            list=tbMemberMapper.selectByExample(example);
        }catch (Exception e){
            throw new HiShopException("Phone获取会员信息失败");
        }
        if(!list.isEmpty()){
            list.get(0).setPassword("");
            return list.get(0);
        }
        return null;
    }

    @Override
    public TbMember getMemberByEmail(String email) {

        List<TbMember> list;
        TbMemberExample example=new TbMemberExample();
        TbMemberExample.Criteria criteria=example.createCriteria();
        criteria.andEmailEqualTo(email);
        try {
            list=tbMemberMapper.selectByExample(example);
        }catch (Exception e){
            throw new HiShopException("Email获取会员信息失败");
        }
        if(!list.isEmpty()){
            list.get(0).setPassword("");
            return list.get(0);
        }
        return null;
    }

    @Override
    public DataTablesResult getMemberCount(){

        DataTablesResult result=new DataTablesResult();
        TbMemberExample example=new TbMemberExample();
        TbMemberExample.Criteria criteria=example.createCriteria();
        criteria.andStateNotEqualTo(0);
        try{
            result.setRecordsTotal((int)tbMemberMapper.countByExample(example));
        }catch (Exception e){
            throw new HiShopException("统计会员数失败");
        }

        return result;
    }

    @Override
    public DataTablesResult getRemoveMemberCount(){

        DataTablesResult result=new DataTablesResult();
        TbMemberExample example=new TbMemberExample();
        TbMemberExample.Criteria criteria=example.createCriteria();
        criteria.andStateEqualTo(2);
        try{
            result.setRecordsTotal((int)tbMemberMapper.countByExample(example));
        }catch (Exception e){
            throw new HiShopException("统计移除会员数失败");
        }

        return result;
    }

    @Override
    public TbMember addMember(MemberDto memberDto) {

        TbMember tbMember= DtoUtil.MemberDto2Member(memberDto);

        if(getMemberByUsername(tbMember.getUsername())!=null){
            throw new HiShopException("用户名已被注册");
        }
        if(getMemberByPhone(tbMember.getPhone())!=null){
            throw new HiShopException("手机号已被注册");
        }
        if(getMemberByEmail(tbMember.getEmail())!=null){
            throw new HiShopException("邮箱已被注册");
        }

        tbMember.setState(1);
        tbMember.setCreated(new Date());
        tbMember.setUpdated(new Date());
        String md5Pass = DigestUtils.md5DigestAsHex(tbMember.getPassword().getBytes());
        tbMember.setPassword(md5Pass);

        if(tbMemberMapper.insert(tbMember)!=1){
            throw new HiShopException("添加用户失败");
        }
        return getMemberByPhone(tbMember.getPhone());
    }

    @Override
    public TbMember updateMember(Long id, MemberDto memberDto) {

        TbMember tbMember = DtoUtil.MemberDto2Member(memberDto);
        tbMember.setId(id);
        tbMember.setUpdated(new Date());
        TbMember oldMember=getMemberById(id);
        tbMember.setState(oldMember.getState());
        tbMember.setCreated(oldMember.getCreated());
        if(tbMember.getPassword()==null||tbMember.getPassword()==""){
            tbMember.setPassword(oldMember.getPassword());
        }else{
            String md5Pass = DigestUtils.md5DigestAsHex(tbMember.getPassword().getBytes());
            tbMember.setPassword(md5Pass);
        }

        if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1){
            throw new HiShopException("更新会员信息失败");
        }
        return getMemberById(id);
    }

    @Override
    public TbMember changePassMember(Long id, MemberDto memberDto) {

        TbMember tbMember=tbMemberMapper.selectByPrimaryKey(id);

        String md5Pass = DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes());
        tbMember.setPassword(md5Pass);
        tbMember.setUpdated(new Date());

        if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1){
            throw new HiShopException("修改会员密码失败");
        }
        return getMemberById(id);
    }

    @Override
    public TbMember alertMemberState(Long id, Integer state) {

        TbMember tbMember = tbMemberMapper.selectByPrimaryKey(id);
        tbMember.setState(state);
        tbMember.setUpdated(new Date());

        if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1){
            throw new HiShopException("修改会员状态失败");
        }
        return getMemberById(id);
    }

    @Override
    public TbMember unBlockMember(Long id) {
        TbMember tbMember = tbMemberMapper.selectByPrimaryKey(id);
        tbMember.setState(3);
        tbMember.setUpdated(new Date());
        tbMember.setBlockDate(null);
        tbMember.setUnblockDate(null);
        if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1){
            throw new HiShopException("修改会员状态失败");
        }
        return getMemberById(id);
    }

    @Override
    public int deleteMember(Long id) {

        if(tbMemberMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除会员失败");
        }
        return 0;
    }

    @Override
    public TbMember getMemberByEditEmail(Long id, String email) {

        TbMember tbMember=getMemberById(id);
        TbMember newTbMember=null;
        if(tbMember.getEmail()==null||!tbMember.getEmail().equals(email)){
            newTbMember=getMemberByEmail(email);
        }
        newTbMember.setPassword("");
        return newTbMember;
    }

    @Override
    public TbMember getMemberByEditPhone(Long id, String phone) {

        TbMember tbMember=getMemberById(id);
        TbMember newTbMember=null;
        if(tbMember.getPhone()==null||!tbMember.getPhone().equals(phone)){
            newTbMember=getMemberByPhone(phone);
        }
        newTbMember.setPassword("");
        return newTbMember;
    }

    @Override
    public TbMember getMemberByEditUsername(Long id, String username) {

        TbMember tbMember=getMemberById(id);
        TbMember newTbMember=null;
        if(tbMember.getUsername()==null||!tbMember.getUsername().equals(username)){
            newTbMember=getMemberByUsername(username);
        }
        newTbMember.setPassword("");
        return newTbMember;
    }

    @Override
    public int blockMember(Long id,Date BlockDate,Date unBlockDate) {
        TbMember member = tbMemberMapper.selectByPrimaryKey(id);
        member.setUpdated(BlockDate);
        member.setBlockDate(BlockDate);
        member.setUnblockDate(unBlockDate);
        member.setState(2);
        return tbMemberMapper.updateByPrimaryKey(member);
    }

    @Override
    public int cancelUserDel() {
        TbMemberExample example  = new TbMemberExample();
        TbMemberExample.Criteria cri = example.createCriteria();
        cri.andStateEqualTo(0);
        List<TbMember> members = tbMemberMapper.selectByExample(example);
        for (TbMember member:members){
            judgeUserDel(member);
        }
        return 1;
    }

    @Override
    public int cancelUserBlock() {
        TbMemberExample example  = new TbMemberExample();
        TbMemberExample.Criteria cri = example.createCriteria();
        cri.andStateEqualTo(2);
        List<TbMember> members = tbMemberMapper.selectByExample(example);
        for (TbMember member:members){
            judgeUserBlock(member);
        }
        return 1;
    }

    /**
     * 判断注册用户是否超时未激活
     */
    public String judgeUserDel(TbMember tbMember){

        String result=null;
        long diff=System.currentTimeMillis()-tbMember.getDeleteDate().getTime();
        if (diff>=0){
            //超时删除用户
            if (tbMemberMapper.deleteByPrimaryKey(tbMember.getId())!=1){
                throw new HiShopException("删除注册超时用户失败");
            }else {
                //返回激活到期时间
                long time=tbMember.getCreated().getTime()+1000 * 60 * 60 * 24;
                result= String.valueOf(time);
            }
        }
        return result;
    }

    public String judgeUserBlock(TbMember tbMember){
        String result=null;
        long diff=System.currentTimeMillis()-tbMember.getUnblockDate().getTime();
        if (diff>=0){
            tbMember.setState(3);
            tbMember.setUpdated(new Date());
            tbMember.setBlockDate(null);
            tbMember.setUnblockDate(null);
            //超时删除用户
            if (tbMemberMapper.updateByPrimaryKey(tbMember)!=1){
                throw new HiShopException("修改封号用户失败");
            }else {
                //返回激活到期时间
                long time=tbMember.getCreated().getTime()+1000 * 60 * 60 * 24;
                result= String.valueOf(time);
            }
        }
        return result;
    }
}
