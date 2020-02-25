package com.hongqisi.service;

import com.hongqisi.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

    public static final String HEADLINELIST="headlinelist";

    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
