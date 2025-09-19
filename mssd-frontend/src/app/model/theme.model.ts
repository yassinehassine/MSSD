export interface Theme {
  id: number;
  name: string;
  slug: string;
  description?: string;
  imageUrl?: string;
  published: boolean;
  createdAt: string;
  updatedAt: string;
  formations?: Formation[];
}

export interface ThemeRequest {
  name: string;
  slug: string;
  description?: string;
  imageUrl?: string;
  published: boolean;
}

export interface Formation {
  id: number;
  title: string;
  slug: string;
  description?: string;
  category: string;
  price: number;
  duration: string;
  imageUrl?: string;
  level: 'BEGINNER' | 'INTERMEDIATE' | 'EXPERT';
  published: boolean;
  createdAt: string;
  updatedAt: string;
  themeId?: number;
  themeName?: string;
}