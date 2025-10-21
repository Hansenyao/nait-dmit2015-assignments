package dmit2015.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import net.datafaker.Faker;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;

/**
 * This Jakarta Persistence class is mapped to a relational database table with the same name.
 * If Java class name does not match database table name, you can use @Table annotation to specify the table name.
 * <p>
 * Each field in this class is mapped to a column with the same name in the mapped database table.
 * If the field name does not match database table column name, you can use the @Column annotation to specify the database table column name.
 * The @Transient annotation can be used on field that is not mapped to a database table column.
 */
@Entity
//@Table(schema = "CustomSchemaName", name="CustomTableName")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bike implements Serializable {

    private static final Logger logger = Logger.getLogger(Bike.class.getName());

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bikeid", unique = true, nullable = false)
    private String id;

    @NotBlank(message = "Color is required.")
    private String color;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "Brand is required")
    private Brand brand;

    @NotBlank(message = "Model is required.")
    private String model;

    @NotBlank(message = "Size is required.")
    private String size;    // Small, Medium, Big

    @NotBlank(message = "Manufacture City is required.")
    private String manufactureCity;

    @NotNull(message = "Manufacture Date is required.")
    @Past(message = "Manufacture Date must be in the past.")
    private LocalDate manufactureDate;

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // Bike brands
    public static final String[] BRANDS =
            {"Trek", "Giant", "Specialized", "Cannondale", "Scott"};

    public Bike(Bike other) {
        this.version = other.version;
        this.id = other.id;
        this.color = other.color;
        this.brand = other.brand;
        this.model = other.model;
        this.size = other.size;
        this.manufactureCity = other.manufactureCity;
        this.manufactureDate = other.manufactureDate;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
    }

    public static Bike copyOf(Bike other) {
        return new Bike(other);
    }

    public static Bike of(Faker faker) {
        var newBike = new Bike();
        Instant instant = faker.timeAndDate().past(100, TimeUnit.DAYS);
        newBike.setId(UUID.randomUUID().toString());
        newBike.setColor(faker.color().name());
        newBike.setModel(faker.bothify("Model-??##"));
        newBike.setSize(faker.number().numberBetween(20, 29) + " inch");
        newBike.setManufactureCity(faker.address().city());
        newBike.setManufactureDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate().minusDays(1));
        return newBike;
    }

    @PrePersist
    private void beforePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    private void beforeUpdate() {
        updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof Bike other) && Objects.equals(id, other.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}