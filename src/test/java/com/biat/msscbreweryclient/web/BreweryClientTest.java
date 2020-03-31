package com.biat.msscbreweryclient.web;

import com.biat.msscbreweryclient.Model.BeerDto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BreweryClientTest {
    @Autowired
    BreweryClient breweryClient;

    @Test
    void getBeerById() {
        BeerDto dtp = breweryClient.getBeerById(UUID.randomUUID());
        assertNotNull(dtp);

    }

    @Test
    void saveNewBeer() {
        BeerDto beerDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("wesky")
                .beerStyle("api")
                .build();
        System.out.println(beerDto.toString());
        URI location = breweryClient.saveNewBeer(beerDto);
      assertNotNull(location);
    }
}