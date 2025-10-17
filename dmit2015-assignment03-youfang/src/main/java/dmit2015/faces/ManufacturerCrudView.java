package dmit2015.faces;

import dmit2015.model.Manufacturer;
import dmit2015.service.ManufacturerService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * This Jakarta Faces backing bean class contains the data and event handlers
 * to perform CRUD operations using a PrimeFaces DataTable configured to perform CRUD.
 */
@Named("currentManufacturerCrudView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
public class ManufacturerCrudView implements Serializable {

    @Inject
    @Named("jakartaPersistenceManufacturerService")
    private ManufacturerService manufacturerService;

    /**
     * The selected Manufacturer instance to create, edit, update or delete.
     */
    @Getter
    @Setter
    private Manufacturer selectedManufacturer;

    /**
     * The unique name of the selected Manufacturer instance.
     */
    @Getter
    @Setter
    private String selectedId;

    /**
     * The list of Manufacturer objects fetched from the data source
     */
    @Getter
    private List<Manufacturer> manufacturers;

    /**
     * Fetch all Manufacturer from the data source.
     * <p>
     * If FacesContext message sent from init() method annotated with @PostConstruct in the Faces backing bean class are not shown on page:
     * 1) Remove the @PostConstruct annotation from the Faces backing bean class
     * 2) Add metadata tag shown below to the page to execute the init() method
     * <f:metadata>
     * <f:viewParam name="dummy" />
     * <f:event type="postInvokeAction" listener="#{currentBeanView.init}" />
     * </f:metadata>
     */
    @PostConstruct
    public void init() {
        try {
            manufacturers = manufacturerService.getAllManufacturers();
        } catch (Exception e) {
            Messages.addGlobalError("Error getting manufacturers %s", e.getMessage());
        }
    }
}
