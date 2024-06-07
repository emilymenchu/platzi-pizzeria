package com.platzi.pizza.web.controller;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.PizzaService;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;
    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int size){
        return ResponseEntity.ok(this.pizzaService.getAll(page, size));
    }
    @GetMapping("/available")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Page<PizzaEntity>> available(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "8") int size,
                                                       @RequestParam(defaultValue = "price") String sortBy,
                                                       @RequestParam(defaultValue = "ASC") String sortDirection){
        return ResponseEntity.ok(this.pizzaService.getAvailable(page, size, sortBy, sortDirection));
    }
    @GetMapping("/with/{ingridient}")
    public ResponseEntity<List<PizzaEntity>> getWith(@PathVariable String ingridient){
        return ResponseEntity.ok(this.pizzaService.getWith(ingridient));
    }
    @GetMapping("/without/{ingridient}")
    public ResponseEntity<List<PizzaEntity>> getWithout(@PathVariable String ingridient){
        return ResponseEntity.ok(this.pizzaService.getWithout(ingridient));
    }
    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getCheapestPizzas(@PathVariable double price){
        return ResponseEntity.ok(this.pizzaService.getCheapest(price));
    }
    @GetMapping("/vegan-or-vegetarian")
    public ResponseEntity<List<PizzaEntity>> getVeganOrVegetarian(){
        return ResponseEntity.ok(this.pizzaService.getVeganOrVegetarian());
    }
    @GetMapping("/available/{name}")
    public ResponseEntity<PizzaEntity> findById(@PathVariable String name){
        return ResponseEntity.ok(this.pizzaService.findFirstByAvailableAndName(name));
    }
    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> findById(@PathVariable Integer idPizza){
        return ResponseEntity.ok(this.pizzaService.findById(idPizza));
    }

    @PostMapping("/save")
    public ResponseEntity<PizzaEntity> save(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping("/update")
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto){
        if (this.pizzaService.exists(dto.getPizzaId())){
            this.pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable Integer idPizza){
        if (this.pizzaService.exists(idPizza)){
            this.pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
