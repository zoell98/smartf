package com.example.smartf.tool.influxdb.Impl;

import com.example.smartf.config.InfluxdbConfig;
import com.example.smartf.model.HistoryData;
import com.example.smartf.tool.TimeformatTrans;
import com.example.smartf.tool.influxdb.InfluxdbDao;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InfluxdbDaoImpl implements InfluxdbDao {
    @Resource
    private InfluxDB influxDB;
    @Resource
    private InfluxdbConfig influxdbConfig;


    //插入一条信息
    public void insert(String measurement, Map<String,String> tags, Map<String,Object> fields){
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(influxdbConfig.getDbName(),null,builder.build());
    }

    //按起始时间和终止时间 查找 数据库 (返回List<HistoryData>)
    public ArrayList findByTime(String starttime, String endtime, String measurement){
        ArrayList<HistoryData> res = new ArrayList<>();

        //构造数据库query查询语句
        Query query = new Query("select * from \"" + measurement + "\" where" + " time>=" + "'" + starttime + "'" +
                " and " + "time<=" + "'" + endtime + "'"  , influxdbConfig.getDbName());

        QueryResult queryResult = influxDB.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0);

        if(oneResult.getSeries() != null){
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);
            if( null != valueList && valueList.size() > 0){
                for(List<Object> value : valueList ){
                    //取字段的值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String CO2 = (value.get(1) == null) ? null : value.get(1).toString();
                    String EC = (value.get(2) == null) ? null : value.get(2).toString();
                    String LED = (value.get(3) == null) ? null : value.get(3).toString();
                    String PH = (value.get(4) == null) ? null : value.get(4).toString();
                    String RH = (value.get(5) == null) ? null : value.get(5).toString();
                    String TEMP = (value.get(6) == null) ? null : value.get(6).toString();
                    String days = (value.get(7) == null) ? null : value.get(7).toString();

                    HistoryData historyData = new HistoryData();
                    //转换influxdb取出来的时间变为 如 "2018-01-05 11:03:05"
                    historyData.setTime(TimeformatTrans.dbtimeTonNormal(time));
                    historyData.setCO2(CO2);
                    historyData.setEC(EC);
                    historyData.setLED(LED);
                    historyData.setPH(PH);
                    historyData.setRH(RH);
                    historyData.setTEMP(TEMP);
                    historyData.setDays(days);

                    res.add(historyData);
                }
            }

        }
        return res;
    }

    //删除表
    public String deleteMeasurement(String measurement){
        QueryResult result = influxDB.query(new Query("drop measurement " + measurement,influxdbConfig.getDbName()));
        return result.getError();
    }

}
