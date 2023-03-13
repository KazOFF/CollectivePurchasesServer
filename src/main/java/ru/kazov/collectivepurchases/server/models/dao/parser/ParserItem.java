package ru.kazov.collectivepurchases.server.models.dao.parser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "parser_item")
public class ParserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ParserCategory category;

    @Column(name = "url")
    private String url;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "parser_item_picture", joinColumns = @JoinColumn(name = "parser_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_parseritem_pictures",
                    foreignKeyDefinition = "foreign key (parser_item_id) references parser_item on delete cascade"))
    @Column(name = "picture")
    @Fetch(FetchMode.SELECT)
    private List<String> pictures = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "parser_item_price", joinColumns = @JoinColumn(name = "parser_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_parseritem_pictures",
                    foreignKeyDefinition = "foreign key (parser_item_id) references parser_item on delete cascade"))
    @Column(name = "price")
    @Fetch(FetchMode.SELECT)
    private Map<String, Double> prices = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "parser_item_property", joinColumns = @JoinColumn(name = "parser_item_id"),
            foreignKey = @ForeignKey(
                    name = "fk_parseritem_pictures",
                    foreignKeyDefinition = "foreign key (parser_item_id) references parser_item on delete cascade"))
    @Column(name = "property")
    @Fetch(FetchMode.SELECT)
    private Map<String, String> properties = new HashMap<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public void setCategory(ParserCategory parserCategory) {
        category = parserCategory;

        if (parserCategory == null)
            return;

        parserCategory.getItems().add(this);
    }

}
