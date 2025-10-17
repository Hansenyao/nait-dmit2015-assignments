package dmit2015.service;

import dmit2015.model.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {

    Manufacturer createManufacturer(Manufacturer manufacturer);

    Optional<Manufacturer> getManufacturerById(String id);

    List<Manufacturer> getAllManufacturers();

    Manufacturer updateManufacturer(Manufacturer manufacturer);

    void deleteManufacturerById(String id);
}