package com.berrybeerservice.services.brewing;

import com.berrybeerservice.domain.Beer;
import com.berrybeerservice.repositories.BeerRepository;
import com.berrybeerservice.services.inventory.BeerInventoryService;
import com.berrybeerservice.web.mappers.BeerMapper;
import com.brewery.model.events.BrewBeerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.berrybeerservice.config.JmsConfig.BREWING_REQUEST_QUEUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory(){
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer quantityOnHand = beerInventoryService.getOnhandInventory(beer.getId());

            log.debug("Min Quantity On Hand Is: " + beer.getMinOnHand());
            log.debug("Inventory is: " + quantityOnHand);

            if (beer.getMinOnHand() >= quantityOnHand){
                jmsTemplate.convertAndSend(BREWING_REQUEST_QUEUE,
                        new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
