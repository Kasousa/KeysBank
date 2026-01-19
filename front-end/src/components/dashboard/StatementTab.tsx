import { useState, useEffect } from 'react';
import { Calendar, Filter, X, ArrowUpRight, ArrowDownRight, Scale, FileText } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { formatCurrency, formatDate, formatCategory } from '@/utils/formatters';
import { getStatement } from '@/services/api';
import { useAuth } from '@/context/AuthContext';
import type { Statement, StatementFilters } from '@/types';

export function StatementTab() {
  const { accountId } = useAuth();
  const [statements, setStatements] = useState<Statement[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [filters, setFilters] = useState<StatementFilters>({
    startDate: '',
    endDate: '',
    type: '',
  });
  const [showFilters, setShowFilters] = useState(false);

  const fetchStatements = async () => {
    if (!accountId) return;
    setIsLoading(true);
    try {
      const data = await getStatement(accountId, filters);
      // Filter out BALANCE entries for display and sort by date descending
      const filtered = data.filter(s => s.type !== 'BALANCE');
      const sorted = filtered.sort((a, b) => 
        new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
      );
      setStatements(sorted);
    } catch (error) {
      console.error('Failed to fetch statements:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchStatements();
  }, [accountId]);

  const handleApplyFilters = () => {
    fetchStatements();
    setShowFilters(false);
  };

  const handleClearFilters = () => {
    setFilters({ startDate: '', endDate: '', type: '' });
    setTimeout(fetchStatements, 0);
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'CREDIT':
        return <ArrowDownRight className="w-4 h-4" />;
      case 'DEBIT':
        return <ArrowUpRight className="w-4 h-4" />;
      default:
        return <Scale className="w-4 h-4" />;
    }
  };

  const getTypeBadge = (type: string) => {
    switch (type) {
      case 'CREDIT':
        return (
          <Badge className="bg-success/10 text-success border-0 hover:bg-success/20">
            {getTypeIcon(type)}
            <span className="ml-1">Crédito</span>
          </Badge>
        );
      case 'DEBIT':
        return (
          <Badge className="bg-destructive/10 text-destructive border-0 hover:bg-destructive/20">
            {getTypeIcon(type)}
            <span className="ml-1">Débito</span>
          </Badge>
        );
      default:
        return (
          <Badge variant="secondary">
            {getTypeIcon(type)}
            <span className="ml-1">Saldo</span>
          </Badge>
        );
    }
  };

  const hasActiveFilters = filters.startDate || filters.endDate || filters.type;

  return (
    <Card className="shadow-card">
      <CardHeader className="pb-4">
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-semibold flex items-center gap-2">
            <FileText className="w-5 h-5 text-primary" />
            Extrato
          </CardTitle>
          <div className="flex items-center gap-2">
            {hasActiveFilters && (
              <Button
                variant="ghost"
                size="sm"
                onClick={handleClearFilters}
                className="text-muted-foreground"
              >
                <X className="w-4 h-4 mr-1" />
                Limpar
              </Button>
            )}
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowFilters(!showFilters)}
              className={showFilters ? 'bg-primary/10 border-primary' : ''}
            >
              <Filter className="w-4 h-4 mr-1" />
              Filtros
            </Button>
          </div>
        </div>

        {showFilters && (
          <div className="mt-4 p-4 bg-muted/50 rounded-xl animate-slide-up">
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="startDate" className="text-sm">Data Inicial</Label>
                <Input
                  id="startDate"
                  type="date"
                  value={filters.startDate}
                  onChange={(e) => setFilters({ ...filters, startDate: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="endDate" className="text-sm">Data Final</Label>
                <Input
                  id="endDate"
                  type="date"
                  value={filters.endDate}
                  onChange={(e) => setFilters({ ...filters, endDate: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="type" className="text-sm">Tipo</Label>
                <Select
                  value={filters.type || 'all'}
                  onValueChange={(value) => setFilters({ ...filters, type: value === 'all' ? '' : value as 'CREDIT' | 'DEBIT' })}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Todos" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Todos</SelectItem>
                    <SelectItem value="CREDIT">Crédito</SelectItem>
                    <SelectItem value="DEBIT">Débito</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
            <div className="mt-4 flex justify-end">
              <Button onClick={handleApplyFilters} size="sm">
                Aplicar Filtros
              </Button>
            </div>
          </div>
        )}
      </CardHeader>

      <CardContent>
        {isLoading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-16 bg-muted/50 rounded-lg animate-pulse" />
            ))}
          </div>
        ) : statements.length === 0 ? (
          <div className="text-center py-12">
            <FileText className="w-12 h-12 mx-auto text-muted-foreground/50 mb-4" />
            <p className="text-muted-foreground font-medium">Nenhuma transação encontrada</p>
            <p className="text-sm text-muted-foreground/70 mt-1">
              Suas transações aparecerão aqui
            </p>
          </div>
        ) : (
          <div className="space-y-2">
            {statements.map((statement, index) => (
              <div
                key={statement.id}
                className="flex items-center justify-between p-4 bg-muted/30 hover:bg-muted/50 rounded-xl transition-colors animate-fade-in"
                style={{ animationDelay: `${index * 50}ms` }}
              >
                <div className="flex items-center gap-4">
                  <div
                    className={`flex items-center justify-center w-10 h-10 rounded-xl ${
                      statement.type === 'CREDIT'
                        ? 'bg-success/10'
                        : 'bg-destructive/10'
                    }`}
                  >
                    {statement.type === 'CREDIT' ? (
                      <ArrowDownRight className="w-5 h-5 text-success" />
                    ) : (
                      <ArrowUpRight className="w-5 h-5 text-destructive" />
                    )}
                  </div>
                  <div>
                    <p className="text-base text-foreground">
                      <span className="font-bold">{formatDate(statement.createdAt)}</span> - {formatCategory(statement.category)}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {statement.description}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <p
                    className={`font-semibold ${
                      statement.type === 'CREDIT' ? 'text-success' : 'text-destructive'
                    }`}
                  >
                    {statement.type === 'CREDIT' ? '+' : '-'} {formatCurrency(statement.amount)}
                  </p>
                  {getTypeBadge(statement.type)}
                </div>
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
