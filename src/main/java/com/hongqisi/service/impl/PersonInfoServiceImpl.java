package com.hongqisi.service.impl;

import com.hongqisi.entity.PersonInfo;
import com.hongqisi.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoService personInfoService;

    @Override
    public PersonInfo getPersonInfoById(long userId) {
        return personInfoService.getPersonInfoById(userId);
    }
}
