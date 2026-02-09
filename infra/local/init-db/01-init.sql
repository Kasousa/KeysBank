-- Script de inicialização do banco de dados local
-- Este script é executado automaticamente quando o container PostgreSQL é criado pela primeira vez

-- Criar extensões úteis
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Log de inicialização
DO $$
BEGIN
  RAISE NOTICE 'Database initialized successfully';
  RAISE NOTICE 'Database: %', current_database();
  RAISE NOTICE 'Version: %', version();
END $$;
