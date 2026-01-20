import { describe, it, expect, beforeEach, vi } from 'vitest';
import { formatDate, formatCurrency, formatCategory, maskCurrency, getNumericValue } from '../../../utils/formatters';

describe('formatters', () => {
  describe('formatDate', () => {
    it('deve formatar data válida em PT-BR', () => {
      const result = formatDate('2026-01-19T10:30:00Z');
      expect(result).toMatch(/\d{2}\/\d{2}\/\d{4}/);
    });

    it('deve retornar "Data inválida" para string vazia', () => {
      const result = formatDate('');
      expect(result).toBe('Data inválida');
    });

    it('deve retornar "Data inválida" para undefined', () => {
      const result = formatDate(undefined);
      expect(result).toBe('Data inválida');
    });

    it('deve retornar "Data inválida" para data inválida', () => {
      const result = formatDate('invalid-date');
      expect(result).toBe('Data inválida');
    });
  });

  describe('formatCurrency', () => {
    it('deve formatar valor em BRL', () => {
      const result = formatCurrency(100);
      expect(result).toContain('R$');
      expect(result).toContain('100');
    });

    it('deve formatar valores decimais', () => {
      const result = formatCurrency(99.99);
      expect(result).toContain('99');
    });

    it('deve formatar zero', () => {
      const result = formatCurrency(0);
      expect(result).toContain('0');
    });
  });

  describe('formatCategory', () => {
    it('deve converter BONUS_ABERTURA para Bonus Abertura', () => {
      const result = formatCategory('BONUS_ABERTURA');
      expect(result).toBe('Bonus Abertura');
    });

    it('deve converter DAILY_BALANCE para Daily Balance', () => {
      const result = formatCategory('DAILY_BALANCE');
      expect(result).toBe('Daily Balance');
    });

    it('deve converter minúsculas', () => {
      const result = formatCategory('DEPOSITO');
      expect(result).toBe('Deposito');
    });

    it('deve manter espaços entre palavras', () => {
      const result = formatCategory('FATURA_CARTAO');
      expect(result).toBe('Fatura Cartao');
    });
  });

  describe('maskCurrency', () => {
    it('deve formatar valor em BRL', () => {
      const result = maskCurrency('10000');
      expect(result).toContain('R$');
      expect(result).toContain('100');
    });

    it('deve retornar R$ 0,00 para string vazia', () => {
      const result = maskCurrency('');
      expect(result).toBe('R$ 0,00');
    });
  });

  describe('getNumericValue', () => {
    it('deve extrair valor numérico de string formatada', () => {
      const result = getNumericValue('R$ 100,00');
      expect(result).toBe(100);
    });

    it('deve retornar 0 para string vazia', () => {
      const result = getNumericValue('');
      expect(result).toBe(0);
    });

    it('deve suportar decimais', () => {
      const result = getNumericValue('R$ 99,99');
      expect(result).toBeCloseTo(99.99, 2);
    });
  });
});
