package dmit2015.service;

import dmit2015.model.Bike;
import dmit2015.model.Manufacturer;
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

@Named("jakartaPersistenceManufacturerService")
@ApplicationScoped
public class JakartaPersistenceManufacturerService implements ManufacturerService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext(unitName="postgresql-jpa-pu") //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        manufacturer.setId(UUID.randomUUID().toString());
        entityManager.persist(manufacturer);
        entityManager.flush();  // Validate immediately
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> getManufacturerById(String id) {
        try {
            Manufacturer querySingleResult = entityManager.find(Manufacturer.class, id);
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
    public List<Manufacturer> getAllManufacturers() {
        return entityManager.createQuery("SELECT o FROM Manufacturer o ", Manufacturer.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Manufacturer updateManufacturer(Manufacturer manufacturer) {

        Optional<Manufacturer> optionalManufacturer = getManufacturerById(manufacturer.getId());
        if (optionalManufacturer.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", manufacturer.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingManufacturer = optionalManufacturer.orElseThrow();
            // Update only properties that is editable by the end user
            existingManufacturer.setName(manufacturer.getName());
            existingManufacturer.setCountry(manufacturer.getCountry());

            manufacturer = entityManager.merge(existingManufacturer);
        }
        return manufacturer;
    }

    @Override
    @Transactional
    public void deleteManufacturerById(String id) {
        Optional<Manufacturer> optionalManufacturer = getManufacturerById(id);
        if (optionalManufacturer.isPresent()) {
            Manufacturer manufacturer = optionalManufacturer.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(manufacturer);
        } else {
            throw new RuntimeException("Could not find Manufacturer with id: " + id);
        }
    }

    @Override
    @Transactional
    public List<Bike> findByManufacturerId(Long manufacturerId) {
        return entityManager.createQuery("SELECT b FROM Bike b WHERE b.manufacturer.id = :id", Bike.class)
                .setParameter("id", manufacturerId)
                .getResultList();
    }

    @Override
    @Transactional
    public Manufacturer findManufacturerById(Long id) {
        return entityManager.find(Manufacturer.class, id);
    }
}