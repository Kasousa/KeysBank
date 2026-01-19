import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Wallet, Loader2, Building, CreditCard } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';
import { useAuth } from '@/context/AuthContext';
import { login } from '@/services/api';

export function LoginForm() {
  const navigate = useNavigate();
  const { toast } = useToast();
  const { login: setAuth } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [form, setForm] = useState({ agency: '', accountNumber: '' });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!form.agency.trim()) {
      newErrors.agency = 'Agência é obrigatória';
    } else if (!/^\d{4}$/.test(form.agency)) {
      newErrors.agency = 'Agência deve ter 4 dígitos';
    }

    if (!form.accountNumber.trim()) {
      newErrors.accountNumber = 'Número da conta é obrigatório';
    } else if (!/^\d{6}$/.test(form.accountNumber)) {
      newErrors.accountNumber = 'Conta deve ter 6 dígitos';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setIsLoading(true);
    try {
      const response = await login(form.agency, form.accountNumber);
      setAuth(response);
      toast({
        title: 'Bem-vindo!',
        description: `Olá, ${response.customerName}!`,
      });
      navigate('/dashboard');
    } catch (error) {
      toast({
        title: 'Erro ao entrar',
        description: 'Agência ou conta inválida. Verifique seus dados.',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      <div className="w-full max-w-md animate-scale-in">
        <div className="flex justify-center mb-8">
          <div className="flex items-center gap-3">
            <div className="flex items-center justify-center w-14 h-14 rounded-2xl bg-primary shadow-elevated">
              <Wallet className="w-7 h-7 text-primary-foreground" />
            </div>
            <div>
              <h1 className="text-3xl font-bold text-foreground">KeysBank</h1>
              <p className="text-sm text-muted-foreground">Seu banco digital</p>
            </div>
          </div>
        </div>

        <Card className="shadow-elevated">
          <CardHeader className="text-center pb-4">
            <CardTitle className="text-xl">Acesse sua conta</CardTitle>
            <CardDescription>
              Entre com sua agência e número da conta
            </CardDescription>
          </CardHeader>

          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-5">
              <div className="space-y-2">
                <Label htmlFor="agency" className="flex items-center gap-2">
                  <Building className="w-4 h-4 text-muted-foreground" />
                  Agência
                </Label>
                <Input
                  id="agency"
                  placeholder="0001"
                  value={form.agency}
                  onChange={(e) => {
                    const value = e.target.value.replace(/\D/g, '').slice(0, 4);
                    setForm({ ...form, agency: value });
                  }}
                  className={errors.agency ? 'border-destructive' : ''}
                  maxLength={4}
                />
                {errors.agency && (
                  <p className="text-sm text-destructive">{errors.agency}</p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="accountNumber" className="flex items-center gap-2">
                  <CreditCard className="w-4 h-4 text-muted-foreground" />
                  Número da Conta
                </Label>
                <Input
                  id="accountNumber"
                  placeholder="123456"
                  value={form.accountNumber}
                  onChange={(e) => {
                    const value = e.target.value.replace(/\D/g, '').slice(0, 6);
                    setForm({ ...form, accountNumber: value });
                  }}
                  className={errors.accountNumber ? 'border-destructive' : ''}
                  maxLength={6}
                />
                {errors.accountNumber && (
                  <p className="text-sm text-destructive">{errors.accountNumber}</p>
                )}
              </div>

              <Button
                type="submit"
                className="w-full"
                size="lg"
                disabled={isLoading}
              >
                {isLoading ? (
                  <>
                    <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                    Entrando...
                  </>
                ) : (
                  'Entrar'
                )}
              </Button>
            </form>

            <div className="mt-6 text-center">
              <p className="text-sm text-muted-foreground">
                Não tem uma conta?{' '}
                <Link
                  to="/signup"
                  className="text-primary font-medium hover:underline"
                >
                  Criar conta
                </Link>
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
