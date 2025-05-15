export interface Shop {
    id: string;
    name: string;
    description: string;
    logoUrl: string;
    address: string | null;
    phone: string;
    email: string;
    active: boolean;
    createdAt: string;
    updatedAt: string;
    sellerId: string;
    sellerName: string;
    sellerEmail: string;
    verified: boolean;
    rating?: number;  // Added for display purposes
    reviews?: number; // Added for display purposes
    tags?: string[];  // Added for display purposes
}