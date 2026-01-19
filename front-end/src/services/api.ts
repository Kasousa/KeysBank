import type {
  Customer,
  Account,
  LoginResponse,
  Transaction,
  Statement,
  CreateCustomerRequest,
  CreateAccountRequest,
  CreateTransactionRequest,
  StatementFilters,
} from '@/types';

const API_BASE_URL = 'http://localhost:8080';

class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const message = await response.text().catch(() => 'Erro desconhecido');
    throw new ApiError(response.status, message);
  }
  return response.json();
}

// Customers
export async function createCustomer(data: CreateCustomerRequest): Promise<Customer> {
  const response = await fetch(`${API_BASE_URL}/customers`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return handleResponse<Customer>(response);
}

// Accounts
export async function createAccount(data: CreateAccountRequest): Promise<Account> {
  const response = await fetch(`${API_BASE_URL}/accounts`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return handleResponse<Account>(response);
}

export async function login(agency: string, accountNumber: string): Promise<LoginResponse> {
  const response = await fetch(
    `${API_BASE_URL}/accounts/login?agency=${encodeURIComponent(agency)}&accountNumber=${encodeURIComponent(accountNumber)}`
  );
  return handleResponse<LoginResponse>(response);
}

// Transactions
export async function createTransaction(data: CreateTransactionRequest): Promise<Transaction> {
  const response = await fetch(`${API_BASE_URL}/transaction`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      accountId: data.accountId,
      type: data.type,
      category: data.category,
      amount: data.amount,
      description: data.description,
    }),
  });
  return handleResponse<Transaction>(response);
}

// Statement
export async function getStatement(
  accountId: string,
  filters?: StatementFilters
): Promise<Statement[]> {
  const params = new URLSearchParams();
  
  if (filters?.startDate) params.append('startDate', filters.startDate);
  if (filters?.endDate) params.append('endDate', filters.endDate);
  if (filters?.type) params.append('type', filters.type);

  const queryString = params.toString();
  const url = `${API_BASE_URL}/accounts/${accountId}/statement${queryString ? `?${queryString}` : ''}`;
  
  const response = await fetch(url);
  return handleResponse<Statement[]>(response);
}

export async function getBalance(accountId: string): Promise<number> {
  const statements = await getStatement(accountId, { type: 'BALANCE' });
  if (statements.length === 0) return 0;
  
  // Get the most recent balance
  const sorted = statements.sort((a, b) => 
    new Date(b.date).getTime() - new Date(a.date).getTime()
  );
  return sorted[0].amount;
}
