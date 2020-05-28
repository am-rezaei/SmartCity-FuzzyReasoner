package com.fuzzy.reasoner.model;

import java.util.List;

public class ElasticResult {

    public Result hits;

    public static class Result {
        public List<Data> hits;
    }

    public static class Data {
        public SensorData _source;
    }

}
