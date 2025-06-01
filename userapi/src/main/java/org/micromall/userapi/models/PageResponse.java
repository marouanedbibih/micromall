package org.micromall.userapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic pagination response model
 * @param <T> Type of data being paginated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    /**
     * The paginated data
     */
    private List<T> content;
    
    /**
     * Current page number (0-based)
     */
    private int pageNumber;
    
    /**
     * Size of each page
     */
    private int pageSize;
    
    /**
     * Total number of elements
     */
    private long totalElements;
    
    /**
     * Total number of pages
     */
    private int totalPages;
    
    /**
     * Whether this is the first page
     */
    private boolean first;
    
    /**
     * Whether this is the last page
     */
    private boolean last;
    
    /**
     * Static factory method to create a PageResponse object
     * 
     * @param content List of content for the current page
     * @param pageNumber Current page number (0-based)
     * @param pageSize Size of each page
     * @param totalElements Total number of elements across all pages
     * @return PageResponse object
     */
    public static <T> PageResponse<T> of(
            List<T> content,
            int pageNumber,
            int pageSize,
            long totalElements
    ) {
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / (double) pageSize) : 0;
        boolean isFirst = pageNumber == 0;
        boolean isLast = pageNumber >= totalPages - 1;
        
        return PageResponse.<T>builder()
                .content(content)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(isFirst)
                .last(isLast)
                .build();
    }
}
