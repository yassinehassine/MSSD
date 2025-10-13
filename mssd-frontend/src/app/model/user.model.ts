export interface User {
  id?: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'USER';
  createdAt?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user?: User;
  token?: string;
}
