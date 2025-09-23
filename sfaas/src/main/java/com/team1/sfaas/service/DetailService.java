package com.team1.sfaas.service;

import com.team1.sfaas.mapper.DetailMapper;
import com.team1.sfaas.model.DetailModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailService {

    private final DetailMapper detailMapper;

    @Autowired
    public DetailService(DetailMapper detailMapper){
        this.detailMapper = detailMapper;
    }

    public List<DetailModel> getDetail(String machine_id) {
        List<DetailModel> detailData;
        DetailModel machineData;

        detailData = detailMapper.getPLCData(machine_id);


        return detailData;
    }
}