package com.shalom.shipping_status.repository.impl;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.properties.ApisProperties;
import com.shalom.shipping_status.repository.ApiShalomRepository;
import com.shalom.shipping_status.repository.wrapper.SearchShalomWrapper;
import com.shalom.shipping_status.repository.wrapper.StateShalomWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
@RequiredArgsConstructor
public class ApiShalomRepositoryImpl implements ApiShalomRepository {
    private final WebClient webClient;
    private final ApisProperties apisProperties;

    @Override
    public Mono<SearchShalomWrapper> search(ShipShalomRequest shipShalomRequest) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("codigo", shipShalomRequest.getCode());
        request.add("numero", shipShalomRequest.getNumber());

        return this.webClient.post()
                .uri(this.retrieveMethod("search"))
                .body(fromFormData(request))
                .retrieve()
                .bodyToMono(SearchShalomWrapper.class);
    }

    @Override
    public Mono<StateShalomWrapper> states(String oseId) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("ose_id", oseId);

        return this.webClient.post()
                .uri(this.retrieveMethod("states"))
                .body(fromFormData(request))
                .retrieve()
                .bodyToMono(StateShalomWrapper.class);
    }

    public String retrieveMethod(String key) {
        var apiShalom = this.apisProperties
                .getApisCredentials()
                .get("shalom");
        var uri = apiShalom.getValue();
        return uri.concat(apiShalom.getMethods().get(key));
    }
}
