package com.tranbaolong2ws.config;

import com.tranbaolong2ws.entitys.Book;
import com.tranbaolong2ws.entitys.Message;
import com.tranbaolong2ws.entitys.Payment;
import com.tranbaolong2ws.entitys.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    private String theAllowedOrigins = "http://localhost:3000";


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry corsRegistry) {
        HttpMethod[] theUnsupporttedMethods = {HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH};

        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        config.exposeIdsFor(Message.class);
        config.exposeIdsFor(Payment.class);

        disableHttpMethods(Book.class, config, theUnsupporttedMethods);
        disableHttpMethods(Review.class, config, theUnsupporttedMethods);
        disableHttpMethods(Message.class, config, theUnsupporttedMethods);
        disableHttpMethods(Payment.class, config, theUnsupporttedMethods);


        corsRegistry.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }


    private void disableHttpMethods(Class theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupporttedMethods) {

        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupporttedMethods))
                .withCollectionExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupporttedMethods));
    }
}
