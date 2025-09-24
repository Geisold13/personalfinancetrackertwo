export interface Transaction {
    transactionID: number | null;
    transactionType: string;
    transactionName: string;
    transactionAmount: number;
    transactionDate: string;
    transactionCategory: string;
    transactionDescription: string;
}
