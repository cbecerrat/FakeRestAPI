package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping("heroes")
public class HelloWorldController {

    private final List<String> INITIAL_HEROES = List.of("Saitama", "Genos", "Fubuki", "Tatsumaki", "King", "Biker without licence", "Metal bat", "Tank-top master", "Pretty pretty prisoner", "Lighting Max");
    private final Map<Integer, Hero> HEROES = new TreeMap<>();
    private int currentId = 0;

    public HelloWorldController() {
        AtomicInteger id = new AtomicInteger(1000);
        INITIAL_HEROES.forEach((var hero) -> {
            var current = id.incrementAndGet();
            HEROES.put(current, new Hero(current, hero));
        });

        currentId = id.incrementAndGet();
    }

    @GetMapping
    public List<Hero> getHeroes() {
        return new ArrayList<>(HEROES.values());
    }
    
    @GetMapping(path = "/{id}")
    public Hero getHero(@PathVariable int id) {
        Hero hero = HEROES.get(id);

        if (hero == null) {
            String message = String.format("Hero with id [%d] not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } else {
            return hero;
        }
    }

    @PostMapping
    public Hero createHero(@RequestBody Hero hero) {
        int id = currentId++;
        hero.setId(id);
        HEROES.put(id, hero);
        return hero;
    }

    @PutMapping(path = "/{id}")
    public Hero updateHero(@PathVariable int id, @RequestBody Hero hero) {
        if (HEROES.get(id) == null) {
            String message = String.format("Hero with id [%d] not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } else {
            hero.setId(id);
            HEROES.put(id, hero);
            return hero;
        }
    }
    
    @DeleteMapping(path = "/{id}")
    public Hero deleteHero(@PathVariable int id) {
        if (HEROES.get(id) == null) {
            String message = String.format("Hero with id [%d] not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } else {
            return HEROES.remove(id);
        }
    }
}
