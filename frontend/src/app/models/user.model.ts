export interface User {
    userId: string;
    email: string;
    token: string;
    role: string;
    phone?: string;
    firstName: string;
    lastName: string;
    profileImageUrl: string;
    isVerified: string;
}