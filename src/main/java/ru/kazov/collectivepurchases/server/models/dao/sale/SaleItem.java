package ru.kazov.collectivepurchases.server.models.dao.sale;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "sale_item")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private SaleCategory category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_comment")
    private String priceComment;

    @Column(name = "url")
    private String url;

    @Column(name = "vk_photo_url")
    private String vkPhotoUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sale_item_picture", joinColumns = @JoinColumn(name = "sale_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_saleitem_pictures",
                    foreignKeyDefinition = "foreign key (sale_item_id) references sale_item on delete cascade"))
    @Column(name = "picture")
    @Fetch(FetchMode.SELECT)
    private List<String> pictures;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sale_item_price", joinColumns = @JoinColumn(name = "sale_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_saleitem_prices",
                    foreignKeyDefinition = "foreign key (sale_item_id) references sale_item on delete cascade"))
    @Column(name = "price")
    @Fetch(FetchMode.SELECT)
    private Map<String, Double> prices;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sale_item_property", joinColumns = @JoinColumn(name = "sale_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_saleitem_properties",
                    foreignKeyDefinition = "foreign key (sale_item_id) references sale_item on delete cascade"))
    @Column(name = "property")
    @Fetch(FetchMode.SELECT)
    private Map<String, String> properties;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public void setCategory(SaleCategory saleCategory) {
        saleCategory.getItems().add(this);
        this.category = saleCategory;
    }
}