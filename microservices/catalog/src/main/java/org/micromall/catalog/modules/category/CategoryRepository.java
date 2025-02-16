package org.micromall.catalog.modules.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    // Query method to search categories by title or description
    @Query("SELECT c FROM Category c WHERE c.title LIKE %?1% OR c.description LIKE %?1%")
    Page<Category> search(String keyword, Pageable pageable);

}
