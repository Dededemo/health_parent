package com.softeem.service;

import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.pojo.Address;

import java.util.List;

public interface AddressService {
    public List<Address> findAll();

    public PageResult findPage(QueryPageBean queryPageBean);

    public void add(Address address);

    public void edit(Address address);

    public void delete(Integer id);

    public Address findById(Integer id);
}
