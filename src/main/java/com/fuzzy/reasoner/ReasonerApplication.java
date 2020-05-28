package com.fuzzy.reasoner;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ReasonerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReasonerApplication.class, args);
    }



}
