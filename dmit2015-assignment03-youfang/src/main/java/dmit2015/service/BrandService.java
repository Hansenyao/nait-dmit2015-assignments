package dmit2015.service;

import dmit2015.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    Brand createBrand(Brand brand);

    Optional<Brand> getBrandById(String id);

    List<Brand> getAllBrands();

    Brand updateBrand(Brand brand);

    void deleteBrandById(String id);
}