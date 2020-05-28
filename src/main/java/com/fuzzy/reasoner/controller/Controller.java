package com.fuzzy.reasoner.controller;

import com.fuzzy.reasoner.model.ProcessResult;
import com.fuzzy.reasoner.model.SensorData;
import com.fuzzy.reasoner.service.FireDetectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class Controller {

    FireDetectionService service = new FireDetectionService();

    List<ProcessResult> lastResult;


    @GetMapping("/result")
    public List<ProcessResult> getResult() {
        System.out.println("GET => /result");
        List<ProcessResult> result;
//        try {
//            result = service.processSensorData();
//            lastResult = result;
//        } catch (Exception x) {
//            result = lastResult;
//        }
//        return result;
        return service.processSensorData();
    }

}
