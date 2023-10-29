package com.bwagih.scheduler.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HttpService {



    public HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> requestHeaders = new HashMap<>();

        if (request != null) {
            requestHeaders = Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(h -> h, request::getHeader));
        }
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        for (String headerKey : requestHeaders.keySet()) {
            if (!headerKey.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                headers.add(headerKey, requestHeaders.get(headerKey));
            }
        }

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.setIfModifiedSince(ZonedDateTime.now());
        headers.setIfNoneMatch("*");

        return headers;
    }


    public HttpClient getHttpClient() {
        return HttpClient.create()
                .headers(httpHeaders -> getHeaders(null).forEach(httpHeaders::add))
//                .doOnResponseError((httpClientResponse, throwable) -> httpClientResponse.)
                .responseTimeout(Duration.ofMillis(5000));
    }


    public Consumer<List<ExchangeFilterFunction>> getFilters() {
        return exchangeFilterFunctions -> {
            exchangeFilterFunctions.add(logRequest());
            exchangeFilterFunctions.add(logResponse());
        };
    }


    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                sb.append("===========================request begin================================================");
                sb.append("URI         : { ").append(clientRequest.url()).append(" } \n");
                sb.append("Method      : { ").append(clientRequest.method()).append(" } \n");
                sb.append("Headers     : { ").append(clientRequest.headers()).append(" } \n");
                sb.append("Request body: { ").append(Arrays.toString(clientRequest.body().toString().getBytes(StandardCharsets.UTF_8))).append(" } \n");
                sb.append("==========================request end================================================");

                log.debug(sb.toString());
            }

            return Mono.just(clientRequest);
        });
    }


    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                sb.append("===========================request begin================================================");
                sb.append("Status code         : { ").append(clientResponse.statusCode()).append(" } \n");
                sb.append("Headers     : { ").append(clientResponse.headers()).append(" } \n");
                sb.append("Response body: { ").append(Arrays.toString(clientResponse.body((clientHttpResponse, context) -> context.messageReaders()).toString().getBytes(StandardCharsets.UTF_8))).append(" } \n");
                sb.append("==========================request end================================================");

                log.debug(sb.toString());
            }

            return Mono.just(clientResponse);
        });
    }


}
