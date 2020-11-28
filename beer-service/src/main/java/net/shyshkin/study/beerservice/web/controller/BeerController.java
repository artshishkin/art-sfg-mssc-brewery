package net.shyshkin.study.beerservice.web.controller;

import net.shyshkin.study.beerservice.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class BeerController {

    public static final String BASE_URL = "/api/v1/beer";

    @GetMapping("{beerId}")
    public BeerDto getBeerById(@PathVariable("beerId") UUID beerId) {
        return BeerDto.builder().build();
    }

    @PostMapping
    public ResponseEntity createNewBeer(@Validated @RequestBody BeerDto beerDto) {
        return ResponseEntity.created(URI.create(BASE_URL)).build();
    }

    @PutMapping("{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBeerById(@PathVariable("beerId") UUID beerId) {
        // TODO: 25.11.2020 Implement real method
    }


}
