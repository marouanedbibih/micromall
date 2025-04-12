import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { OrderListModule } from 'primeng/orderlist';
import { PickListModule } from 'primeng/picklist';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TagModule } from 'primeng/tag';
import { ProductService } from '../../../modules/catalog/services/product.service';
import { Product } from '../../../modules/catalog/catalog.model';

@Component({
    selector: 'products-widget',
    standalone: true,
    imports: [CommonModule, DataViewModule, FormsModule, SelectButtonModule, PickListModule, OrderListModule, TagModule, ButtonModule],
    template: ` <div class="flex flex-col px-32">
        <div class="card">
            <p-dataview [value]="products" [layout]="layout">
                <ng-template #grid let-items>
                    <div class="grid grid-cols-12 gap-4">
                        <div *ngFor="let item of items; let i = index" class="col-span-12 sm:col-span-6 lg:col-span-4 p-2">
                            <div class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded flex flex-col">
                                <div class="bg-surface-50 flex justify-center rounded p-6">
                                    <div class="relative mx-auto">
                                        <img class="rounded w-full" src="{{ item.imageUrl }}" [alt]="item.name" style="max-width: 300px" />
                                    </div>
                                </div>
                                <div class="pt-12">
                                    <div class="flex flex-row justify-between items-start gap-2">
                                        <div>
                                            <span class="font-medium text-surface-500 dark:text-surface-400 text-sm">{{ item.category.title }}</span>
                                            <div class="text-lg font-medium mt-1">{{ item.title }}</div>
                                        </div>
                                    </div>
                                    <div class="flex flex-col gap-6 mt-6">
                                        <span class="text-2xl font-semibold">$ {{ item.price }}</span>
                                        <div class="flex gap-2">
                                            <p-button icon="pi pi-shopping-cart" label="Add to Cart"  class="flex-auto whitespace-nowrap" styleClass="w-full"></p-button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </ng-template>
            </p-dataview>
        </div>

    </div>`,
    styles: `
        ::ng-deep {
            .p-orderlist-list-container {
                width: 100%;
            }
        }
    `,
    providers: [ProductService]
})
export class ProductsList {
    layout: 'grid' | 'list' = 'grid';

    options = ['list', 'grid'];

    products: Product[] = [];

    sourceCities: any[] = [];

    targetCities: any[] = [];

    orderCities: any[] = [];

    constructor(private productService: ProductService) {}

    ngOnInit() {
        this.fetchProducts();
    }

    fetchProducts() {
        this.productService.fetchPaginatedProducts(1,10,"id","desc").subscribe({
            next: (res) => {
                console.log("Prodcuts data", res);
                this.products = res.content;
            },
            error: (err) => {
                console.error(err);
            }
        })
    }

}
