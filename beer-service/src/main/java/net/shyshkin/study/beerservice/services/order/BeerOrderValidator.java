package net.shyshkin.study.beerservice.services.order;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidator {

    private final BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrder) {

        boolean upcInvalid = beerOrder
                .getBeerOrderLines()
                .stream()
                .anyMatch(lineDto -> beerRepository.findByUpc(lineDto.getUpc()).isEmpty());
        return !upcInvalid;
    }

//    public Boolean validateOrder(BeerOrderDto beerOrder) {
//
//        boolean upcValid = beerOrder.getBeerOrderLines()
//                .stream()
//                .allMatch(lineDto -> Objects.equals(beerRepository.getOne(lineDto.getBeerId()).getUpc(), lineDto.getUpc()));
//        return upcValid;
//    }
}
