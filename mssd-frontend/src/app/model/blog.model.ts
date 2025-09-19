export interface Blog {
  id?: number;
  title: string;
  slug: string;
  excerpt?: string;
  content: string;
  imageUrl?: string;
  author?: string;
  category?: string;
  tags?: string;
  published?: boolean;
  active?: boolean;
  publishedAt?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface BlogRequest {
  title: string;
  slug?: string;
  excerpt?: string;
  content: string;
  imageUrl?: string;
  author?: string;
  category?: string;
  tags?: string;
  published?: boolean;
}

export interface BlogPageResponse {
  content: Blog[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}