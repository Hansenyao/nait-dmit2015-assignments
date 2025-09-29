package dmit2015.model;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.datafaker.Faker;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bike {
    private String id;

    @NotBlank(message = "Color is required.")
    private String Color;

    @NotBlank(message = "Brand is required.")
    private String Brand;

    @NotBlank(message = "Model is required.")
    private String Model;

    @NotBlank(message = "Size is required.")
    private String Size;    // Small, Medium, Big

    @NotBlank(message = "Manufacture City is required.")
    private String ManufactureCity;

    @NotBlank(message = "CreateDate is required.")
    private Date CreateDate;

    public Bike(Bike other) {
        this.id = other.id;
        this.Color = other.Color;
        this.Brand = other.Brand;
        this.Model = other.Model;
        this.Size = other.Size;
        this.ManufactureCity = other.ManufactureCity;
        this.CreateDate = other.CreateDate;
    }

    public static Bike copyOf(Bike other) {
        return new Bike(other);
    }

    public static Bike of(Faker faker) {
        var newBike = new Bike();
        String[] brands = {"Trek", "Giant", "Specialized", "Cannondale", "Scott"};

        newBike.setId(UUID.randomUUID().toString());
        newBike.setColor(faker.color().name());
        newBike.setBrand(faker.options().option(brands));
        newBike.setModel(faker.bothify("Model-??##"));
        newBike.setSize(faker.number().numberBetween(20, 29) + " inch");
        newBike.setManufactureCity(faker.address().city());
        return newBike;
    }
}
