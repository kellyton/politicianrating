package util;

import java.util.HashMap;

public class NamesMap {

	public static final int DEPUTADO = 0;
	public static final int SENADOR = 1;

	private static HashMap<String, String> namesDeputados = new HashMap<String, String>();
	private static HashMap<String, String> namesSenadores = new HashMap<String, String>();
	
	//initialization
	static {
		namesDeputados.put("ASSINATURA DE PUBLICAÇňES","Assinatura de publicações");
		namesDeputados.put("PASSAGENS TERRESTRES, MARÍTIMAS OU FLUVIAIS","Passagens Terrestes/Marítimas/Fluviais");
		namesDeputados.put("FORNECIMENTO DE ALIMENTAÇăO DO PARLAMENTAR","Alimentação");
		namesDeputados.put("DIVULGAÇăO DA ATIVIDADE PARLAMENTAR.","Divulgação");
		namesDeputados.put("SERVIÇO DE TÁXI, PEDÁGIO E ESTACIONAMENTO","Taxi/Pedágio/Estacionamento");
		namesDeputados.put("TELEFONIA","Telefonia");
		namesDeputados.put("EmissĂo Bilhete Aéreo","Passagens aéreas");
		namesDeputados.put("PASSAGENS AÉREAS","Passagens aéreas");
		
		namesDeputados.put("LOCAÇăO OU FRETAMENTO DE EMBARCAÇňES","Locação de veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇăO DE VEÍCULOS AUTOMOTORES OU FRETAMENTO DE EMBARCAÇňES ","Locação de veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇăO OU FRETAMENTO DE AERONAVES","Locação de veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇăO OU FRETAMENTO DE VEÍCULOS AUTOMOTORES","Locação de veículos/embarcações/aeronaves");
		
		namesDeputados.put("SERVIÇO DE SEGURANÇA PRESTADO POR EMPRESA ESPECIALIZADA.","Segurança");
		namesDeputados.put("MANUTENÇăO DE ESCRITÓRIO DE APOIO Ě ATIVIDADE PARLAMENTAR","Manutenção de escritório");
		namesDeputados.put("COMBUSTÍVEIS E LUBRIFICANTES.","Combustível");
		namesDeputados.put("CONSULTORIAS, PESQUISAS E TRABALHOS TÉCNICOS.","Consultorias e Pesquisas");		
		namesDeputados.put("HOSPEDAGEM ,EXCETO DO PARLAMENTAR NO DISTRITO FEDERAL.","Consultorias e Pesquisas");
		namesDeputados.put("SERVIÇOS POSTAIS","Serviços Postais");
		
		namesSenadores.put("Aluguel de imóveis para escritório político, compreendendo despesas concernentes a eles.","Imóveis");
		namesSenadores.put("Aquisição de material de consumo para uso no escritório político, inclusive aquisição ou locação de software, despesas postais, aquisição de publicações, locação de móveis e de equipamentos. ","Material de Consumo");
		namesSenadores.put("Contratação de consultorias, assessorias, pesquisas, trabalhos técnicos e outros serviços de apoio ao exercício do mandato parlamentar","Consultorias");
		namesSenadores.put("Divulgação da atividade parlamentar","Divulgação");
		namesSenadores.put("Locomoção, hospedagem, alimentação, combustíveis e lubrificantes","Combustível e Hospedagem");
		namesSenadores.put("Passagens aéreas, aquáticas e terrestres nacionais","Passagens");
		namesSenadores.put("Serviços de Segurança Privada","Segurança");
	}
	
	public static String getShortName(int nameType, String name){
		String result = null;
		
		if (nameType == DEPUTADO){
			result = namesDeputados.get(name);
		} else if (nameType == SENADOR){
			result = namesSenadores.get(name);
		}
		if (result == null) {
			return name;
		} else {
			return result;
		}
	}
	
}


