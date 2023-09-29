package com.softeem.dao;

import com.softeem.pojo.Member;

public interface MemberDao {
    Member findByTelephone(String telephone);

    public void add(Member member);

    //    public Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountBeforeDate(String month);

    public Integer findMemberCountByDate(String date);

    public Integer findMemberCountAfterDate(String date);

    public Integer findMemberTotalCount();

}
