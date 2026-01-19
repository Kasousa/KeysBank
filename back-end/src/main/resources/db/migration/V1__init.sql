CREATE TABLE customers (
  id UUID PRIMARY KEY,
  name TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customers(id),
  agency TEXT NOT NULL,
  account_number TEXT NOT NULL UNIQUE,
  status TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE transactions (
  id UUID PRIMARY KEY,
  account_id UUID NOT NULL REFERENCES accounts(id),
  type TEXT NOT NULL,           -- CREDIT / DEBIT
  category TEXT NOT NULL,       -- BONUS_ABERTURA, TRANSFERENCIA, PAGAMENTO_CONTA
  amount NUMERIC(18,2) NOT NULL,
  description TEXT,
  correlation_id UUID,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_account_created_at
ON transactions(account_id, created_at DESC);

CREATE TABLE bills (
  id UUID PRIMARY KEY,
  account_id UUID NOT NULL REFERENCES accounts(id),
  description TEXT NOT NULL,
  amount NUMERIC(18,2) NOT NULL,
  due_date DATE NOT NULL,
  status TEXT NOT NULL,         -- PENDENTE / PAGA / CANCELADA
  paid_at TIMESTAMP
);