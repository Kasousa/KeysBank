import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { LoginForm } from '../../../components/auth/LoginForm';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../../context/AuthContext';

// Mock da API
vi.mock('../../../services/api', () => ({
  login: vi.fn(),
}));

const Wrapper = ({ children }) => (
  <BrowserRouter>
    <AuthProvider>{children}</AuthProvider>
  </BrowserRouter>
);

describe('LoginForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('deve renderizar formulário de login', () => {
    render(<LoginForm />, { wrapper: Wrapper });

    expect(screen.getByText('Acesse sua conta')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('0001')).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/343316/)).toBeInTheDocument();
  });

  it('deve validar agência obrigatória', async () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const submitButton = screen.getByText('Entrar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Agência é obrigatória')).toBeInTheDocument();
    });
  });

  it('deve validar agência com 4 dígitos', async () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const agencyInput = screen.getByPlaceholderText('0001');
    await userEvent.type(agencyInput, '123');

    const submitButton = screen.getByText('Entrar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Agência deve ter 4 dígitos')).toBeInTheDocument();
    });
  });

  it('deve validar número da conta obrigatório', async () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const submitButton = screen.getByText('Entrar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Número da conta é obrigatório')).toBeInTheDocument();
    });
  });

  it('deve validar número da conta com 6 dígitos', async () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const accountInput = screen.getByPlaceholderText(/343316/);
    await userEvent.type(accountInput, '12345');

    const submitButton = screen.getByText('Entrar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Conta deve ter 6 dígitos')).toBeInTheDocument();
    });
  });

  it('deve permitir entrada apenas de números', async () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const agencyInput = screen.getByPlaceholderText('0001') as HTMLInputElement;
    await userEvent.type(agencyInput, 'abc123');

    expect(agencyInput.value).toBe('123');
  });

  it('deve renderizar link para signup', () => {
    render(<LoginForm />, { wrapper: Wrapper });

    const signupLink = screen.getByText('Criar conta');
    expect(signupLink).toBeInTheDocument();
  });
});
