export interface Portfolio {
  id?: number;
  companyName: string;
  trainingTitle: string;
  trainingDate: string;
  logoUrl?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface PortfolioResponse {
  content: Portfolio[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}