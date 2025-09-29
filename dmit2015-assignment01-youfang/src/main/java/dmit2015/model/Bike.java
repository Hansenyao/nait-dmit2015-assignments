package dmit2015.model;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.datafaker.Faker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bike {
    private String id;

    @NotBlank(message = "Color is required.")
    private String color;

    @NotBlank(message = "Brand is required.")
    private String brand;

    @NotBlank(message = "Model is required.")
    private String model;

    @NotBlank(message = "Size is required.")
    private String size;    // Small, Medium, Big

    @NotBlank(message = "Manufacture City is required.")
    private String manufactureCity;

    @NotBlank(message = "Manufacture Date is required.")
    private LocalDate manufactureDate;

    // Bike brands
    public static final String[] BRANDS =
            {"Trek", "Giant", "Specialized", "Cannondale", "Scott"};

    public Bike(Bike other) {
        this.id = other.id;
        this.color = other.color;
        this.brand = other.brand;
        this.model = other.model;
        this.size = other.size;
        this.manufactureCity = other.manufactureCity;
        this.manufactureDate = other.manufactureDate;
    }

    public static Bike copyOf(Bike other) {
        return new Bike(other);
    }

    public static Bike of(Faker faker) {
        var newBike = new Bike();
        Instant instant = faker.timeAndDate().past(100, TimeUnit.DAYS);
        newBike.setId(UUID.randomUUID().toString());
        newBike.setColor(faker.color().name());
        newBike.setBrand(faker.options().option(BRANDS));
        newBike.setModel(faker.bothify("Model-??##"));
        newBike.setSize(faker.number().numberBetween(20, 29) + " inch");
        newBike.setManufactureCity(faker.address().city());
        newBike.setManufactureDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
        return newBike;
    }
}
