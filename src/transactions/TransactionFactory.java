package transactions;

import java.util.ArrayList;

public class TransactionFactory {

    /**
     * Return a Transaction object specified by the given arraylist where the first element is the transaction code
     * matching with one of the Transaction implementations
     *
     * @param attr ArrayList<String> of attributes for the Transaction being built
     * @return Transaction object
     */
    public Transaction buildTransaction(ArrayList<String> attr) {
        String transactionCode = attr.get(0);
        String attr1 = attr.get(1);
        String attr2 = attr.get(2);
        String attr3 = attr.get(3);

        Transaction transac;
        switch(transactionCode) {
            // Login 00-Username-Type-Funds
            case "00":
                transac = new Login(attr1, attr2, attr3);
                break;

            // Create 01-Username-Type-Funds
            case "01":
                transac = new Create(attr1, attr2, attr3);
                break;

            // Delete 02-Username-Type-Funds
            case "02":
                transac = new Delete(attr1, attr2, attr3);
                break;

            // Sell 03-GameName-Seller-Discount-SalePrice
            case "03":
                transac = new Sell(attr1, attr2, attr3, attr.get(4));
                break;

            // Buy 04-GameName-Seller-Buyer
            case "04":
                transac = new Buy(attr1, attr2, attr3);
                break;

            // Refund 05-Buyer-Seller-Credit
            case "05":
                transac = new Refund(attr1, attr2, attr3);
                break;

            // AddCredit 06-Username-Type-Funds
            case "06":
                transac = new AddCredit(attr1, attr2, attr3);
                break;

            // AuctionSale 07-Username-Type-Funds
            case "07":
                transac = new AuctionSale(attr1, attr2, attr3);
                break;

            // RemoveGame 08-GameName-OwnerName
            case "08":
                transac = new RemoveGame(attr1, attr2);
                break;

            // Gift 09-GameName-OwnerName-ReceiverName
            case "09":
                transac = new Gift(attr1, attr2, attr3);
                break;

            // Logout 10-Username-Type-Funds
            case "10":
                transac = new Logout(attr1, attr2, attr3);
                break;

            // If none of the codes match this is a FATAL ERROR!!!
            default:
                transac = new Error();
        }

        return transac;
    }
}
