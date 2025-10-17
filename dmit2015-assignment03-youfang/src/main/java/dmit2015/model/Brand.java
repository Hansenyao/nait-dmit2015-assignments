package dmit2015.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.datafaker.Faker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
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
public class Brand implements Serializable {

    private static final Logger logger = Logger.getLogger(Brand.class.getName());

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branid", nullable = false)
    private String id;

    @NotBlank(message = "Brand name is required.")
    private String name;

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // Bike brands
    public static final String[] BRANDS =
            {"Trek", "Giant", "Specialized", "Cannondale", "Scott"};

    // Return a random brand name from BRANDS
    public static String getRandomBrandName() {
        Random random = new Random();
        int randomIndex = random.nextInt(BRANDS.length);
        return  BRANDS[randomIndex];
    }

    // Constructor with name
    public Brand(String brand) {
        if (!Arrays.asList(BRANDS).contains(brand)) {
            throw new IllegalArgumentException("Invalid rand name: " + brand);
        }
        this.setId(UUID.randomUUID().toString());
        this.setName(brand);
    }

    @PrePersist
    private void beforePersist() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    private void beforeUpdate() {
        updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof Brand other) && Objects.equals(id, other.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}