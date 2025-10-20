package dmit2015.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Column(name = "UNIT_PRICE", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "PRODUCT_DETAILS")
    private byte[] productDetails;

    @Column(name = "PRODUCT_IMAGE")
    private byte[] productImage;

    @Size(max = 512)
    @Column(name = "IMAGE_MIME_TYPE", length = 512)
    private String imageMimeType;

    @Size(max = 512)
    @Column(name = "IMAGE_FILENAME", length = 512)
    private String imageFilename;

    @Size(max = 512)
    @Column(name = "IMAGE_CHARSET", length = 512)
    private String imageCharset;

    @Column(name = "IMAGE_LAST_UPDATED")
    private LocalDate imageLastUpdated;

    @OneToMany(mappedBy = "product")
    private Set<Inventory> inventories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public byte[] getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(byte[] productDetails) {
        this.productDetails = productDetails;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getImageMimeType() {
        return imageMimeType;
    }

    public void setImageMimeType(String imageMimeType) {
        this.imageMimeType = imageMimeType;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getImageCharset() {
        return imageCharset;
    }

    public void setImageCharset(String imageCharset) {
        this.imageCharset = imageCharset;
    }

    public LocalDate getImageLastUpdated() {
        return imageLastUpdated;
    }

    public void setImageLastUpdated(LocalDate imageLastUpdated) {
        this.imageLastUpdated = imageLastUpdated;
    }

    public Set<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        this.inventories = inventories;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

}