package com.softeem.dao;

import com.github.pagehelper.Page;
import com.softeem.pojo.Address;

import java.util.List;

public interface AddressDao {
    public List<Address> findAll();

    public Page<Address> findPage(String queryString);

    public void add(Address address);

    public void edit(Address address);

    public void delete(Integer id);

    public Address findById(Integer id);


}
