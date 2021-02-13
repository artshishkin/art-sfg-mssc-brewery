package net.shyshkin.study.beerinventoryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.BeerOrderLineDto;
import net.shyshkin.study.beerinventoryservice.domain.BeerInventory;
import net.shyshkin.study.beerinventoryservice.repositories.BeerInventoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNullElse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderImpl implements AllocateOrder {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        log.debug("Allocation Order Id: {}, order: {}", beerOrderDto.getId(), beerOrderDto);

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDto
                .getBeerOrderLines()
                .forEach(beerOrderLine -> {
                    Integer orderedQuantity = requireNonNullElse(beerOrderLine.getOrderQuantity(), 0);
                    Integer allocatedQuantity = requireNonNullElse(beerOrderLine.getQuantityAllocated(), 0);
                    if (orderedQuantity > allocatedQuantity)
                        allocateBeerOrderLine(beerOrderLine);

                    totalOrdered.addAndGet(orderedQuantity);
                    totalAllocated.addAndGet(beerOrderLine.getQuantityAllocated());
                });
        log.debug("Total Ordered: {}. Total allocated: {}", totalOrdered.get(), totalAllocated.get());
        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BeerOrderDto beerOrderDto) {
        log.debug("Deallocation Order Id: {}", beerOrderDto.getId());
        beerOrderDto
                .getBeerOrderLines()
                .forEach(this::deallocateBeerOrderLine);
    }

    private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {

        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc());
        beerInventoryList
                .forEach(beerInventory -> {
                    int inventory = requireNonNullElse(beerInventory.getQuantityOnHand(), 0);
                    int orderQty = requireNonNullElse(beerOrderLine.getOrderQuantity(), 0);
                    int allocatedQty = requireNonNullElse(beerOrderLine.getQuantityAllocated(), 0);
                    int qtyToAllocate = orderQty - allocatedQty;

                    if (qtyToAllocate > 0) {
                        if (inventory >= qtyToAllocate) { //full allocation
                            inventory -= qtyToAllocate;
                            beerOrderLine.setQuantityAllocated(allocatedQty + qtyToAllocate);
                            beerInventory.setQuantityOnHand(inventory);

                            beerInventoryRepository.save(beerInventory);

                        } else if (inventory > 0) { //partial allocation
                            beerOrderLine.setQuantityAllocated(allocatedQty + inventory);
                            beerInventory.setQuantityOnHand(0);
                        }
                    }
                    if (beerInventory.getQuantityOnHand() == 0) {
                        beerInventoryRepository.delete(beerInventory);
                    }
                });

    }

    private void deallocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {

        BeerInventory beerInventory = BeerInventory.builder()
                .upc(beerOrderLine.getUpc())
                .beerId(beerOrderLine.getBeerId())
                .quantityOnHand(beerOrderLine.getQuantityAllocated())
                .build();
        BeerInventory savedInventory = beerInventoryRepository.save(beerInventory);

        log.debug("Saved Inventory for Beer Upc: {}, Inventory Id: {}", savedInventory.getUpc(), savedInventory.getId());
    }
}
