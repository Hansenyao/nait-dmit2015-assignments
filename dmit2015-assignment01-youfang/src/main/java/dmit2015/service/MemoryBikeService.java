package dmit2015.service;

import dmit2015.model.Bike;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import net.datafaker.Faker;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.random.RandomGenerator;

@Named("memoryBikeService")
@ApplicationScoped
public class MemoryBikeService implements BikeService {

    private final List<Bike> bikes = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        String[] brands = {"Trek", "Giant", "Specialized", "Cannondale", "Scott"};

        var faker = new Faker();
        var randomGenerator = RandomGenerator.getDefault();
        for (int counter = 1; counter <= 5; counter++) {
            var currentBike = new Bike();
            currentBike.setId(UUID.randomUUID().toString());
            currentBike.setColor(faker.color().name());
            currentBike.setBrand(faker.options().option(brands));
            currentBike.setModel(faker.bothify("Model-??##"));
            currentBike.setSize(faker.number().numberBetween(20, 29) + " inch");
            currentBike.setManufactureCity(faker.address().city());
            bikes.add(currentBike);
        }
    }

    @Override
    public Bike createBike(Bike bike) {
        Objects.requireNonNull(bike, "Bike to create must not be null");

        // Assign a fresh id on create to ensure uniqueness (ignore any incoming id)
        Bike stored = Bike.copyOf(bike);
        stored.setId(UUID.randomUUID().toString());
        bikes.add(stored);

        // Return a defensive copy
        return Bike.copyOf(stored);
    }

    @Override
    public Optional<Bike> getBikeById(String id) {
        Objects.requireNonNull(id, "id must not be null");

        return bikes.stream()
                .filter(currentBike -> currentBike.getId().equals(id))
                .findFirst()
                .map(Bike::copyOf); // return a copy to avoid external mutation
    }

    @Override
    public List<Bike> getAllBikes() {
        // Unmodifiable snapshot of copies
        return bikes.stream().map(Bike::copyOf).toList();
    }

    @Override
    public Bike updateBike(Bike bike) {
        Objects.requireNonNull(bike, "Bike to update must not be null");
        Objects.requireNonNull(bike.getId(), "Bike id must not be null");

        // Find index of existing task by id
        int index = -1;
        for (int i = 0; i < bikes.size(); i++) {
            if (bikes.get(i).getId().equals(bike.getId())) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new NoSuchElementException("Could not find Task with id: " + bike.getId());
        }

        // Replace stored item with a copy (preserve id)
        Bike stored = Bike.copyOf(bike);
        bikes.set(index, stored);

        return Bike.copyOf(stored);
    }

    @Override
    public void deleteBikeById(String id) {
        Objects.requireNonNull(id, "id must not be null");

        boolean removed = bikes.removeIf(currentBike -> id.equals(currentBike.getId()));
        if (!removed) {
            throw new NoSuchElementException("Could not find Task with id: " + id);
        }
    }
}
