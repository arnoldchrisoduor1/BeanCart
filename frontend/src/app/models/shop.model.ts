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
}