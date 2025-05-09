export interface OrderItem {
    id: string;
    orderId: string;
    productId: string;
    shopId: string;
    quantity: number;
    unitPrice: number;
    subtotal: number;
    status: string;
    createdAt: string;
    updatedAt: string;

    // some additional fields from OrderItemResponseDto
    productName?: string;
    shopName?: string;
}

export interface OrderItemCreateDto {
    orderId: string;
    productId: string;
    shopId: string;
    quantity: number;
    unitPrice: number;
    status?: string;
}

export interface OrderItemUpdateDto {
    quantity?: number;
    unitPrice?: number;
    status?: string;
}

export interface Order {
    id: string;
    buyerId: string;
    buyerEmail?: string;
    totalAmount: number;
    status: string;
    shippingAddress?: string;
    trackingNumber?: string;
    notes?: string;
    createdAt: string;
    updatedAt: string;
    items: OrderItem[];
}

export interface OrderResponse {
    id: string;
    buyerId: string;
    buyerEmail?: string;
    totalAmount: number;
    status: string;
    shippingAddress?: string;
    shippingMethod?: string;
    trackingNumber?: string;
    notes?: string;
    createdAt: string;
    updatedAt: string;
    items: OrderItem[];
}

export interface CreateOrderDto {
    buyerId: string;
    totalAmount: number;
    status?: string;
    shippingAddress?: string;
    shippingMethod?: string;
    trackingNumber?: string;
    notes?: string;
}

export interface OrderUpdateDto {
    totalAmount?: number;
    status?: string;
    shippingAddress?: string;
    shippingMethod?: string;
    trackingNumber?: string;
    notes?: string;
}