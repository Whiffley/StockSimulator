//This class is a simulation of buying and selling stocks. It prompts the user to either
//buy stocks, sell stocks, save their portfolio, or quit the program. Then, it returns the
//user's total stock portfolio value.
import java.util.*;
import java.io.*;

public class Stocks {
    public static final String STOCKS_FILE_NAME = "stonks.tsv";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner fileScan = new Scanner(new File(STOCKS_FILE_NAME));

        int numStocks = Integer.parseInt(fileScan.nextLine());
        String[] stocks = new String[numStocks];
        double[] price = new double[numStocks];
        double[] portfolio = new double[numStocks];

        loadFile(fileScan, stocks, price);

        System.out.println("Welcome to the CSE 122 Stocks Simulator!");
        System.out.println("There are " + numStocks + " stocks on the market:");

        for(int i = 0; i < numStocks; i++) {
            System.out.println(stocks[i] + ": " + price[i]);
        }

        String choice = "";
        while (!choice.equalsIgnoreCase("Q")) {
            System.out.println();
            System.out.println("Menu: (B)uy, (Se)ll, (S)ave, (Q)uit");
            
            System.out.print("Enter your choice: ");
            choice = console.nextLine();

            if (choice.equalsIgnoreCase("B")) {
                System.out.print("Enter the stock ticker: ");
                String stockName = console.nextLine();
                
                System.out.print("Enter your budget: ");
                double budget = Double.parseDouble(console.nextLine());
                
                if (budget >= 5.0) {
                    buyStocks(stocks, price, portfolio, stockName, budget);
                    System.out.println("You successfully bought " + stockName + ".");
                } else {
                    System.out.println("Budget must be at least $5");
                }

            } else if (choice.equalsIgnoreCase("Se")) {
                System.out.print("Enter the stock ticker: ");
                String stockName = console.nextLine();
                
                System.out.print("Enter the number of shares to sell: ");  
                double soldShares = Double.parseDouble(console.nextLine());
                double numShares = getNumShares(stocks, portfolio, stockName);
                
                if (numShares >= soldShares) {
                    sellStocks(stocks, price, portfolio, stockName, soldShares);
                    System.out.println("You successfully sold " + soldShares + 
                                            " shares of " + stockName + ".");
                } else {
                    System.out.println("You do not have enough shares of " + stockName
                                            + " to sell " + soldShares + " shares.");
                }

            } else if (choice.equalsIgnoreCase("S")) {
                System.out.print("Enter new portfolio file name: ");
                String fileName = console.nextLine();

                save(stocks, portfolio, fileName);

            } else if (!choice.equalsIgnoreCase("Q")) {
                System.out.println("Invalid choice: " + choice);
                System.out.println("Please try again");
            }
        }

        double portfolioValue = findPortfolioValue(price, portfolio);
        System.out.println("Your portfolio is currently valued at: $" + portfolioValue);

    }


    //Behavior:
    //  - loads the stock names and prices into arrays
    //Parameters: 
    //  - fileScan: scans the file
    //  - stocks: array that holds the stock names
    //  - price: array that holds the prices of the stocks
    public static void loadFile(Scanner fileScan, String[] stocks, double[] price) {
        int index = 0;
        fileScan.nextLine();

        while(fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            Scanner lineScan = new Scanner(line);
            
            stocks[index] = lineScan.next();
            price[index] = lineScan.nextDouble();

            index++;
        }
    }


    //Behavior:
    //  - finds the index of the inputted stock name in the stocks array
    //Parameters:
    //  - stocks: array that holds the stock names
    //  - stockName: represents the stock name inputted by the user
    //Returns:
    // int: returns the index of the inputted stock name in the stocks array
    public static int indexOf(String[] stocks, String stockName) {
        int index = -1;
        for (int i = 0; i < stocks.length; i++) {
            if (stocks[i].equals(stockName)) {
                index = i;
            }
        }
        return index; 
    }


    //Behavior:
    //  - calculates the number of shares the user has based on their budget
    //Parameters:
    //  - stocks: array that holds the stock names
    //  - price: array that holds the prices of the stocks
    //  - portfolio: array that holds the number of stocks owned by the user
    //  - stockName: represents the stock name inputted by the user
    //  - budget: represents the budget inputted by the user
    public static void buyStocks(String[] stocks, double[] price, 
                double[] portfolio, String stockName, double budget){
        int index = indexOf(stocks, stockName);
        double numShares = budget / price[index];
        portfolio[index] = portfolio[index] + numShares;
    }


    //Behavior:
    //  - calculates the number of shares the user has after they sell stocks
    //Parameters:
    //  - stocks: array that holds the stock names
    //  - price: array that holds the prices of the stocks
    //  - portfolio: array that holds the number of stocks owned by the user
    //  - stockName: represents the stock name inputted by the user
    //  - soldShares: represents the shares the user intends to sell 
    public static void sellStocks(String[] stocks, double[] price, 
            double[] portfolio, String stockName, double soldShares){
        int index = indexOf(stocks, stockName);
        portfolio[index] = portfolio[index] - soldShares;
    }


    //Behavior:
    //  - outputs the user's owned stocks to a file
    //Exception:
    //  - FileNotFoundException: throws the failure of attempting to open the inputted file
    //Parameters:
    //  - stocks: array that holds the stock names
    //  - portfolio: array that holds the number of stocks owned by the user
    //  - fileName: represents the file inputted by the user
    public static void save(String[] stocks, double[] portfolio, String fileName) 
                                                    throws FileNotFoundException {        
        File outFile = new File(fileName);
        PrintStream out = new PrintStream(outFile);

        for (int i = 0; i < stocks.length; i++) {
            if (portfolio[i] > 0) {
                out.print(stocks[i]);
                out.print(" ");
                out.println(portfolio[i]);
            } 
        }
    }


    //Behavior:
    //  - Calculates the total value of the user's portfolio
    //Parameters:
    //  - price: array that holds the prices of the stocks
    //  - portfolio: array that holds the number of stocks owned by the user
    //Returns:
    //  - double: returns the value of the user's portfolio
    public static double findPortfolioValue(double[] price, double[] portfolio) {
        double portfolioValue = 0;
        for(int i = 0; i < price.length; i++) {
            double shareValue = price[i] * portfolio[i];
            portfolioValue = portfolioValue + shareValue;
        } 
        return portfolioValue;
    }


    //Behavior:
    //  - gets the number of shares the user has for a stock
    //Parameters:
    //  - stocks: array that holds the stock names
    //  - portfolio: array that holds the number of stocks owned by the user
    //  - stockName: represents the stock name inputted by the user
    //Returns:
    //  - double: returns the number of shares the user has for a stock
    public static double getNumShares(String[] stocks, double[] portfolio, String stockName) {
        int index = indexOf(stocks, stockName);
        double numShares = portfolio[index];
        return numShares;
    }

    
}
