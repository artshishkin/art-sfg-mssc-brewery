package net.shyshkin.study.artsfgmsscbrewery.web.controller.v2;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.v2.BeerServiceV2;
import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerDtoV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.v2.BeerControllerV2.BASE_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class BeerControllerV2 {

    public static final String BASE_URL = "/api/v2/beer";

    private final BeerServiceV2 beerService;

    @GetMapping("{beerId}")
    public ResponseEntity<BeerDtoV2> getBeer(@PathVariable("beerId") UUID beerId) {
        return new ResponseEntity<>(beerService.getBeerById(beerId), OK);
    }

    @PostMapping
    public ResponseEntity<BeerDtoV2> handlePost(@Valid @RequestBody BeerDtoV2 beerDto, HttpServletRequest request) {
        BeerDtoV2 saveNewBeer = beerService.saveNewBeer(beerDto);
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
    public void updateBeer(@PathVariable UUID beerId, @Valid @RequestBody BeerDtoV2 beerDto) {
        beerService.updateBeer(beerId, beerDto);
    }

    @DeleteMapping("{beerId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId) {
        beerService.deleteById(beerId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleValidationException(MethodArgumentNotValidException exception) {
        return exception
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
