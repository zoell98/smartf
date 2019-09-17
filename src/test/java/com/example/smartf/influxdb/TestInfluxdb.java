package com.example.smartf.influxdb;


import com.example.smartf.model.HistoryData;
import com.example.smartf.tool.influxdb.InfluxdbDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInfluxdb {

    @Resource
    private InfluxdbDao influxdbDao;

    @Test
    public void test() throws Exception{
        Map<String,String> maptag = new HashMap<>();
        maptag.put("days","2");
        Map<String,Object> mapfields = new HashMap<>();
        mapfields.put("CO2",50.2);
        mapfields.put("EC",33.6);
        mapfields.put("LED",0.0);
        mapfields.put("PH",6.0);
        mapfields.put("RH",18.9);
        mapfields.put("TEMP",31.2);
        influxdbDao.insert("6af6188e14aa",maptag,mapfields);
        List<HistoryData> list =  influxdbDao.findByTime("2019-09-16T00:00:00Z","2019-09-18T00:00:00Z","6af6188e14aa");
        System.out.println("============================");
        for(int i = 0;i < list.size();i++){
            System.out.println("time:" + list.get(i).getTime() +
                    " CO2:" + list.get(i).getCO2() +
                    " EC:" + list.get(i).getEC() +
                    " LED:" + list.get(i).getLED() +
                    " PH:" + list.get(i).getPH() +
                    " RH:" + list.get(i).getRH() +
                    " TEMP:" + list.get(i).getTEMP() +
                    " days:" + list.get(i).getDays());
        }
    }
}
