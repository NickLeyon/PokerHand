import java.util.InputMismatchException;
import java.util.Scanner;

public class main {
	static Scanner scan = new Scanner(System.in);
	static Card cards[] = new Card[9]; //Array to hold 2 different hands (4 cards) and the 5 board cards.
	
	public static void main(String[] args) throws InterruptedException {
		Deck d = null;
		boolean loop = true; //For looping the evaluator.
		String loopChoice = "";
		
		int choice =-1;
		String sChoice = ""; //String to check the whole line when selecting [choice].
		System.out.print("Welcome to my poker hand evaluator! Would you like to randomly generate cards [0] or enter your own [1]: ");
		do {	//Loop to ensure correct input is entered.
			try {
				sChoice = scan.nextLine();
				if (Integer.parseInt(sChoice) != 0 && Integer.parseInt(sChoice) != 1) {
			        System.out.print("Invalid input. Please enter 0 for randomly generated cards or 1 to enter your own: ");
			    }
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter 0 for randomly generated cards or 1 to enter your own: ");
				sChoice = "-1";		//To avoid an error when checking in the while loop.
			}
		} while (Integer.parseInt(sChoice) != 0 && Integer.parseInt(sChoice) != 1);
		choice = Integer.parseInt(sChoice);
		
		while(loop) {
			if(choice == 1) {
				d = new Deck();
				System.out.println("\nEnter a card by typing the specified letter corresponding to your desired suit,"
						+ " followed directly by the rank.\n\nA - Spade\nS - Club\nD - Heart\nF - Diamond\n1-13 for rank, 1 is Ace.\n"
							+ "Example: D12 is Queen of Hearts!");
			
				System.out.print("\nSo would you like Quick Input [0] or Guided [1]: ");
				int inputChoice = -1;	
				do {	//Loop to ensure correct input is entered.
					try {
						inputChoice = scan.nextInt();
						if (inputChoice != 0 && inputChoice != 1) {
					        System.out.print("Invalid input. Please enter 0 for Quick Input or 1 for Guided: ");
					    }
					} catch (InputMismatchException e) {
						System.out.print("Invalid input. Please enter 0 for Quick Input or 1 for Guided: ");
						scan.nextLine();
					}
				} while (inputChoice != 0 && inputChoice != 1);
				scan.nextLine();
				
				Card ph = null; //PlaceHolder Card
				if(inputChoice == 1) {			/*Guided*/
					String message = ""; //Variable for the changing prompt.
					System.out.println();
					for(int i = 0; i < cards.length; i++) {
						switch (i) {
						case 0: message = "Enter your 1st card: ";
								break;
						case 1: message = "Enter your 2nd card: ";
								break;
						case 2: message = "Enter your rival's 1st card: ";
								break;
						case 3: message = "Enter your rival's 2nd card: ";
								break;
						case 4: message = "Enter the 1st community card: ";
								break;
						case 5: message = "Enter the 2nd community card: ";
								break;
						case 6: message = "Enter the 3rd community card: ";
								break;
						case 7: message = "Enter the 4th community card: ";
								break;
						case 8: message = "Enter the 5th and last community card: ";
								break;
						}
						do {
							System.out.print(message);
							ph = interpretCard(scan.nextLine());
						} while (cardExists(ph, i));
						cards[i] = ph;
						System.out.print(ph.outputCard() + " added!\n\n");
					}
				} else if (inputChoice == 0) {	/*Quick Input*/
					System.out.println("\nEnter your 1st card and 2nd card, the rival's 1st card and 2nd card, and the 5 community cards. In that order. (Press enter "
							+ "after each card)\n");
					for(int i = 0; i < cards.length; i++) {
						if(i == cards.length - 1) {
							System.out.print("Last card: ");
						}
						do {
							ph = interpretCard(scan.nextLine());
							cards[i] = ph;
						} while(cardExists(ph, i));
						System.out.print(ph.outputCard() + " added!\n\n");
					}
				} else {						//In case something goes wrong beforehand.
					System.out.println("UNEXPECTED ERROR for inputChoice!");
					System.exit(0);
				}
			} else {
				d = new Deck(15);
				for(int i = 0; i < cards.length; i++) {
					cards[i] = d.getDeck()[i + i];
				}
			}
			System.out.println("\nYour hand:    " + cards[0].outputCardShort() + " " + cards[1].outputCardShort() + "\nRival's hand: " + cards[2].outputCardShort() + " " + cards[3].outputCardShort()
					+ "\nBoard cards:  " + cards[4].outputCardShort() + " " + cards[5].outputCardShort() + " " + cards[6].outputCardShort() + " "+ cards[7].outputCardShort()
						+ " " + cards[8].outputCardShort() + "\n");

			double score1 = d.evalHand(cards[0], cards[1], cards[4], cards[5], cards[6], cards[7], cards[8]);
			double score2 = d.evalHand(cards[2], cards[3], cards[4], cards[5], cards[6], cards[7], cards[8]);
			
			if(score1 > score2) {
				System.out.println("\nYou win!");
			} else if(score1 < score2) {
				System.out.println("\nRival wins!");
			} else {
				System.out.println("\nIt's a tie!");
			}
			
			System.out.print("\nAgain? [Y/N]: ");
			do {	//Loop to ensure correct input is entered.
				do {
					loopChoice = scan.nextLine().toUpperCase();
				} while (loopChoice.length() == 0);	//Wait until input is entered.
					if (loopChoice.charAt(0) != 'Y' && loopChoice.charAt(0) != 'N' || loopChoice.length() != 1) {
				        System.out.print("Invalid input. Please enter Y or N: ");
				    }
			} while (loopChoice.charAt(0) != 'Y' && loopChoice.charAt(0) != 'N' || loopChoice.length() != 1);
			if(loopChoice.equals("N")) {
				loop = false;
			}
			System.out.println("\n");
		}
		scan.close();	//Done with any and all input.
		System.out.println("Goodbye!");
		Thread.sleep(3333);
		System.exit(0);
	}
	
