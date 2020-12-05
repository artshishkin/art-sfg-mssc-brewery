package net.shyshkin.study.beerservice.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.services.BeerService;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
@Validated
public class BeerController {

    public static final String BASE_URL = "/api/v1";
    public static final String BEER_URL = BASE_URL + "/beer";

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(path = "beer", produces = {APPLICATION_JSON_VALUE})
    public BeerPagedList listBeers(
            @PositiveOrZero @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @Positive @RequestParam(value = "pageSize", required = false, defaultValue = "25") Integer pageSize,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
            @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {

        return beerService.listBeer(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
    }


    @GetMapping("beer/{beerId}")
    public BeerDto getBeerById(@PathVariable("beerId") UUID beerId,
                               @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {
        return beerService.getBeerById(beerId, showInventoryOnHand);
    }

    @PostMapping("beer")
    public ResponseEntity<BeerDto> createNewBeer(@Validated @RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.saveNewBeer(beerDto);
        UUID id = savedBeer.getId();
        return ResponseEntity.created(URI.create(BEER_URL).resolve(id.toString())).body(savedBeer);
    }

    @PutMapping("beer/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
        beerService.updateBeer(beerId, beerDto);
    }

    @GetMapping("beerUpc/{beerUpc}")
    public BeerDto getBeerByUpc(@PathVariable("beerUpc") String beerUpc,
                               @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {
        return beerService.getBeerByUpc(beerUpc, showInventoryOnHand);
    }
}
