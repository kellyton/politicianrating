package models.util;

public class WordFrequency implements Comparable<WordFrequency>{
	public String word;
	public long occurrences;
	
	/**
	 * Return number of ocurrences between 0 and 100
	 * @return
	 */
	public long getOcurrences(){
		return occurrences * 10;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof WordFrequency){
			WordFrequency other = (WordFrequency)o; 
			
			boolean equals = this.word.equals(other.word);
			
			System.out.println("Este = " + this.word + ", outro = " + other.word + ", resultado = " + equals);
			
			return equals; 
		} else {
			return false;
		}
	}
	
	@Override
	public int compareTo(WordFrequency o) {
		if (this.occurrences == o.occurrences){
			return 0;
		} else {
			if (this.occurrences > o.occurrences){
				return -1;
			} else {
				return 1;
			}
		}
	}
	@Override
	public String toString(){
		return word + ":"+occurrences;
	}
}
