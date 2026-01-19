import { useState, useCallback } from 'react';
import { Header } from '@/components/layout/Header';
import { BalanceCard } from '@/components/dashboard/BalanceCard';
import { StatementTab } from '@/components/dashboard/StatementTab';
import { TransactionTab } from '@/components/dashboard/TransactionTab';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { FileText, Plus } from 'lucide-react';

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState('statement');
  const [refreshKey, setRefreshKey] = useState(0);

  const handleTransactionCreated = useCallback(() => {
    setRefreshKey((prev) => prev + 1);
    setActiveTab('statement');
  }, []);

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-6">
        <div className="max-w-3xl mx-auto space-y-6">
          <div className="animate-fade-in">
            <BalanceCard key={refreshKey} />
          </div>

          <Tabs value={activeTab} onValueChange={setActiveTab} className="animate-slide-up">
            <TabsList className="grid w-full grid-cols-2 bg-muted/50">
              <TabsTrigger
                value="statement"
                className="flex items-center gap-2 data-[state=active]:bg-card data-[state=active]:shadow-soft"
              >
                <FileText className="w-4 h-4" />
                Extrato
              </TabsTrigger>
              <TabsTrigger
                value="transaction"
                className="flex items-center gap-2 data-[state=active]:bg-card data-[state=active]:shadow-soft"
              >
                <Plus className="w-4 h-4" />
                Nova Transação
              </TabsTrigger>
            </TabsList>

            <TabsContent value="statement" className="mt-4">
              <StatementTab key={refreshKey} />
            </TabsContent>

            <TabsContent value="transaction" className="mt-4">
              <TransactionTab onTransactionCreated={handleTransactionCreated} />
            </TabsContent>
          </Tabs>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
