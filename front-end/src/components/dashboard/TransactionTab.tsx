import { useState } from 'react';
import { ArrowDownRight, ArrowUpRight, Loader2, Plus } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';
import { formatCurrency, maskCurrency, getNumericValue } from '@/utils/formatters';
import { createTransaction } from '@/services/api';
import { useAuth } from '@/context/AuthContext';

interface TransactionTabProps {
  onTransactionCreated?: () => void;
}

// 10 categorias visíveis ao cliente + 1 categoria interna (DAILY_BALANCE)
const TRANSACTION_CATEGORIES = [
  { value: 'SALARIO', label: 'Salário' },
  { value: 'DEPOSITO', label: 'Depósito' },
  { value: 'SAQUE', label: 'Saque' },
  { value: 'TRANSFERENCIA', label: 'Transferência' },
  { value: 'PAGAMENTO', label: 'Pagamento' },
  { value: 'FATURA_CARTAO', label: 'Fatura Cartão' },
  { value: 'RESGATE_INVESTIMENTO', label: 'Resgate de Investimento' },
  { value: 'TAXA_SERVICO', label: 'Taxa/Serviço' },
  { value: 'REEMBOLSO', label: 'Reembolso' },
  { value: 'OUTRO', label: 'Outro' },
];

// Categoria interna usada automaticamente pelo sistema
const INTERNAL_CATEGORIES = ['DAILY_BALANCE', 'BONUS_ABERTURA'];

export function TransactionTab({ onTransactionCreated }: TransactionTabProps) {
  const { accountId } = useAuth();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [form, setForm] = useState({
    description: '',
    type: 'CREDIT' as 'CREDIT' | 'DEBIT',
    category: 'SALARIO',
    amount: '',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!form.description.trim()) {
      newErrors.description = 'Descrição é obrigatória';
    } else if (form.description.length < 3) {
      newErrors.description = 'Descrição deve ter pelo menos 3 caracteres';
    } else if (form.description.length > 255) {
      newErrors.description = 'Descrição deve ter no máximo 255 caracteres';
    }

    const numericAmount = getNumericValue(form.amount);
    if (!form.amount || numericAmount <= 0) {
      newErrors.amount = 'Valor deve ser maior que zero';
    } else if (numericAmount > 999999.99) {
      newErrors.amount = 'Valor máximo é R$ 999.999,99';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate() || !accountId) return;

    setIsLoading(true);
    try {
      await createTransaction({
        accountId,
        description: form.description.trim(),
        type: form.type,
        category: form.category,
        amount: getNumericValue(form.amount),
      });

      toast({
        title: 'Transação criada!',
        description: `${form.type === 'CREDIT' ? 'Crédito' : 'Débito'} de ${form.amount} registrado com sucesso.`,
      });

      setForm({ description: '', type: 'CREDIT', amount: '' });
      setErrors({});
      onTransactionCreated?.();
    } catch (error) {
      toast({
        title: 'Erro ao criar transação',
        description: 'Tente novamente mais tarde.',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\D/g, '');
    if (value === '') {
      setForm({ ...form, amount: '' });
    } else {
      setForm({ ...form, amount: maskCurrency(value) });
    }
  };

  const handleClear = () => {
    setForm({ description: '', type: 'CREDIT', category: 'SALARIO', amount: '' });
    setErrors({});
  };

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="text-lg font-semibold flex items-center gap-2">
          <Plus className="w-5 h-5 text-primary" />
          Nova Transação
        </CardTitle>
      </CardHeader>

      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* 1. Categoria */}
          <div className="space-y-2">
            <Label htmlFor="category">Categoria</Label>
            <select
              id="category"
              value={form.category}
              onChange={(e) => setForm({ ...form, category: e.target.value })}
              className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground"
            >
              {TRANSACTION_CATEGORIES.map((cat) => (
                <option key={cat.value} value={cat.value}>
                  {cat.label}
                </option>
              ))}
            </select>
          </div>

          {/* 2. Tipo de Transação */}
          <div className="space-y-2">
            <Label>Tipo de Transação</Label>
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={() => setForm({ ...form, type: 'CREDIT' })}
                className={`flex items-center justify-center gap-2 p-4 rounded-xl border-2 transition-all ${
                  form.type === 'CREDIT'
                    ? 'border-success bg-success/10 text-success'
                    : 'border-border hover:border-success/50 text-muted-foreground hover:text-success'
                }`}
              >
                <ArrowDownRight className="w-5 h-5" />
                <span className="font-medium">Crédito</span>
              </button>
              <button
                type="button"
                onClick={() => setForm({ ...form, type: 'DEBIT' })}
                className={`flex items-center justify-center gap-2 p-4 rounded-xl border-2 transition-all ${
                  form.type === 'DEBIT'
                    ? 'border-destructive bg-destructive/10 text-destructive'
                    : 'border-border hover:border-destructive/50 text-muted-foreground hover:text-destructive'
                }`}
              >
                <ArrowUpRight className="w-5 h-5" />
                <span className="font-medium">Débito</span>
              </button>
            </div>
          </div>

          {/* 3. Descrição da Transação */}
          <div className="space-y-2">
            <Label htmlFor="description">Descrição da Transação</Label>
            <Input
              id="description"
              placeholder="Ex: Salário de janeiro, Conta de água, etc."
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              className={errors.description ? 'border-destructive' : ''}
              maxLength={255}
            />
            {errors.description && (
              <p className="text-sm text-destructive">{errors.description}</p>
            )}
            <p className="text-xs text-muted-foreground text-right">
              {form.description.length}/255
            </p>
          </div>

          {/* 4. Valor */}
          <div className="space-y-2">
            <Label htmlFor="amount">Valor</Label>
            <Input
              id="amount"
              placeholder="R$ 0,00"
              value={form.amount}
              onChange={handleAmountChange}
              className={`text-lg font-semibold ${errors.amount ? 'border-destructive' : ''}`}
            />
            {errors.amount && (
              <p className="text-sm text-destructive">{errors.amount}</p>
            )}
          </div>

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={handleClear}
              className="flex-1"
              disabled={isLoading}
            >
              Limpar
            </Button>
            <Button
              type="submit"
              className="flex-1"
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  Registrando...
                </>
              ) : (
                'Registrar Transação'
              )}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}
