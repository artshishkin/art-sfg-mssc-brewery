package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.BeerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @GetMapping("{beerId}")
    public ResponseEntity<BeerDto> getBeer(@PathVariable("beerId") UUID beerId) {
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }
}
