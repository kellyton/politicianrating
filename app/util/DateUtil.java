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

package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	public static String getFormattedDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}

	/**
	 * Parse from dd/MM/yyyy
	 * @param str
	 * @return
	 */
	public static Date parseToDate(String str) {
		
		TimeZone GMT = TimeZone.getTimeZone("GMT-03:00");
		
		try {
			int day = Integer.parseInt(str.substring(0, 2));
			int month = Integer.parseInt(str.substring(3, 5)) - 1;
			int year = Integer.parseInt(str.substring(6, 10));
			
			Calendar dateTime = new GregorianCalendar(GMT);
			dateTime.set(year, month, day);
			return dateTime.getTime();

		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

}
