export interface Product {
  id: string;
  shopId: string;
  name: string;
  description: string;
  price: number;
  discount?: number;
  imageUrl: string;
  quantity: number;
  category: string;
  isFeatured: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  rating?: number;  // Added for display purposes
  reviews?: number; // Added for display purposes
}
  
  export interface ProductCreateDto {
    shopId: string;
    name: string;
    description?: string;
    price: number;
    discount?: number;
    imageUrl?: string;
    quantity?: number;
    category: string;
    isFeatured?: boolean;
  }
  
  export interface ProductUpdateDto {
    id: string;
    name?: string;
    description?: string;
    price?: number;
    discount?: number;
    imageUrl?: string;
    quantity?: number;
    category?: string;
    isFeatured?: boolean;
    active?: boolean;
  }