package net.shyshkin.study.beerservice.repositories;

import net.shyshkin.study.beerservice.domain.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Optional<Beer> findByUpc(String upc);
}
