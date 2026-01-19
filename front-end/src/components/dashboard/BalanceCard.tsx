import { useState, useEffect } from 'react';
import { Eye, EyeOff, TrendingUp, TrendingDown, RefreshCw } from 'lucide-react';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { formatCurrency } from '@/utils/formatters';
import { getBalance, getStatement } from '@/services/api';
import { useAuth } from '@/context/AuthContext';
import type { Statement } from '@/types';

export function BalanceCard() {
  const { accountId } = useAuth();
  const [balance, setBalance] = useState<number>(0);
  const [showBalance, setShowBalance] = useState(true);
  const [isLoading, setIsLoading] = useState(true);
  const [monthStats, setMonthStats] = useState({ credits: 0, debits: 0 });

  const fetchData = async () => {
    if (!accountId) return;
    setIsLoading(true);
    try {
      const bal = await getBalance(accountId);
      setBalance(bal);

      // Get this month's transactions
      const now = new Date();
      const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1);
      const statements = await getStatement(accountId, {
        startDate: startOfMonth.toISOString().split('T')[0],
        endDate: now.toISOString().split('T')[0],
      });

      const credits = statements
        .filter((s) => s.type === 'CREDIT')
        .reduce((sum, s) => sum + s.amount, 0);
      const debits = statements
        .filter((s) => s.type === 'DEBIT')
        .reduce((sum, s) => sum + s.amount, 0);

      setMonthStats({ credits, debits });
    } catch (error) {
      console.error('Failed to fetch balance:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [accountId]);

  return (
    <Card className="bg-gradient-to-br from-primary to-primary/80 text-primary-foreground shadow-elevated overflow-hidden">
      <CardContent className="p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <p className="text-primary-foreground/80 text-sm font-medium">Saldo Disponível</p>
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="icon"
              onClick={() => setShowBalance(!showBalance)}
              className="text-primary-foreground/80 hover:text-primary-foreground hover:bg-primary-foreground/10"
            >
              {showBalance ? <Eye className="w-5 h-5" /> : <EyeOff className="w-5 h-5" />}
            </Button>
            <Button
              variant="ghost"
              size="icon"
              onClick={fetchData}
              disabled={isLoading}
              className="text-primary-foreground/80 hover:text-primary-foreground hover:bg-primary-foreground/10"
            >
              <RefreshCw className={`w-5 h-5 ${isLoading ? 'animate-spin' : ''}`} />
            </Button>
          </div>
        </div>

        <div className="mb-8">
          {isLoading ? (
            <div className="h-12 w-48 bg-primary-foreground/20 rounded-lg animate-pulse" />
          ) : (
            <p className="text-4xl font-bold animate-balance">
              {showBalance ? formatCurrency(balance) : '••••••'}
            </p>
          )}
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="flex items-center gap-3 bg-primary-foreground/10 rounded-xl p-3">
            <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-success/20">
              <TrendingUp className="w-5 h-5 text-success" />
            </div>
            <div>
              <p className="text-primary-foreground/70 text-xs">Créditos</p>
              <p className="text-sm font-semibold">
                {showBalance ? formatCurrency(monthStats.credits) : '••••'}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-3 bg-primary-foreground/10 rounded-xl p-3">
            <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-destructive/20">
              <TrendingDown className="w-5 h-5 text-destructive" />
            </div>
            <div>
              <p className="text-primary-foreground/70 text-xs">Débitos</p>
              <p className="text-sm font-semibold">
                {showBalance ? formatCurrency(monthStats.debits) : '••••'}
              </p>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
