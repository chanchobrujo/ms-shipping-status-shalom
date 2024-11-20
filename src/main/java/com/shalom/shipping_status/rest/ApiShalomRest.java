package com.shalom.shipping_status.rest;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.api.APISearchShalomResponse;
import com.shalom.shipping_status.model.response.api.APIStateShalomResponse;
import com.shalom.shipping_status.properties.ApisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiShalomRest extends _WebClientRest implements IApiShalomRest {
    private final ApisProperties apisProperties;

    @Override
    public Mono<APISearchShalomResponse> search(ShipShalomRequest shipShalomRequest) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("numero", shipShalomRequest.getNumber());
        request.add("codigo", shipShalomRequest.getCode());
        var uri = this.apisProperties._search();
        return this.defaultGenericWebClient("POST", uri, request)
                .bodyToMono(APISearchShalomResponse.class);
    }

    @Override
    public Mono<APIStateShalomResponse> states(String oseId) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("ose_id", oseId);
        return this.defaultGenericWebClient("POST",this.apisProperties._state(), request)
                .bodyToMono(APIStateShalomResponse.class);
    }
}
