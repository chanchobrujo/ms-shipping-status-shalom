package com.yape.shipping_status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ShippingStatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingStatusApplication.class, args);
	}

}
