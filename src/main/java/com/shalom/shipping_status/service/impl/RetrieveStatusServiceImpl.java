package com.shalom.shipping_status.service.impl;

import com.shalom.shipping_status.error.exception.BusinessException;
import com.shalom.shipping_status.mapper.SearchShalomMapper;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.repository.ApiShalomRepository;
import com.shalom.shipping_status.repository.wrapper.SearchShalomWrapper;
import com.shalom.shipping_status.repository.wrapper.StateShalomWrapper;
import com.shalom.shipping_status.service.RetrieveStatusService;
import com.shalom.shipping_status.service.VerifyCurrentTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class RetrieveStatusServiceImpl implements RetrieveStatusService {
    private final VerifyCurrentTrackingService verifyCurrentTrackingService;
    private final SearchShalomMapper searchShalomMapper;
    private final ApiShalomRepository apiShalomRepository;

    @Override
    public Mono<SearchShalomResponse> retrieve(ShipShalomRequest request) {
        return this.apiShalomRepository
                .search(request)
                .filter(SearchShalomWrapper::getSuccess)
                .switchIfEmpty(error(new BusinessException("Elemento no encontrado.")))
                .map(this.searchShalomMapper::toResponse)
                .flatMap(this::setTrackingStates)
                .flatMap(response -> this.verifyCurrentTrackingService.verify(request, response));
    }

    private Mono<SearchShalomResponse> setTrackingStates(SearchShalomResponse response) {
        return this.apiShalomRepository
                .states(response.getOse_id().toString())
                .filter(StateShalomWrapper::getSuccess)
                .doOnNext(stateShalom -> stateShalom.setDataOfStates(response))
                .thenReturn(response);
    }
}
