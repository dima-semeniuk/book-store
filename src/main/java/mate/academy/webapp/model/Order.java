package mate.academy.webapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        PENDING,
        COMPLETED,
        DELIVERED
    }
}
