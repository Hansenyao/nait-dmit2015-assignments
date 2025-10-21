package dmit2015.faces;

import dmit2015.entity.Inventory;
import dmit2015.entity.Product;
import dmit2015.persistence.InventoryRepository;
import dmit2015.persistence.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import org.omnifaces.util.Messages;

/**
 * View-scoped backing bean: lives across postbacks on the SAME view.
 * Destroyed when navigating away to a different view.
 */
@Named("inventoryQueryView")
@ViewScoped // Survives postbacks (including AJAX) on this view; Serializable required
public class InventoryQueryView implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(InventoryQueryView.class.getName());

    @Inject
    private ProductRepository productRepository;

    @Inject
    private InventoryRepository inventoryRepository;

    @Getter
    private Product selectedProduct;

    @Getter
    private List<Inventory> queryResults;

    public List<Product> completeProduct(String query) {
        return productRepository.findProductsByName("%" + query + "%");
    }

    @PostConstruct // Runs after @Inject fields are initialized (once per view instance)
    public void init() {
    }

    public void onSubmit() {
        try {
            queryResults = inventoryRepository.findInventoryByProductId(selectedProduct.getId());
            Messages.addGlobalInfo("Query returned {0} records", queryResults.size());
        } catch (Exception ex) {
            handleException(ex, "Unable to process your request.");
        }
    }

    public void onClear() {
        queryResults = null;
        selectedProduct = null;
    }

    /**
     * Log server-side and show a concise root-cause chain in the UI.
     * Assumes the page includes <p:messages id="error" />.
     */
    protected void handleException(Throwable ex, String userMessage) {
        LOG.log(Level.SEVERE, userMessage != null ? userMessage : "Unhandled error", ex);

        StringBuilder details = new StringBuilder();
        Throwable t = ex;
        while (t != null) {
            String msg = t.getMessage();
            if (msg != null && !msg.isBlank()) {
                details.append(t.getClass().getSimpleName())
                        .append(": ")
                        .append(msg);
                if (t.getCause() != null) details.append("  Caused by: ");
            }
            t = t.getCause();
        }

        try {
            Messages.create(userMessage != null ? userMessage : "An unexpected error occurred.")
                    .detail(details.toString())
                    .error()
                    .add("messages");
        } catch (Throwable ignored) {
            // No FacesContext available; skip UI message safely.
        }
    }
}