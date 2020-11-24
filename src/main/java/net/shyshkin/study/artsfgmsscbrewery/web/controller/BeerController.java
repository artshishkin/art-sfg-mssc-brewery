package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.BeerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
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

    @PostMapping
    public ResponseEntity<BeerDto> handlePost(@RequestBody BeerDto beerDto, HttpServletRequest request) {
        BeerDto saveNewBeer = beerService.saveNewBeer(beerDto);
        String url = request
                .getRequestURL()
                .append("/")
                .append(saveNewBeer.getId().toString())
                .toString();
        URI resourceUri = URI.create(url);
        return ResponseEntity.created(resourceUri).body(saveNewBeer);
    }
}
