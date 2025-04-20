import { Product } from "./product.model";

export interface Category {
    id?: string; // Optional if you need to identify categories
    name: string;
    products: Product[]; // Using your existing Product interface
    imageUrl?: string; // Optional category image
    description?: string; // Optional description
  }