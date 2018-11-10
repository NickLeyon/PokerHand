import java.util.*;

public class Deck
{
	public Card[] deck = new Card[52];
	
	public Deck(Card[] deck){
		this.deck = deck;
		if(!checkDeck()) {
			System.out.println("Abort BAD DECK");
			System.exit(0);
		}
	}
	
	public Deck(int shuffles){
		this.deck = initDeck();
		shuffle(shuffles);
		if(!checkDeck()) {
			System.out.println("Abort BAD DECK");
			System.exit(0);
		}
	}
	
	public Deck(){
		this.deck = initDeck();
		if(!checkDeck()) {
			System.out.println("Abort BAD DECK");
			System.exit(0);
		}
	}
	
	public Card[] initDeck(){
		Card[] d = new Card[52];
		Card c = null;
		int i = 0;
		for(int s = 0; s < 4; s++){
			for(int r = 2; r < 15; r++){
				c = new Card(s, r);
				d[i] = c;
				i++;
			}
		}
		return d;
	}
	
	public void shuffle(int shuf){
		Random rng = new Random();
		Card temp = null;
		for(int x = 0; x < shuf; x++){
			for(int i = 0; i < this.deck.length; i++){
				int r = rng.nextInt(52);
				temp = this.deck[i];
				this.deck[i] = this.deck[r];
				this.deck[r] = temp;
			}
		}
		if(!checkDeck()) {
			System.out.println("Abort BAD SHUFFLE");
			System.exit(0);
		}
	}
	
	public float evalHand(Card p1, Card p2, Card f1, Card f2, Card f3, Card t, Card r){
		float score = 0;	//Hold highest score
		float check = 0;	//Check every combination
		Card[] cards = new Card[]{f1, f2, f3, t, r};	//Cards evaluated
		
		for(int i = 0; i < 21; i++){
			if(i == 0){
				score = eval5(cards);
			} else if (i >= 1 && i <= 5){
				cards = new Card[]{f1, f2, f3, t, r};
				cards[i - 1] = p1;
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			} else if (i >= 6 && i <= 10){
				cards = new Card[]{f1, f2, f3, t, r};
				cards[i - 6] = p2;
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			} else if (i >= 11 && i <= 14){
				cards = new Card[]{f1, f2, f3, t, r};
				cards[0] = p1;
				cards[i - 10] = p2;
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			} else if (i >= 15 && i <= 17){
				cards = new Card[]{f1, f2, f3, t, r};
				cards[1] = p1;
				cards[i - 13] = p2;
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			} else if (i == 18 || i == 19){
				cards = new Card[]{f1, f2, f3, t, r};
				cards[2] = p1;
				cards[i - 15] = p2;
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			} else if (i == 20){
				cards = new Card[]{f1, f2, f3, p1, p2};
				check = eval5(cards);
				if(check > score){
					score = check;
				}
			}
		}
		
		return score;	
	}
	
