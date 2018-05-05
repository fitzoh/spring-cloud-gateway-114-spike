package com.github.fitzoh.springcloudgateway114spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCloudGateway114SpikeApplication {

    private final ForkingGatewayFilterFactory forkingGatewayFilterFactory;
    private final JoiningGatewayFilterFactory joiningGatewayFilterFactory;

    public SpringCloudGateway114SpikeApplication(ForkingGatewayFilterFactory forkingGatewayFilterFactory, JoiningGatewayFilterFactory joiningGatewayFilterFactory) {
        this.forkingGatewayFilterFactory = forkingGatewayFilterFactory;
        this.joiningGatewayFilterFactory = joiningGatewayFilterFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGateway114SpikeApplication.class, args);
    }


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r ->
                        r.path("/forkjoin")
                                .filters(f -> {
                                    f.filter(forkingGatewayFilterFactory.apply(c -> c.setName("fork1").setUri("http://httpbin.org/anything/fork1")));
                                    f.filter(forkingGatewayFilterFactory.apply(c -> c.setName("fork2").setUri("http://httpbin.org/anything/fork2")));
                                    f.filter(joiningGatewayFilterFactory.apply(c -> c.setName("join")));
                                    return f;
                                })
                                .uri("http://google.com"))
                .build();


    }
}
