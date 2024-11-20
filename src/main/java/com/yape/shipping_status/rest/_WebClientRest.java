package com.yape.shipping_status.rest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static org.springframework.web.reactive.function.client.WebClient.*;

@Service
@RequiredArgsConstructor
public abstract class _WebClientRest {
    @Setter
    private String bearerToken;
    private final WebClient webClient = builder()
            .build();

    private RequestBodySpec getMethod(String method, String URI) {
        method = method.toUpperCase();
        RequestBodyUriSpec _method = this.webClient.post();
        switch (method) {
            case "POST": {
                _method = this.webClient.post();
                break;
            }
            case "PUT": {
                _method = this.webClient.put();
                break;
            }
            case "PATCH": {
                _method = this.webClient.patch();
                break;
            }
        }
        return _method.uri(URI)
                .contentType(APPLICATION_JSON);
    }

    private RequestHeadersSpec<?> setToken(RequestHeadersSpec<?> _methodBody) {
        if (nonNull(this.bearerToken)) {
            _methodBody = _methodBody
                    .header(AUTHORIZATION, this.bearerToken);
        }
        return _methodBody;
    }

    public ResponseSpec defaultGenericWebClientGet(String URI) {
        RequestHeadersSpec<?> getMethod = this.webClient
                .get()
                .uri(URI);
        return this.setToken(getMethod)
                .accept(APPLICATION_JSON)
                .retrieve();
    }

    public ResponseSpec defaultGenericWebClient(String method, String URI, Object request) {
        RequestHeadersSpec<?> _methodBody = this.getMethod(method, URI)
                .bodyValue(request);
        return this.setToken(_methodBody)
                .accept(APPLICATION_JSON)
                .retrieve();
    }

    public ResponseSpec defaultGenericWebClient(String method, String URI, MultiValueMap<String, String> request) {
        RequestHeadersSpec<?> _methodBody = this.getMethod(method, URI)
                .body(fromFormData(request));
        return this.setToken(_methodBody)
                .accept(MULTIPART_FORM_DATA)
                .retrieve();
    }
}
