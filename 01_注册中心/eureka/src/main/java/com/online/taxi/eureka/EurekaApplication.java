package com.online.taxi.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.netflix.eureka.registry.InstanceRegistry;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.registry.PeerAwareInstanceRegistryImpl;
/**
 * http://localhost:7900/  http://eureka-7900:7900/
 *
 * @author yueyi2019
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}