	public float eval5(Card[] cards){
		float score = 0;
		boolean pairFlag = false, tpFlag = false, tripFlag = false, fullhouseFlag = false, quadFlag = false;
		int pairString = 1;
		
		cards = sort(cards);	//From lowest to highest.
		
		
		//Find pairs
		for(int i = 0; i < 4; i++){
			if(cards[i].rank == cards[i+1].rank){
				pairString++;
			} else {
				pairString = 1;
			}
			switch (pairString){		/*Don't touch or unforeseen consequences will happen.*/
				case 1:
					break;
				case 2:
					if(tripFlag){
						fullhouseFlag = true;
						tripFlag = false;
						break;
					}
					if(!pairFlag){
						pairFlag = true;
					} else {
						tpFlag = true;
						pairFlag = false;
					}
					break;
				case 3:
					if(pairFlag){
						tripFlag = true;
						pairFlag = false;
					} else if(tpFlag){
						fullhouseFlag = true;
						tpFlag = false;
					}
					break;
				case 4:
					if(tripFlag){
						tripFlag = false;
						quadFlag = true;
					}
					break;
			}							/**/
		}
		
	
		//Straight Flush
		int sfFlag = 1;
		boolean aceFlag = false;	//To check if ace should be 1 for a straight.
		
		if(cards[0].rank == 2 && cards[4].rank == 14){
			aceFlag = true;
			cards[4].rank = 1;
			cards = sort(cards);
		}
		for(int i = 0; i < 4; i++){
			if(cards[i].rank == cards[i+1].rank - 1 && cards[i].suit == cards[i+1].suit){
				sfFlag++;
				score += (float) cards[i].rank * 10000000000L;
			} else {
				score = 0;	//Score manually reset if no pairFlags are needed.
				i = 4;
			}

			if(sfFlag == 5){
				score += (float) cards[i+1].rank * 10000000000L;
				//System.out.println("\nStraight Flush\n");
				return score;
			}
		}
		
		if(aceFlag){	//Revert ace from 1 back to 14
			cards[0].rank = 14;
			cards = sort(cards);
			aceFlag = false;
		}
		
		
		//Quad
		if(quadFlag){
			int qScore = 0;
			if(cards[0].rank == cards[1].rank) {
				qScore = cards[4].rank * 100;
			}else {
				qScore = cards[0].rank * 100;
			}
			//System.out.println("\nQuads\n");
			return qScore + (float) cards[2].rank * 100000000;
		}
		
		//Full House
		if(fullhouseFlag){
			boolean pairFirst = false;
			if(cards[1].rank != cards[2].rank){
				pairFirst = true;
			}
			
			if(pairFirst){
				score = (float) cards[4].rank * 1000000;
				//System.out.println("\nFull House\n");
				return score + (float) cards[0].rank;
			} else {
				score = (float) cards[0].rank * 1000000;
				//System.out.println("\nFull House\n");
				return score + (float) cards[4].rank;
			}
		}
		
		
		//Flush
		int flushFlag = 1, flushPlier = 1;	

		for(int i = 0; i < 4; i++){
			if(cards[i].suit == cards[i+1].suit){
				flushFlag++;
				score += (float) cards[i].rank * 10000 * flushPlier;	//If card's rank went past 14 this wouldn't work.
				flushPlier *= 10;		//More points for later appearing cards.
			} else {
				score = 0;
				i = 4;
			}
			
			if(flushFlag == 5){		//Fix for last card in array
				score += (float) cards[i+1].rank * 10000 * flushPlier;
				//System.out.println("\nFlush\n");
				return score / 100000;	//To account for large score inflation
			}
		}
		
		
		//Straight
		int straightFlag = 1;
		
		if(cards[0].rank == 2 && cards[4].rank == 14){
			aceFlag = true;
			cards[4].rank = 1;
			cards = sort(cards);
		}
		for(int i = 0; i < 4; i++){
			if(cards[i].rank == cards[i+1].rank - 1){
				straightFlag++;
				score += (float) cards[i].rank * 100;
			} else {
				score = 0;
				i = 4;
			}
			
			if(straightFlag == 5){		//Fix for last card 
				score += (float) cards[i+1].rank * 100;
				//System.out.println("\nStraight\n");
				return score;
			}
		}
		
		if(aceFlag){		//Revert ace from 1 back to 14
			cards[0].rank = 14;
			cards = sort(cards);
			aceFlag = false;	//'aceFlag' isn't used again so this line isn't necessary.
		}
		
		
		//Three of a Kind
		if(tripFlag){
			int tripStart = 0;
			for(int i = 0; i < 4; i++){		//Find start of trips
				if(cards[i].rank != cards[i+1].rank){
					tripStart++;
				} else {
					i = 4;
				}
			}
			
			int tripPlier = 100;		//Less points for earlier appearing high cards.
			for(int i = 0; i < 5; i++){
				if(i == tripStart){
					for(int x = 0; x < 3; x++){
						score += (float) cards[i].rank;
						i++;	//Increment parent index inside child loop.
					}
					if(i > 4) {	//In case the trips are at the end.
						break;    //exit
					}
				}
				score += (float) cards[i].rank / (100 * tripPlier);
				tripPlier /= 100;
			}
			//System.out.println("\nTrips\n");
			return score;
		}

		//Two Pair
		if(tpFlag){
			int firstPair = 0, secondPair = 0;	//Keep track of where each pair starts
			boolean pairSwitch = false;
			for(int i = 0; i < 4; i++){
				if(cards[i].rank == cards[i+1].rank && !pairSwitch){
					firstPair = i;
					i += 2;
					pairSwitch = true;
				}
				if(cards[i].rank == cards[i+1].rank && pairSwitch){
					secondPair = i;
					i = 4;	//Exit loop
				}
			}
			
			for(int i = 0; i < 5; i++){
				if(i == firstPair){
					for(int x = 0; x < 2; x++){
						score += (float) cards[i].rank / 10000;
						i += (1 - x);
					}
				} else if(i == secondPair){
					for(int x = 0; x < 2; x++){
						score += (float) cards[i].rank / 100;
						i += (1 - x);
					}
				} else {
					score += (float) cards[i].rank / 1000000;
				}
			}
			//System.out.println("\nTwo Pair\n");
			return score;
		} 
		
		//Pair
		if(pairFlag){
			int pairStart = 0;
			for(int i = 0; i < 4; i++){
				if(cards[i].rank == cards[i+1].rank){
					pairStart = i;
					i = 4;
				}
			}
			
			int pPlier = 10000; 	//Less points for earlier appearing cards.
			for(int i = 0; i < 5; i++){
				if(i == pairStart){		//Account for the pair
					score += 2 * ((float)cards[i].rank/10000);
					i += 2;
					if (i > 4){	//To avoid the final addition to 'score'.
						break;
					}
				}
					score += (float) cards[i].rank / (1000000 * pPlier);	//Add high cards
					pPlier /= 100;
			}
			
			//System.out.println("\nPair\n");
			return score;
		}
		
		
		//High Card
		int hcPlier = 10000;	//Less points at the start
		for(int i = 0; i < 5; i++){
			score += (float) cards[i].rank / (1000000 * hcPlier);
			hcPlier /= 10;	//10 would be higher if cards could go up to like 20
		}
		//System.out.println("\nHigh Card\n");
		return score;
	}
	
