package com.berrybeerservice.web.mappers;

import com.berrybeerservice.domain.Beer;
import com.berrybeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
    BeerDto beerToBeerDTO(Beer beer);
    Beer beerDtoToBeer(BeerDto beerDto);
}
