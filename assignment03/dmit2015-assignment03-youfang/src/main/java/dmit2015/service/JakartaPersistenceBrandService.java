package dmit2015.service;

import dmit2015.model.Brand;
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

@Named("jakartaPersistenceBrandService")
@ApplicationScoped
public class JakartaPersistenceBrandService implements BrandService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext(unitName="postgresql-jpa-pu") //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Brand createBrand(Brand brand) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        brand.setId(UUID.randomUUID().toString());
        entityManager.persist(brand);
        entityManager.flush();  // Validate immediately
        return brand;
    }

    @Override
    public Optional<Brand> getBrandById(String id) {
        try {
            Brand querySingleResult = entityManager.find(Brand.class, id);
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
    public List<Brand> getAllBrands() {
        return entityManager.createQuery("SELECT o FROM Brand o ", Brand.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Brand updateBrand(Brand brand) {

        Optional<Brand> optionalBrand = getBrandById(brand.getId());
        if (optionalBrand.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", brand.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingBrand = optionalBrand.orElseThrow();
            // Update only properties that is editable by the end user
            existingBrand.setName(brand.getName());

            brand = entityManager.merge(existingBrand);
        }
        return brand;
    }

    @Override
    @Transactional
    public void deleteBrandById(String id) {
        Optional<Brand> optionalBrand = getBrandById(id);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(brand);
        } else {
            throw new RuntimeException("Could not find Brand with id: " + id);
        }
    }
}