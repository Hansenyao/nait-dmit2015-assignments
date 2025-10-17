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

    /**
     * Event handler for the New button on the Faces crud page.
     * Create a new selected Manufacturer instance to enter data for.
     */
    public void onOpenNew() {
        selectedManufacturer = new Manufacturer();
        selectedId = null;
    }


    /**
     * Event handler to generate fake data using DataFaker.
     *
     * @link <a href="https://www.datafaker.net/documentation/getting-started/">Getting started with DataFaker</a>
     */
    public void onGenerateData() {
        try {
            var faker = new Faker();
            int randomIndex = faker.number().numberBetween(0, Manufacturer.MANUFACTURERS.length);
            selectedManufacturer = new Manufacturer();
            selectedManufacturer.setName(Manufacturer.MANUFACTURERS[randomIndex][0]);
            selectedManufacturer.setCountry(Manufacturer.MANUFACTURERS[randomIndex][1]);
            selectedManufacturer.setId(selectedId);
        } catch (Exception e) {
            Messages.addGlobalError("Error generating data {0}", e.getMessage());
        }
    }

    /**
     * Event handler for Save button to create or update data.
     */
    public void onSave() {
        try {

            // If selectedId is null then create new data otherwise update current data
            if (selectedId == null) {
                Manufacturer createdManufacturer = manufacturerService.createManufacturer(selectedManufacturer);

                // Send a Faces info message that create was successful
                Messages.addGlobalInfo("Create was successful. {0}", createdManufacturer.getId());
                // Reset the selected instance to null
                selectedManufacturer = null;

            } else {
                manufacturerService.updateManufacturer(selectedManufacturer);

                Messages.addGlobalInfo("Update was successful");

            }

            // Fetch a list of objects from the data source
            manufacturers = manufacturerService.getAllManufacturers();
            PrimeFaces.current().ajax().update("dialogs:messages", "form:dt-Manufacturers");

            // Hide the PrimeFaces dialog
            PrimeFaces.current().executeScript("PF('manageManufacturerDialog').hide()");
        } catch (RuntimeException ex) { // handle application generated exceptions
            Messages.addGlobalError(ex.getMessage());
        } catch (Exception ex) {    // handle system generated exceptions
            Messages.addGlobalError("Save not successful.");
            handleException(ex);
        }

    }

    /**
     * Event handler for Delete to delete selected data.
     */
    public void onDelete() {
        try {
            // Get the unique name of the Json object to delete
            selectedId = selectedManufacturer.getId();
            manufacturerService.deleteManufacturerById(selectedId);
            Messages.addGlobalInfo("Delete was successful for id of {0}", selectedId);
            // Fetch new data from the data source
            manufacturers = manufacturerService.getAllManufacturers();

            PrimeFaces.current().ajax().update("dialogs:messages", "form:dt-Manufacturers");
        } catch (RuntimeException ex) { // handle application generated exceptions
            Messages.addGlobalError(ex.getMessage());
        } catch (Exception ex) {    // handle system generated exceptions
            Messages.addGlobalError("Delete not successful.");
            handleException(ex);
        }

    }

    /**
     * This method is used to handle exceptions and display root cause to user.
     *
     * @param ex The Exception to handle.
     */
    protected void handleException(Exception ex) {
        StringBuilder details = new StringBuilder();
        Throwable causes = ex;
        while (causes.getCause() != null) {
            details.append(ex.getMessage());
            details.append("    Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }
        Messages.create(ex.getMessage()).detail(details.toString()).error().add("errors");
    }
}