	public static Card interpretCard(String c) {
		int suit = -1, rank = -1;
		
		int hintCounter = 0;	//Display a hint for card entry after 3 incorrect attempts.
		while(!checkCard(c)) {	//Check if input is a valid card
			hintCounter++;
			if(hintCounter >= 3) {
				System.out.println("Invalid card entry! Please enter a card by typing the specified letter corresponding to your desired suit,"
						+ " followed directly by the rank.\n\nA - Spade\nS - Club\nD - Heart\nF - Diamond\n1-13 for rank, 1 is Ace.\n"
							+ "Example: S8 is 8 of Clubs!");
				System.out.print("\nInput Card: ");
				
			} else {
				System.out.print("\nInvalid input for card entry, please try again: ");
			}
			c = scan.nextLine();
		}
		
		/*Parse a Card from the inputed string*/
		//Parse the suit
		switch (Character.toUpperCase(c.charAt(0))) {
			case 'A': suit = 0;
					break;
			case 'S': suit = 1;
					break;
			case 'D': suit = 2;
					break;
			case 'F': suit = 3;
					break;
		}
		//Parse the rank
		String sRank = "";
		for(int i = 1; i < c.length(); i++) {
			sRank += c.charAt(i);
		}
		if (rank == 1) {	//Ace is actually 14 and not 1.
			rank = 14;
		}
		rank = Integer.parseInt(sRank);
		
		return new Card(suit, rank);
	}
	
	public static boolean checkCard(String c) {
		//Check for valid length
		if (c.length() < 2 || c.length() > 3) {
			return false;
		}
		//Check for valid suit
		char j = Character.toUpperCase(c.charAt(0));
		if (j != 'A' && j != 'S' && j != 'D' && j != 'F') {
			return false;
		}
		//Check for valid rank
		String sRank = "";		//Parse rank from string
		for(int i = 1; i < c.length(); i++) {
			sRank += c.charAt(i);
		}
		int rank = -1;	//Initialized to avoid errors.
		try {
			rank = Integer.parseInt(sRank);
		} catch (NumberFormatException e) {
			rank = -1;
		}
		if(rank < 1 || rank > 13) {
			return false;
		}
		
		return true;
	}
	
	public static boolean cardExists(Card c, int selfIndex) {
		Card check = null;
		for(int i = 0; i < cards.length; i++) {	//Loop through whole array to check for an existing identical card.
			check = cards[i];
			if(check != null && i != selfIndex) {	//Only do a check if the spot in the array has a card.
				if(check.suit == c.suit && check.rank == c.rank) {
					System.out.print("\n" + c.outputCard() + " already exists! Please try again: ");
					return true;
				}
			}
		}

		return false;
	}

}
