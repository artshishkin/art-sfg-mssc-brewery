package net.shyshkin.study.artsfgmsscbrewery.services;

import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId);
}