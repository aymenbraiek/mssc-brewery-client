package com.biat.msscbreweryclient.config;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {
    private  final Integer maxTotalConnections;
    private final  Integer defaultMaxTotalConnections;
    private final Integer connectionsRequestTimeout;
    private final Integer socketTimeout;

    public BlockingRestTemplateCustomizer(@org.springframework.beans.factory.annotation.Value("${sfg.maxtotalconnections}") Integer maxTotalConnections,
                                          @org.springframework.beans.factory.annotation.Value("${sfg.defaultmaxtotalconnections}") Integer defaultMaxTotalConnections,
                                          @org.springframework.beans.factory.annotation.Value("${sfg.connectionsrequesttimeout}") Integer connectionsRequestTimeout,
                                          @Value("${sfg.sockettimeout}") Integer socketTimeout) {
        this.maxTotalConnections = maxTotalConnections;
        this.defaultMaxTotalConnections = defaultMaxTotalConnections;
        this.connectionsRequestTimeout = connectionsRequestTimeout;
        this.socketTimeout = socketTimeout;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory()
    {
        PoolingHttpClientConnectionManager connectionManager= new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConnections);
        RequestConfig requestConfig=RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionsRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        CloseableHttpClient httpClient= HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return  new HttpComponentsClientHttpRequestFactory(httpClient);

    }


    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());

    }
}
