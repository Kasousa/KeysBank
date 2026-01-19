import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Wallet, Loader2, User, Mail, ArrowLeft, CheckCircle } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';
import { createCustomer, createAccount } from '@/services/api';

export function SignupForm() {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [success, setSuccess] = useState<{ agency: string; accountNumber: string } | null>(null);
  const [form, setForm] = useState({ name: '', email: '' });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!form.name.trim()) {
      newErrors.name = 'Nome é obrigatório';
    } else if (form.name.length < 3) {
      newErrors.name = 'Nome deve ter pelo menos 3 caracteres';
    } else if (form.name.length > 100) {
      newErrors.name = 'Nome deve ter no máximo 100 caracteres';
    }

    if (!form.email.trim()) {
      newErrors.email = 'Email é obrigatório';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      newErrors.email = 'Email inválido';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setIsLoading(true);
    try {
      // Create customer
      const customer = await createCustomer({
        name: form.name.trim(),
        email: form.email.trim().toLowerCase(),
      });

      // Create account for customer
      const account = await createAccount({
        customerId: customer.id,
      });

      setSuccess({
        agency: account.agency,
        accountNumber: account.accountNumber,
      });

      toast({
        title: 'Conta criada com sucesso!',
        description: 'Sua conta foi criada. Use os dados abaixo para acessar.',
      });
    } catch (error: any) {
      const message = error.message?.includes('email')
        ? 'Este email já está em uso. Tente outro.'
        : 'Erro ao criar conta. Tente novamente.';
      toast({
        title: 'Erro ao criar conta',
        description: message,
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
  };

  if (success) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background p-4">
        <div className="w-full max-w-md animate-scale-in">
          <Card className="shadow-elevated">
            <CardContent className="pt-8 pb-8 text-center">
              <div className="flex justify-center mb-6">
                <div className="flex items-center justify-center w-16 h-16 rounded-full bg-success/10">
                  <CheckCircle className="w-8 h-8 text-success" />
                </div>
              </div>

              <h2 className="text-2xl font-bold text-foreground mb-2">Conta Criada!</h2>
              <p className="text-muted-foreground mb-8">
                Anote seus dados de acesso:
              </p>

              <div className="bg-muted/50 rounded-xl p-6 mb-8 space-y-4">
                <div>
                  <p className="text-sm text-muted-foreground">Agência</p>
                  <p className="text-2xl font-bold text-foreground">{success.agency}</p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">Número da Conta</p>
                  <p className="text-2xl font-bold text-foreground">{success.accountNumber}</p>
                </div>
              </div>

              <Button onClick={() => navigate('/')} className="w-full" size="lg">
                Acessar minha conta
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    );
  }

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
            <CardTitle className="text-xl">Criar nova conta</CardTitle>
            <CardDescription>
              Preencha seus dados para abrir sua conta
            </CardDescription>
          </CardHeader>

          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-5">
              <div className="space-y-2">
                <Label htmlFor="name" className="flex items-center gap-2">
                  <User className="w-4 h-4 text-muted-foreground" />
                  Nome Completo
                </Label>
                <Input
                  id="name"
                  placeholder="João Silva"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  className={errors.name ? 'border-destructive' : ''}
                  maxLength={100}
                />
                {errors.name && (
                  <p className="text-sm text-destructive">{errors.name}</p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="email" className="flex items-center gap-2">
                  <Mail className="w-4 h-4 text-muted-foreground" />
                  Email
                </Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="joao@email.com"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  className={errors.email ? 'border-destructive' : ''}
                />
                {errors.email && (
                  <p className="text-sm text-destructive">{errors.email}</p>
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
                    Criando conta...
                  </>
                ) : (
                  'Criar Conta'
                )}
              </Button>
            </form>

            <div className="mt-6 text-center">
              <Link
                to="/"
                className="inline-flex items-center gap-1 text-sm text-muted-foreground hover:text-primary transition-colors"
              >
                <ArrowLeft className="w-4 h-4" />
                Voltar ao login
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
