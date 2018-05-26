package com.utn.tp5.controllers;
import com.utn.tp5.models.Country;
import java.util.List;

import java.util.concurrent.atomic.AtomicLong;

import com.utn.tp5.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    CountryService countryService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Country> index() {
        return countryService.getAll();
    }
}
