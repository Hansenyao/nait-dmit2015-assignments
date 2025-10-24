package dmit2015.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {
    private static final long serialVersionUID = -8584492550473513242L;
    @NotNull
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @NotNull
    @Column(name = "LINE_ITEM_ID", nullable = false)
    private Long lineItemId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId entity = (OrderItemId) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.lineItemId, entity.lineItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, lineItemId);
    }

}