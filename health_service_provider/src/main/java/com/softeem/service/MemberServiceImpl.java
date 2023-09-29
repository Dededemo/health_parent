package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.softeem.dao.MemberDao;
import com.softeem.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> mylist = new ArrayList<>();
        for (String m : list) {
            Integer count = memberDao.findMemberCountBeforeDate(m);
            mylist.add(count);
        }
        return mylist;
    }
}
