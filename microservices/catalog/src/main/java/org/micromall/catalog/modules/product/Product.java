package org.micromall.catalog.modules.product;
import java.util.Objects;

import org.micromall.catalog.modules.category.Category;
import org.micromall.catalog.utils.MyEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "products")
public class Product extends MyEntity {

    private String title;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // To String method
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    // Equals method
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(title, product.title)
                && Objects.equals(description, product.description) && Objects.equals(price, product.price);
    }

    // Hash code method
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price);
    }

}
