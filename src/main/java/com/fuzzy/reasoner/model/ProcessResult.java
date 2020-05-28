package com.fuzzy.reasoner.model;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessResult {
    long timestamp ;

    double value;

    String ref;

    public long  getTimestamp() {
        return  timestamp%10000;
    }

    public void setTimestamp(long  x) {
        timestamp= x/10000 ;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