	public Card[] sort(Card[] cards){	//Sorts from lowest to highest
		Card[] result = cards;
		
		Card temp = null;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4 - i; j++){
				if(result[j].rank > result[j+1].rank){
					temp = cards[j];
					result[j] = result[j+1];
					result[j+1] = temp;
				}
			}
		}
		return result;
	}
	public boolean checkDeck() {	//Doesn't matter if shuffled.
		if(this.deck.length != 52) {	//Cut it short if there isn't exactly 52.
			return false;
		}
		
		Card[] mold = new Card[52];	//Mold copy of deck for checking.
		mold = this.deck;
		ArrayList<Card> cards = new ArrayList<Card>();	//Create a list of each card checked.
		

		int xS = 0, xC = 0, xH = 0, xD = 0;	//Count suits
		for(int i = 0; i < mold.length; i++) {
			Card mB = mold[i];	//[moldBit] Replace mold[i] in loop
			if (mB.rank < 2 || mB.rank > 14) {	//Make sure rank isn't out of bounds.
				return false;
			}
			for(Card c : cards) {
				if(c.suit == mB.suit && c.rank == mB.rank) {	//Check for same cards
					return false;
				} 
			}
					cards.add(mB);
					switch (mB.suit) {
						case 0: xS++;
								break;
						case 1: xC++;
								break;
						case 2: xH++;
								break;
						case 3: xD++;
								break;
						default:	return false;	//Should only be 0-3
					}
		}
		
		if(xS != 13 || xC != 13 || xH != 13 || xD != 13) {	//Final 52 suited check.
			return false;
		}
		return true;
	}
	
	public Card[] getDeck(){
		return this.deck;
	}
}
