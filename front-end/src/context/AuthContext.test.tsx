import { describe, it, expect, beforeEach, vi } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { useAuth } from '../../../context/AuthContext';
import { AuthProvider } from '../../../context/AuthContext';

const wrapper = ({ children }) => <AuthProvider>{children}</AuthProvider>;

describe('useAuth Hook', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('deve retornar estado inicial não autenticado', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.accountId).toBeNull();
    expect(result.current.customerName).toBeNull();
  });

  it('deve fazer login com dados corretos', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });

    const loginData = {
      accountId: 'uuid-123',
      agency: '0001',
      accountNumber: '343316',
      customerName: 'João Silva',
    };

    act(() => {
      result.current.login(loginData);
    });

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.accountId).toBe('uuid-123');
    expect(result.current.customerName).toBe('João Silva');
  });

  it('deve fazer logout', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });

    const loginData = {
      accountId: 'uuid-123',
      agency: '0001',
      accountNumber: '343316',
      customerName: 'João Silva',
    };

    act(() => {
      result.current.login(loginData);
    });

    expect(result.current.isAuthenticated).toBe(true);

    act(() => {
      result.current.logout();
    });

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.accountId).toBeNull();
  });

  it('deve persistir dados no localStorage', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });

    const loginData = {
      accountId: 'uuid-123',
      agency: '0001',
      accountNumber: '343316',
      customerName: 'João Silva',
    };

    act(() => {
      result.current.login(loginData);
    });

    const stored = localStorage.getItem('keysbank_auth');
    expect(stored).toBeTruthy();
    
    const parsed = JSON.parse(stored!);
    expect(parsed.accountId).toBe('uuid-123');
  });

  it('deve restaurar dados do localStorage', () => {
    const loginData = {
      accountId: 'uuid-123',
      agency: '0001',
      accountNumber: '343316',
      customerName: 'João Silva',
    };

    localStorage.setItem('keysbank_auth', JSON.stringify(loginData));

    const { result } = renderHook(() => useAuth(), { wrapper });

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.accountId).toBe('uuid-123');
  });
});
