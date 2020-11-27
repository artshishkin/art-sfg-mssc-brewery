package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.BeerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.BeerController.BASE_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Deprecated
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class BeerController {

    public static final String BASE_URL = "/api/v1/beer";

    private final BeerService beerService;

    @GetMapping("{beerId}")
    public ResponseEntity<BeerDto> getBeer(@PathVariable("beerId") UUID beerId) {
        return new ResponseEntity<>(beerService.getBeerById(beerId), OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> handlePost(@Valid @RequestBody BeerDto beerDto, HttpServletRequest request) {
        BeerDto saveNewBeer = beerService.saveNewBeer(beerDto);
        String url = request
                .getRequestURL()
                .append("/")
                .append(saveNewBeer.getId().toString())
                .toString();
        URI resourceUri = URI.create(url);
        return ResponseEntity.created(resourceUri).body(saveNewBeer);
    }

    @PutMapping("{beerId}")
    @ResponseStatus(NO_CONTENT)
    public void updateBeer(@PathVariable UUID beerId, @Valid @RequestBody BeerDto beerDto) {
        beerService.updateBeer(beerId, beerDto);
    }

    @DeleteMapping("{beerId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId) {
        beerService.deleteById(beerId);
    }
}
