export interface Portfolio {
  id?: number;
  title: string;
  description: string;
  formationId: number;
  formationName?: string;
  formationCategory?: string;
  category?: string; // Portfolio's own category
  imageUrl?: string;
  companyLogo?: string;
  clientName?: string;
  projectDate?: string; // Format: YYYY-MM-DD
  projectUrl?: string;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
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
