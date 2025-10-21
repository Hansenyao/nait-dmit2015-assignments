package dmit2015.faces;

import dmit2015.entity.Product;
import dmit2015.persistence.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.convert.Converter;

@Named
@ApplicationScoped
@FacesConverter(value = "productConvert", managed = true)
public class ProductConverter implements Converter<Product> {
    @Inject
    private ProductRepository productRepository;

    @Override
    public Product getAsObject(FacesContext facesContext, UIComponent unComponent, String value) {
        if (value != null && !value.isBlank()) {
            try {
                long productId = Long.parseLong(value);
                return productRepository.findProductById(productId);
            } catch (NumberFormatException e) {
                throw  new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Invalid product name!",
                        null));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent unComponent, Product value) {
        if (value != null) {
            return value.getId().toString();
        } else {
            return null;
        }
    }
}
