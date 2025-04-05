package com.shalom.shipping_status.service.impl;

import com.shalom.shipping_status.error.exception.BusinessException;
import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.service.SetEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

import static java.util.Objects.isNull;
import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class SetEmailServiceImpl implements SetEmailService {
    private final ShipStatusRepository repository;

    @Override
    public Mono<Map<String, String>> setEmail(SetEmailShipShalomRequest request) {
        return this.repository.findById(request.getNumber())
                .filter(itemSaved -> isNull(itemSaved.getEmail()) || itemSaved.getEmail().isEmpty())
                .switchIfEmpty(error(new BusinessException("Correo ya registrado.")))
                .doOnNext(itemSaved -> itemSaved.setEmail(request.getEmail()))
                .flatMap(this.repository::save)
                .map(response -> Map.of("message", "ok"));
    }
}
