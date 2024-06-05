package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.persistence.repository.PizzaPagSortRepository;
import com.platzi.pizza.persistence.repository.PizzaRepository;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import com.platzi.pizza.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    public Page<PizzaEntity> getAll(int page, int size){
        Pageable pageRequest = PageRequest.of(page, size);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }
    public Page<PizzaEntity> getAvailable(int page, int size, String sortBy, String sortDirection){
        System.out.println(this.pizzaRepository.countByVeganTrue());
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }
    public List<PizzaEntity> getWith(String ingridient){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingridient);
    }
    public List<PizzaEntity> getWithout(String ingridient){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingridient);
    }
    public List<PizzaEntity> getCheapest(double price){
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }
    public List<PizzaEntity> getVeganOrVegetarian(){
        return  this.pizzaRepository.findTop3ByAvailableTrueAndVeganTrueOrVegetarianTrueOrderByPriceAsc();
    }
    public PizzaEntity findFirstByAvailableAndName(String name){
        return  this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("La pizza no existe"));
    }

    public PizzaEntity findById(Integer idPizza){
        return  this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity save(PizzaEntity pizza){
        return this.pizzaRepository.save(pizza);
    }

    @Transactional(noRollbackFor = EmailApiException.class, propagation = Propagation.REQUIRED)
    public void updatePrice(UpdatePizzaPriceDto dto){
        this.pizzaRepository.updatePrice(dto);
        this.sendEmail();
    }
    public void sendEmail(){
        throw new EmailApiException();
    }

    public boolean exists(Integer idPizza){
        return this.pizzaRepository.existsById(idPizza);
    }

    public void delete(Integer idPizza){
        this.pizzaRepository.deleteById(idPizza);
    }


}
