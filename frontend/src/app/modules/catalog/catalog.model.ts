export interface Product {
    id: number,
    title: string,
    description: string,
    price: number,
    imageUrl: string,
    category: Category,

}

export interface Category {
    id: number,
    title: string,
    description: string,
}


