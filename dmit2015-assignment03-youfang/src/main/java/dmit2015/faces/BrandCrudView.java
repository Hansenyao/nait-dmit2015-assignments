package dmit2015.faces;

import dmit2015.model.Brand;
import dmit2015.service.BrandService;
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
import java.util.List;

/**
 * This Jakarta Faces backing bean class contains the data and event handlers
 * to perform CRUD operations using a PrimeFaces DataTable configured to perform CRUD.
 */
@Named("currentBrandCrudView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
public class BrandCrudView implements Serializable {

    @Inject
    @Named("jakartaPersistenceBrandService")
    private BrandService brandService;

    /**
     * The selected Brand instance to create, edit, update or delete.
     */
    @Getter
    @Setter
    private Brand selectedBrand;

    /**
     * The unique name of the selected Brand instance.
     */
    @Getter
    @Setter
    private String selectedId;

    /**
     * The list of Brand objects fetched from the data source
     */
    @Getter
    private List<Brand> brands;

    /**
     * Fetch all Brand from the data source.
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
            brands = brandService.getAllBrands();
        } catch (Exception e) {
            Messages.addGlobalError("Error getting brands %s", e.getMessage());
        }
    }
}
