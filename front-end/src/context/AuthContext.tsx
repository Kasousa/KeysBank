import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import type { AuthState, LoginResponse } from '@/types';

interface AuthContextType extends AuthState {
  login: (data: LoginResponse) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

const STORAGE_KEY = 'keysbank_auth';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [authState, setAuthState] = useState<AuthState>(() => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      try {
        const parsed = JSON.parse(stored);
        return { ...parsed, isAuthenticated: true };
      } catch {
        return {
          accountId: null,
          agency: null,
          accountNumber: null,
          customerName: null,
          isAuthenticated: false,
        };
      }
    }
    return {
      accountId: null,
      agency: null,
      accountNumber: null,
      customerName: null,
      isAuthenticated: false,
    };
  });

  const login = useCallback((data: LoginResponse) => {
    const newState: AuthState = {
      accountId: data.accountId,
      agency: data.agency,
      accountNumber: data.accountNumber,
      customerName: data.customerName,
      isAuthenticated: true,
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(newState));
    setAuthState(newState);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY);
    setAuthState({
      accountId: null,
      agency: null,
      accountNumber: null,
      customerName: null,
      isAuthenticated: false,
    });
  }, []);

  return (
    <AuthContext.Provider value={{ ...authState, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
