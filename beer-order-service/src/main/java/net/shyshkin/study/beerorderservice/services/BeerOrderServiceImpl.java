/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.shyshkin.study.beerorderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.BeerOrderPagedList;
import net.shyshkin.study.beerorderservice.domain.BeerOrder;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import net.shyshkin.study.beerorderservice.web.mappers.BeerOrderMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final BeerOrderManager beerOrderManager;

    @Override
    public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Page<BeerOrder> beerOrderPage =
                    beerOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

            return new BeerOrderPagedList(beerOrderPage
                    .stream()
                    .map(beerOrderMapper::beerOrderToDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    beerOrderPage.getPageable().getPageNumber(),
                    beerOrderPage.getPageable().getPageSize()),
                    beerOrderPage.getTotalElements());
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer Not Found, id: `" + customerId + "`"));

        BeerOrder beerOrder = beerOrderMapper.dtoToBeerOrder(beerOrderDto);
        beerOrder.setId(null); //should not be set by outside client
        beerOrder.setCustomer(customer);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        log.debug("Saved Beer Order: " + beerOrder.getId());
        return beerOrderMapper.beerOrderToDto(savedBeerOrder);
    }

    @Override
    public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
        return beerOrderMapper.beerOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(UUID customerId, UUID orderId) {
        getOrder(customerId, orderId);
        beerOrderManager.beerOrderPickedUp(orderId);
    }

    private BeerOrder getOrder(UUID customerId, UUID orderId) {
        BeerOrder beerOrder = beerOrderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Beer Order Not Found"));

        // fall to exception if customer id's do not match - order not for customer
        if (!beerOrder.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Order does not belong to Customer");
        }
        return beerOrder;
    }

}
