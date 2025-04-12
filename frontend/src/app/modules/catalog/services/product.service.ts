import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../env';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private baseUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    // Create a new product
    createProduct(request: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/api/v1/product`, request);
    }

    // Create a product with an image
    createProductWithImage(request: any, image: File): Observable<any> {
        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
        formData.append('image', image);

        return this.http.post('/api/v2/product', formData);
    }

    // Update a product
    updateProduct(id: number, request: any): Observable<any> {
        return this.http.put(`${this.baseUrl}/${id}`, request);
    }

    // Delete a product
    deleteProduct(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // Fetch product by ID
    fetchProductById(id: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/${id}`);
    }

    // Fetch list of products
    fetchAllProducts(): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}s/list`);
    }

    // Fetch paginated products
    fetchPaginatedProducts(page: number = 1, size: number = 5, sortBy: string, sortDirection: string): Observable<any> {
        const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sortBy', sortBy).set('sortDirection', sortDirection);

        return this.http.get(`${this.baseUrl}/api/v1/products`, { params });
    }

    // Fetch purchase products by IDs
    fetchPurchaseProducts(ids: number[]): Observable<any[]> {
        const params = new HttpParams().set('ids', ids.join(','));
        return this.http.get<any[]>('/api/v1/products/purchase', { params });
    }
}
