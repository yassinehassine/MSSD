export interface Portfolio {
  id?: number;
  
  // New PortfolioItem fields (primary)
  companyName?: string;
  trainingTitle?: string;
  trainingDate?: string; // Format: YYYY-MM-DD
  logoUrl?: string;
  description?: string;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
  
  // Old Portfolio fields (for backward compatibility with admin)
  title?: string;
  formationId?: number;
  formationName?: string;
  formationCategory?: string;
  category?: string;
  imageUrl?: string;
  clientName?: string;
  projectDate?: string;
  projectUrl?: string;
  companyLogoUrl?: string;
}

export interface Formation {
  id: number;
  title: string;
  category: string;
  description: string;
  price: number;
  duration: string;
  level: string;
  published: boolean;
}
