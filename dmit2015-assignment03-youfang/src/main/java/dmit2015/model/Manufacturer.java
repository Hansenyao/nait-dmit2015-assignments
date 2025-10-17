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
public class Manufacturer implements Serializable {

    private static final Logger logger = Logger.getLogger(Manufacturer.class.getName());

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturerid", nullable = false)
    private String id;

    @NotBlank(message = "Manufacturer name is required.")
    private String name;

    @NotBlank(message = "Manufacturer country is required.")
    private String Country;

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // Bike manufacturers
    public static final String[][] MANUFACTURERS = {
            {"Trek Bicycle Corporation", "US"},
            {"Giant Manufacturing Co., Ltd.", "TW"},
            {"Specialized Bicycle Components, Inc.", "US"},
            {"Cannondale Bicycle Corporation", "US"},
            {"Scott Sports SA", "CH"},
    };

    // Constructor
    public Manufacturer(String  name, String country) {
        this.setId(UUID.randomUUID().toString());
        this.setName(name);
        this.setCountry(country);
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
        return ((obj instanceof Manufacturer other) && Objects.equals(id, other.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}