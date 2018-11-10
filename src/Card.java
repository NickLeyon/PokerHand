
public class Card {
	public int suit, rank;
	
	public Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public String outputCard() {
		//Make suit into a string
		String sSuit = "";
		switch (this.suit) {
			case 0: sSuit = "Spades";
					break;
			case 1: sSuit = "Clubs";
					break;
			case 2: sSuit = "Hearts";
					break;
			case 3: sSuit = "Diamonds";
					break;
		}
		
		//Make rank into a string
		String sRank = "";
		switch (this.rank) {
			case 11: sRank = "Jack";
					break;
			case 12: sRank = "Queen";
					break;
			case 13: sRank = "King";
					break;
			case 14: sRank = "Ace";
					break;
			default: sRank = String.valueOf(this.rank);
					break;
		}
		return sRank + " of " + sSuit;
	}
	
	public String outputCardShort() {
		//Make suit into a string
				String sSuit = "";
				switch (this.suit) {
					case 0: sSuit = "s";
							break;
					case 1: sSuit = "c";
							break;
					case 2: sSuit = "h";
							break;
					case 3: sSuit = "d";
							break;
				}
				
				//Make rank into a string
				String sRank = "";
				switch (this.rank) {
					case 11: sRank = "J";
							break;
					case 12: sRank = "Q";
							break;
					case 13: sRank = "K";
							break;
					case 14: sRank = "A";
							break;
					default: sRank = String.valueOf(this.rank);
							break;
				}
				return sRank + sSuit;
	}

}