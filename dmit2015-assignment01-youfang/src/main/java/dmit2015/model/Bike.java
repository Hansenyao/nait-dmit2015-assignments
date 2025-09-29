package dmit2015.model;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.datafaker.Faker;

import java.util.Date;
import java.util.UUID;

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

    // Bike brands
    public static final String[] BRANDS =
            {"rek", "Giant", "Specialized", "Cannondale", "Scott"};

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
        newBike.setId(UUID.randomUUID().toString());
        newBike.setColor(faker.color().name());
        newBike.setBrand(faker.options().option(BRANDS));
        newBike.setModel(faker.bothify("Model-??##"));
        newBike.setSize(faker.number().numberBetween(20, 29) + " inch");
        newBike.setManufactureCity(faker.address().city());
        return newBike;
    }
}
