// KeysBank Types

export interface Customer {
  id: string;
  name: string;
  email: string;
  createdAt: string;
}

export interface Account {
  id: string;
  customerId: string;
  agency: string;
  accountNumber: string;
  status?: string;
  createdAt: string;
}

export interface LoginResponse {
  accountId: string;
  agency: string;
  accountNumber: string;
  customerName: string;
}

export interface Transaction {
  id: string;
  description: string;
  type: 'CREDIT' | 'DEBIT' | 'BALANCE';
  amount: number;
  date?: string;
  createdAt: string;
}

export interface Statement {
  id: string;
  description: string;
  type: 'CREDIT' | 'DEBIT' | 'BALANCE';
  category: string;
  amount: number;
  createdAt: string;
  date?: string;
}

export interface CreateCustomerRequest {
  name: string;
  email: string;
}

export interface CreateAccountRequest {
  customerId: string;
}

export interface CreateTransactionRequest {
  accountId: string;
  description: string;
  type: 'CREDIT' | 'DEBIT';
  category: string;
  amount: number;
}

export interface StatementFilters {
  startDate?: string;
  endDate?: string;
  type?: 'CREDIT' | 'DEBIT' | 'BALANCE' | '';
}

export interface AuthState {
  accountId: string | null;
  agency: string | null;
  accountNumber: string | null;
  customerName: string | null;
  isAuthenticated: boolean;
}
