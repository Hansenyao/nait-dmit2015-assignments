package dmit2015.service;

import dmit2015.model.Bike;
import dmit2015.model.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface BikeService {

    Bike createBike(Bike bike);

    Optional<Bike> getBikeById(String id);

    List<Bike> getAllBikes();

    Bike updateBike(Bike bike);

    void deleteBikeById(String id);

    List<Bike> findByBrand(String brand);

    void deleteAllBikes();

}