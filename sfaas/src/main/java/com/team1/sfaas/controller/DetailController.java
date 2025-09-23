package com.team1.sfaas.controller;

import com.team1.sfaas.model.DetailModel;
import com.team1.sfaas.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class DetailController {

    private final DetailService detailService;

    @Autowired
    public DetailController(DetailService detailService){
        this.detailService = detailService;
    }

    @GetMapping("/detail")
    public Map<String, List<DetailModel>> getDetail(@RequestParam("machine_id") String machine_id){

        List<DetailModel> temp = detailService.getDetail(machine_id);
        Map<String, List<DetailModel>> groupedMap = temp.stream().collect(Collectors.groupingBy(DetailModel::getData_name));

        // 결과 출력
        groupedMap.forEach((machineName, records) -> {
            System.out.println("Machine: " + machineName);
            for (DetailModel record : records) {
                System.out.println("  Data Name: " + record.getData_name()
                        + ", Value: " + record.getValue()
                        + ", PLC Date: " + record.getPlc_create_dt());
            }
        });
        return groupedMap;
    }

    @GetMapping("/test")
    public String test() {
        return "hello spring";
    }

}