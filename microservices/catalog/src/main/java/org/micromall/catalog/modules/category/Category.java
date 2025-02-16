package org.micromall.catalog.modules.category;

import java.util.List;
import java.util.Objects;

import org.micromall.catalog.modules.product.Product;
import org.micromall.catalog.utils.MyEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
@Table(name = "categories")
public class Category extends MyEntity {

    private String title;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products;

    // To String method
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // Equals method
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(title, category.title) &&
                Objects.equals(description, category.description);

    }

    // Hash code method
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }

}
