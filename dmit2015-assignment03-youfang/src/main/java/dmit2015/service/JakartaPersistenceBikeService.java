package dmit2015.service;

import dmit2015.model.Bike;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Named("jakartaPersistenceBikeService")
@ApplicationScoped
public class JakartaPersistenceBikeService implements BikeService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext(unitName="postgresql-jpa-pu") //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Bike createBike(Bike bike) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        bike.setId(UUID.randomUUID().toString());
        entityManager.persist(bike);
        return bike;
    }

    @Override
    public Optional<Bike> getBikeById(String id) {
        try {
            Bike querySingleResult = entityManager.find(Bike.class, id);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // id value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        return entityManager.createQuery("SELECT o FROM Bike o ", Bike.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Bike updateBike(Bike bike) {

        Optional<Bike> optionalBike = getBikeById(bike.getId());
        if (optionalBike.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", bike.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingBike = optionalBike.orElseThrow();
            // Update only properties that is editable by the end user
            existingBike.setBrand(bike.getBrand());
            existingBike.setSize(bike.getSize());
            existingBike.setColor(bike.getColor());
            existingBike.setModel(bike.getModel());
            existingBike.setManufactureCity(bike.getManufactureCity());
            existingBike.setManufactureDate(bike.getManufactureDate());
            bike = entityManager.merge(existingBike);
        }
        return bike;
    }

    @Override
    @Transactional
    public void deleteBikeById(String id) {
        Optional<Bike> optionalBike = getBikeById(id);
        if (optionalBike.isPresent()) {
            Bike bike = optionalBike.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(bike);
        } else {
            throw new RuntimeException("Could not find Bike with id: " + id);
        }
    }

    @Override
    @Transactional
    // Find bikes by brand
    public List<Bike> findByBrand(String brand) {
        // Return all bikes if brand is null or empty
        brand = brand != null ? brand.trim() : "";
        String jpql = """
            SELECT b FROM Bike b
            WHERE (:brand IS NULL OR :brand = '' OR LOWER(b.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
            ORDER BY b.id
            """;
        return entityManager.createQuery(jpql, Bike.class)
                .setParameter("brand", brand)
                .getResultList();
    }
}