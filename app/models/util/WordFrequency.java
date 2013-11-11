/* Copyright 2013 de Kellyton Brito. Este arquivo é parte 
* do programa MeuCongressoNacional.com . O MeuCongressoNacional.com 
* é um software livre; você pode redistribuí-lo e/ou modificá-lo 
* dentro dos termos da GNU Affero General Public License como 
* publicada pela Fundação do Software Livre (FSF) na versão 3 
* da Licença. Este programa é distribuído na esperança que possa 
* ser útil, mas SEM NENHUMA GARANTIA; sem uma garantia implícita 
* de ADEQUAÇÃO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja 
* a licença para maiores detalhes, disponível em 
* meucongressonacional.com/license. Você deve ter recebido uma cópia 
* da GNU Affero General Public License, sob o título "LICENCA.txt", 
* junto com este programa, se não, acesse http://www.gnu.org/licenses/
**/

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
