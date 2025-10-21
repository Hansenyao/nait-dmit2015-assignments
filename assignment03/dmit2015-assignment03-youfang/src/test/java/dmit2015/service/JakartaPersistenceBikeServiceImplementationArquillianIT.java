package dmit2015.service;

import dmit2015.config.ApplicationConfig;
import dmit2015.model.Bike;
import dmit2015.model.Brand;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import net.datafaker.Faker;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ArquillianTest
public class JakartaPersistenceBikeServiceImplementationArquillianIT { // The class must be declared as public

    static Faker faker = new Faker();

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:3.0").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core:3.27.6").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("net.datafaker:datafaker:2.5.1").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.3.232").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:13.2.0.jre11").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:23.9.0.25.07").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.postgresql:postgresql:42.7.7").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.mysql:mysql-connector-j:9.2.0").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.mariadb.jdbc:mariadb-java-client:3.5.3").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial:6.6.28.Final").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson:3.0.4").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(Bike.class, BikeService.class, JakartaPersistenceBikeService.class)
                .addClasses(Brand.class, BrandService.class, JakartaPersistenceBrandService.class)
                // TODO Add any additional libraries, packages, classes or resource files required
//                .addAsLibraries(pomFile.resolve("jakarta.platform:jakarta.jakartaee-api:10.0.0").withTransitivity().asFile())
                // .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    @Named("jakartaPersistenceBikeService")
    private JakartaPersistenceBikeService bikeService;

    @Inject
    @Named("jakartaPersistenceBrandService")
    private JakartaPersistenceBrandService brandService;

    @Resource
    private UserTransaction beanManagedTransaction;

    @BeforeAll
    static void beforeAllTests() {
        // code to execute before all tests in the current test class
    }

    @AfterAll
    static void afterAllTests() {
        // code to execute after all tests in the current test class
    }

    @BeforeEach
    void beforeEachTestMethod() throws SystemException, NotSupportedException {
        // Start a new transaction
        beanManagedTransaction.begin();
    }

    @AfterEach
    void afterEachTestMethod() throws SystemException {
        // Rollback the transaction
        beanManagedTransaction.rollback();
    }

    @Order(1)
    @Test
    void givenNewEntity_whenAddEntity_thenEntityIsAdded() {
        // Arrange
        Bike newBike = Bike.of(faker);
        // Set brand
        List<Brand> allBrand = brandService.getAllBrands();
        int randomIndex = faker.number().numberBetween(0, allBrand.size());
        newBike.setBrand(allBrand.get(randomIndex));

        // Act
        bikeService.createBike(newBike);

        // Assert
        assertThat(newBike.getId())
                .isNotNull();
    }

    @Order(2)
    @Test
    void givenExistingId_whenFindById_thenReturnEntity() {
        // Arrange
        Bike newBike = Bike.of(faker);
        // Set brand
        List<Brand> allBrand = brandService.getAllBrands();
        int randomIndex = faker.number().numberBetween(0, allBrand.size());
        newBike.setBrand(allBrand.get(randomIndex));

        // Act
        newBike = bikeService.createBike(newBike);

        // Assert
        Optional<Bike> optionalBike = bikeService.getBikeById(newBike.getId());
        assertThat(optionalBike.isPresent())
                .isTrue();
        // Assert
        var existingBike = optionalBike.orElseThrow();
        assertThat(existingBike)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(newBike);
    }

    @Order(3)
    @Test
    void givenExistingEntity_whenUpdatedEntity_thenEntityIsUpdated() {
        // Arrange
        Bike newBike = Bike.of(faker);
        // Set brand
        List<Brand> allBrand = brandService.getAllBrands();
        int randomIndex = faker.number().numberBetween(0, allBrand.size());
        newBike.setBrand(allBrand.get(randomIndex));

        // Update
        Instant instant = faker.timeAndDate().past(100, TimeUnit.DAYS);
        newBike = bikeService.createBike(newBike);
        newBike.setColor(faker.color().name());
        newBike.setModel(faker.bothify("Model-??##"));
        newBike.setSize(faker.number().numberBetween(20, 29) + " inch");
        newBike.setManufactureCity(faker.address().city());
        newBike.setManufactureDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate().minusDays(1));

        // Act
        Bike updatedBike = bikeService.updateBike(newBike);

        // Assert
        Optional<Bike> optionalBike = bikeService.getBikeById(updatedBike.getId());
        assertThat(optionalBike.isPresent())
                .isTrue();
        var existingBike = optionalBike.orElseThrow();
        assertThat(existingBike)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(newBike);
    }

    @Order(4)
    @Test
    void givenExistingId_whenDeleteEntity_thenEntityIsDeleted() {
        List<Brand> allBrand = brandService.getAllBrands();
        int randomIndex = faker.number().numberBetween(0, allBrand.size());

        // Arrange
        Bike newBike = Bike.of(faker);
        newBike.setBrand(allBrand.get(randomIndex));
        newBike = bikeService.createBike(newBike);
        // Act
        bikeService.deleteBikeById(newBike.getId());
        // Assert
        Optional<Bike> optionalBike = bikeService.getBikeById(newBike.getId());
        assertThat(optionalBike.isPresent())
                .isFalse();
    }

    @Order(5)
    @ParameterizedTest
    @CsvSource({"10"})
    void givenMultipleEntity_whenFindAll_thenReturnEntityList(int expectedRecordCount) {
        // Arrange: Set up the initial state
        List<Brand> allBrand = brandService.getAllBrands();

        // Delete all existing data
        assertThat(bikeService).isNotNull();
        bikeService.deleteAllBikes();
        // Generate expectedRecordCount number of fake data
        Bike firstExpectedBike = null;
        Bike lastExpectedBike = null;
        for (int counter = 1; counter <= expectedRecordCount; counter++) {
            Bike currentBike = Bike.of(faker);
            int randomIndex = faker.number().numberBetween(0, allBrand.size());
            currentBike.setBrand(allBrand.get(randomIndex));

            if (counter == 1) {
                firstExpectedBike = currentBike;
            } else if (counter == expectedRecordCount) {
                lastExpectedBike = currentBike;
            }

            bikeService.createBike(currentBike);
        }

        // Act: Perform the action to be tested
        List<Bike> bikeList = bikeService.getAllBikes();

        // Assert: Verify the expected outcome
        assertThat(bikeList.size())
                .isEqualTo(expectedRecordCount);

        // Get the first entity and compare with expected results
        var firstActualBike = bikeList.getFirst();
        assertThat(firstActualBike)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(firstExpectedBike);
        // Get the last entity and compare with expected results
        var lastActualBike = bikeList.getLast();
        assertThat(lastActualBike)
                .usingRecursiveComparison()
                // .ignoringFields("field1", "field2")
                .isEqualTo(lastExpectedBike);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource(value = {
            "'red', 'Model-xy21', '21inch', '', '2025-01-01', 'Manufacture City is required'",
            "'black', 'Model-ab26', '26inch', 'Vancouver', '2027-01-01', 'Manufacture Date must be in the past'",
    }, nullValues = {"null"})
    void givenEntityWithValidationErrors_whenAddEntity_thenThrowException(
            String color,
            String model,
            String size,
            String manufactureCity,
            String manufactureDate,
            String expectedExceptionMessage
    ) {
        List<Brand> allBrand = brandService.getAllBrands();
        int randomIndex = faker.number().numberBetween(0, allBrand.size());

        // Arrange
        Bike newBike = new Bike();
        newBike.setBrand(allBrand.get(randomIndex));
        newBike.setColor(color);
        newBike.setModel(model);
        newBike.setSize(size);
        newBike.setManufactureCity(manufactureCity);
        newBike.setManufactureDate(LocalDate.parse(manufactureDate));

        try {
            // Act
            bikeService.createBike(newBike);
            fail("An bean validation constraint should have been thrown");
        } catch (Exception ex) {
            // Assert
            assertThat(ex)
                    .hasMessageContaining(expectedExceptionMessage);
        }
    }

    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {
            "Trek",
            "Giant",
    }, nullValues = {"null"})
    void givenBrand_findEntityByBrand_thenReturnEntityList(
            String brandName
    ) {
        List<Brand> allBrand = brandService.getAllBrands();
        Brand brand = allBrand.stream()
                .filter(b -> b.getName().equals(brandName))
                .findFirst()
                .orElse(null);

        // Create new bike with the given brand
        Bike newBike = Bike.of(faker);
        newBike.setBrand(brand);
        bikeService.createBike(newBike);

        // Act
        List<Bike> bikes = bikeService.findByBrandId(brand.getId());

        // Assert
        assertThat(bikes.size()).isGreaterThanOrEqualTo(1);
    }
}