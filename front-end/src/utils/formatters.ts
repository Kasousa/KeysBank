// Currency formatting for Brazilian Real
export function formatCurrency(value: number): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
}

// Parse currency string to number
export function parseCurrency(value: string): number {
  const cleaned = value.replace(/[^\d,]/g, '').replace(',', '.');
  return parseFloat(cleaned) || 0;
}

// Format date for display (DD/MM/YYYY)
export function formatDate(dateString: string | undefined): string {
  if (!dateString) return 'Data inválida';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Data inválida';
    return date.toLocaleDateString('pt-BR');
  } catch (error) {
    return 'Data inválida';
  }
}

// Format date for API (YYYY-MM-DD)
export function formatDateForApi(date: Date): string {
  return date.toISOString().split('T')[0];
}

// Format account number with mask
export function formatAccountNumber(agency: string, accountNumber: string): string {
  return `${agency} / ${accountNumber}`;
}

// Mask currency input
export function maskCurrency(value: string): string {
  const numericValue = value.replace(/\D/g, '');
  const numberValue = parseInt(numericValue, 10) / 100;
  
  if (isNaN(numberValue)) return 'R$ 0,00';
  
  return formatCurrency(numberValue);
}

// Get numeric value from masked currency
export function getNumericValue(maskedValue: string): number {
  const numericString = maskedValue.replace(/[^\d,]/g, '').replace(',', '.');
  return parseFloat(numericString) || 0;
}

// Format category (remove uppercase, replace _ with space, capitalize first letter of each word)
export function formatCategory(category: string): string {
  return category
    .replace(/_/g, ' ')
    .toLowerCase()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}
