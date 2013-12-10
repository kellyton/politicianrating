package util;

import java.util.HashMap;

public class NamesMap {

	public static final int DEPUTADO = 0;
	public static final int SENADOR = 1;

	private static HashMap<String, String> namesDeputados = new HashMap<String, String>();
	private static HashMap<String, String> namesSenadores = new HashMap<String, String>();
	
	//initialization
	static {
		namesDeputados.put("ASSINATURA DE PUBLICAÇÕES","Assinatura de publicações");	
		namesDeputados.put("COMBUSTÍVEIS E LUBRIFICANTES","Combustível");
		namesDeputados.put("CONSULTORIAS, PESQUISAS E TRABALHOS TÉCNICOS","Consultorias");
		namesDeputados.put("DIVULGAÇÃO DA ATIVIDADE PARLAMENTAR","Divulgação");
		namesDeputados.put("EMISSÃO DE BILHETE AÉREO","Passagens aéreas");
		namesDeputados.put("FORNECIMENTO DE ALIMENTAÇÃO DO PARLAMENTAR","Alimentação");
		namesDeputados.put("HOSPEDAGEM ,EXCETO DO PARLAMENTAR NO DISTRITO FEDERAL.","Hospedagem");
		namesDeputados.put("LOCAÇÃO DE VEÍCULOS AUTOMOTORES OU FRETAMENTO DE EMBARCAÇÕES","Locação de veículos");
		namesDeputados.put("MANUTENÇÃO DE ESCRITÓRIO DE APOIO A ATIVIDADE PARLAMENTAR","Manutenção de escritório");
		namesDeputados.put("PASSAGENS AÉREAS E FRETAMENTO DE AERONAVES","Passagens aéreas e fretamentos");
		namesDeputados.put("SERVIÇO DE SEGURANÇA PRESTADO POR EMPRESA ESPECIALIZADA","Segurança");
		namesDeputados.put("SERVIÇOS POSTAIS","Serviços postais");
		namesDeputados.put("TELEFONIA","Telefonia");
		
		namesDeputados.put("EmissÆo Bilhete Areo","Passagens aéreas");
		namesDeputados.put("PASSAGENS AREAS","Passagens aéreas");
		namesDeputados.put("PASSAGENS TERRESTRES, MARÖTIMAS OU FLUVIAIS","Passagens Terrestes/Marítimas/Fluviais");

		namesDeputados.put("LOCAÇO DE VEÖCULOS AUTOMOTORES OU FRETAMENTO DE EMBARCAåES ","Veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇO OU FRETAMENTO DE VEÖCULOS AUTOMOTORES","Veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇO OU FRETAMENTO DE EMBARCAåES","Veículos/embarcações/aeronaves");
		namesDeputados.put("LOCAÇO OU FRETAMENTO DE AERONAVES","Veículos/embarcações/aeronaves");
		
		namesDeputados.put("COMBUSTÖVEIS E LUBRIFICANTES.","Combustível");
		
		namesDeputados.put("ASSINATURA DE PUBLICAåES","Assinatura de publicações");
		namesDeputados.put("CONSULTORIAS, PESQUISAS E TRABALHOS TCNICOS.","Consultorias");
		namesDeputados.put("DIVULGAÇO DA ATIVIDADE PARLAMENTAR.","Divulgação");		
		namesDeputados.put("FORNECIMENTO DE ALIMENTAÇO DO PARLAMENTAR","Alimentação");
		namesDeputados.put("HOSPEDAGEM ,EXCETO DO PARLAMENTAR NO DISTRITO FEDERAL.","Hospedagem");
		namesDeputados.put("MANUTENÇO DE ESCRITàRIO DE APOIO · ATIVIDADE PARLAMENTAR","Manutenção de escritório");
		namesDeputados.put("SERVIO DE SEGURANA PRESTADO POR EMPRESA ESPECIALIZADA.","Segurança");
		namesDeputados.put("SERVIOS POSTAIS","Serviços postais");
		namesDeputados.put("Telefonia","Telefonia");
		namesDeputados.put("SERVIO DE TµXI, PEDµGIO E ESTACIONAMENTO","Taxi/Pedágio/Estacionamento");
		
		
		namesSenadores.put("Aluguel de imóveis para escritório político, compreendendo despesas concernentes a eles","Imóveis");
		namesSenadores.put("Aquisição de material de consumo para uso no escritório político, inclusive aquisição ou locação de software, despesas postais, aquisição de publicações, locação de móveis e de equipamentos","Material de Consumo");
		namesSenadores.put("Contratação de consultorias, assessorias, pesquisas, trabalhos técnicos e outros serviços de apoio ao exercício do mandato parlamentar","Consultorias");
		namesSenadores.put("Divulgação da atividade parlamentar","Divulgação");
		namesSenadores.put("Locomoção, hospedagem, alimentação, combustíveis e lubrificantes","Combustível e Hospedagem");
		namesSenadores.put("Passagens aéreas, aquáticas e terrestes nacionais","Passagens");
		namesSenadores.put("Serviços de Segurança Privada","Segurança");
		
		namesSenadores.put("Aluguel de im�veis para escrit�rio pol�tico, compreendendo despesas concernentes a eles.","Imóveis");
		namesSenadores.put("Aquisi��o de material de consumo para uso no escrit�rio pol�tico, inclusive aquisi��o ou loca��o de software, despesas postais, aquisi��o de publica��es, loca��o de m�veis e de equipamentos. ","Material de Consumo");
		namesSenadores.put("Contrata��o de consultorias, assessorias, pesquisas, trabalhos t�cnicos e outros servi�os de apoio ao exerc�cio do mandato parlamentar","Consultorias");
		namesSenadores.put("Divulga��o da atividade parlamentar","Divulgação");
		namesSenadores.put("Locomo��o, hospedagem, alimenta��o, combust�veis e lubrificantes","Combustível/Hotel/Alimentação");
		namesSenadores.put("Passagens a�reas, aqu�ticas e terrestres nacionais","Passagens");
		namesSenadores.put("Servi�os de Seguran�a Privada","Segurança");
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


