package com.yape.shipping_status.properties;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@ToString
@Configuration
public class ApisProperties {
    @Value("${apis.shalom.value}")
    private String value;
    @Value("${apis.shalom.methods.search}")
    private String search;
    @Value("${apis.shalom.methods.states}")
    private String state;

    public String _search() {
        return this.getValue().concat(this.getSearch());
    }
    public String _state() {
        return this.getValue().concat(this.getState());
    }
}
