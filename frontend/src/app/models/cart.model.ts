export interface CartItem {
    id: string;
    productId: string;
    shopId: string;
    productName: string;
    productDescription: string;
    productPrice: number;
    productDiscount: number;
    productImageUrl: string;
    quantity: number;
    totalPrice: number;
}

export interface CartResponse {
    id: string;
    buyerId: string;
    createdAt: string;
    updatedAt: string;
    items: CartItem[];
    totalItems: number;
    subtotal: number;
    discount: number;
    total: number;
}

export interface CartItemDto {
    productId: string;
    shopId: string;
    quantity: number;
}

export interface CartItemUpdateDto {
    quantity: number;
}