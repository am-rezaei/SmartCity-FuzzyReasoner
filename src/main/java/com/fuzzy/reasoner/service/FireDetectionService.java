package com.fuzzy.reasoner.service;

import com.fuzzy.reasoner.component.ModelBuilder;
import com.fuzzy.reasoner.model.ElasticResult;
import com.fuzzy.reasoner.model.ProcessResult;
import com.fuzzy.reasoner.model.SensorData;
import fuzzydl.Concept;
import fuzzydl.Individual;
import fuzzydl.KnowledgeBase;
import fuzzydl.MaxInstanceQuery;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

public class FireDetectionService {

    public List<ProcessResult> processSensorData() {
        List<SensorData> rawData = new ArrayList<>();

        List<SensorData> queryData = getData("temperature", 8);
        if (queryData != null) rawData.addAll(queryData);
        queryData = getData("movement", 8);
        if (queryData != null) rawData.addAll(queryData);
        queryData = getData("smoke", 8);
        if (queryData != null) rawData.addAll(queryData);

        List<String> homes = new ArrayList<>();

        rawData.forEach(p -> {
            if (!homes.contains(p.home)) homes.add(p.home);
        });

        List<ProcessResult> results = new ArrayList<>();
        System.out.println("homes size " + homes.size());
        homes.forEach(home ->
        {
            int temperature = 0;
            int numberOfMovements = 0;
            int smokeLevel = 0;
            try {
                temperature = rawData.stream().filter(p -> p.home.equalsIgnoreCase(home)
                        && p.source.equalsIgnoreCase("temperature")).findFirst().get().val;
            } catch (Exception x) {
                x.printStackTrace();
            }
            try {
                numberOfMovements = rawData.stream().filter(p -> p.home.equalsIgnoreCase(home)
                        && p.source.equalsIgnoreCase("movement")).findFirst().get().val;
            } catch (Exception x) {
            }
            try {
                smokeLevel = rawData.stream().filter(p -> p.home.equalsIgnoreCase(home)
                        && p.source.equalsIgnoreCase("smoke")).findFirst().get().val;
            } catch (Exception x) {
            }
            try {
                ProcessResult res = getHomeFireProbability(temperature, numberOfMovements, smokeLevel);
                res.setRef(home);
                res.setTimestamp(System.currentTimeMillis());
                results.add(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return results;
    }


    private List<SensorData> getData(String type, int sec) {
        System.out.println("start searching for ");
        String query = "{'query': { 'range' : { 'timestamp': { 'gte': 'TIMESTAMP' } } }, '_source' : ['timestamp','home','source','val'] }".replace("'", "\"").replace("TIMESTAMP", String.valueOf(new Date().getTime() - (sec * 1000)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =
                new HttpEntity<>(query, headers);

        ElasticResult res = new RestTemplate().postForObject("http://192.168.1.4:9200/" + type + "/data/_search?filter_path=hits.hits._source", request, ElasticResult.class);

        if (res != null && res.hits != null && res.hits.hits != null) {
            List<SensorData> data = res.hits.hits.stream().map(p -> p._source).collect(Collectors.toList());

            Map<String, Optional<SensorData>> recentData = data.stream()
                    .collect(groupingBy(SensorData::getHome,
                            maxBy(comparingLong(SensorData::getTimestamp))));

            List<SensorData> mostRecentData = new ArrayList<>();

            recentData.keySet().stream().forEach(p -> mostRecentData.add(new SensorData(System.currentTimeMillis(), type, p, recentData.get(p).get().val)));
            return mostRecentData;
        } else {
            System.out.println("Return null - no query data");
            return null;

        }

    }

    private ProcessResult getHomeFireProbability(int temperature, int numberOfMovements, int smokeLevel) throws Exception {

        System.out.println("start fuzzy with temp " + temperature + " movement " + numberOfMovements + " smoke " + smokeLevel);
        ModelBuilder modelBuilder = new ModelBuilder(temperature, numberOfMovements, smokeLevel);

        KnowledgeBase kb = modelBuilder.getKb();
        Concept FireRisk = kb.getConcept("FireRisk");
        Individual home = kb.getIndividual("home");
        MaxInstanceQuery query = new MaxInstanceQuery(FireRisk, home);

        ProcessResult res = new ProcessResult();
        res.setValue(query.solve(kb).getSolution());

        System.out.println("ConsistentKB, result: " + res.getValue());

        return res;
    }

}
