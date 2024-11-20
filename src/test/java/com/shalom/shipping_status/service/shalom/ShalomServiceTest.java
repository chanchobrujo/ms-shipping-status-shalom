package com.shalom.shipping_status.service.shalom;

import com.shalom.shipping_status.common.utils.MapperUtils;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.rest.ApiShalomRest;
import com.shalom.shipping_status.service.ship_status.ShipStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(MockitoJUnitRunner.class)
class ShalomServiceTest {
    @Mock
    private ApiShalomRest apiShalomRest;
    @Mock
    private ShipStatusService shipStatusService;

    @InjectMocks
    private ShalomService service;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test
    void getPackage() {
        var verifier = MapperUtils.objectToObject(new ShipShalomRequest(), ShipShalomRequest.class);
        Assertions.assertNotNull(verifier);
    }
}
