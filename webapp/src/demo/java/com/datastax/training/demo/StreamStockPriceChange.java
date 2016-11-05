package com.datastax.training.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.fluttercode.datafactory.impl.DataFactory;

public class StreamStockPriceChange {

	public static void main(String[] args) {
		int portNumber = 5006;
		DataFactory df = new DataFactory();
		Random rand = new Random();
		format.setMaximumFractionDigits(2);

		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			while (true) {
				boolean hasPriceIncreased = rand.nextBoolean();
				String priceChange = format.format(rand.nextDouble() * 10);
				if (!hasPriceIncreased) {
					priceChange = "-" + priceChange;
				}
				// outputting as symbol, price, up/down, change, lastupdate
				out.println(df.getItem(symbols) + "," + format.format(rand.nextDouble() * 100) + "," + priceChange + ","
						+ hasPriceIncreased + "," + new Date());
				Thread.sleep(df.getNumberBetween(1, 100));
			}

		} catch (IOException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static DecimalFormat format = new DecimalFormat();
	private static String[] symbols = { "AGO", "FFIV", "DLTR", "NSC", "GILD", "WLK", "TMK", "BBBY", "VLY", "ADP", "AVT",
			"ACAS", "WFC", "CVC", "EA", "WDC", "XOM", "SEIC", "NATI", "PXD", "UPL", "ROVI", "AEE", "EAT", "UNH", "ENDP",
			"C", "HCA", "TWC", "AGNC", "GES", "PWR", "FRT", "WMB", "LII", "SNPS", "KLAC", "PCL", "TOL", "SYK", "FLO",
			"OMC", "WDR", "T", "CLR", "RES", "CBSH", "PBI", "PH", "PRGO", "ARIA", "FII", "DHI", "ABC", "PG", "CBT",
			"STR", "TCO", "RRC", "KMX", "BOKF", "GLW", "IR", "COH", "DBD", "GCI", "NDSN", "CNA", "ARE", "TK", "STT",
			"M", "PBCT", "UNM", "TSO", "QCOM", "LXK", "PLCM", "BDN", "GNTX", "MMC", "SCHW", "THO", "HAR", "ADSK",
			"TROW", "AFL", "UNP", "RGA", "ADT", "NBR", "AVY", "BDX", "BAC", "TFX", "X", "EXC", "HCN", "MRVL", "TAP",
			"AME", "COF", "KMB", "HD", "MAR", "UDR", "SYY", "HIG", "CSX", "PPL", "ETFC", "NKE", "KSU", "SBAC", "FISV",
			"OGE", "MCD", "WIN", "MKL", "PNR", "PLD", "LLY", "KBR", "AES", "RAX", "UTX", "CAT", "WPC", "PENN", "MDLZ",
			"RHT", "KORS", "KEX", "EMC", "KMI", "WHR", "CST", "UNT", "ITC", "HE", "NOV", "TDW", "SJM", "PKG", "ANSS",
			"NDAQ", "LOW", "AVX", "RSG", "ITT", "SRCL", "STI", "USB", "SDRL", "RE", "PEG", "PPG", "AOS", "HCP", "ENR",
			"EL", "CE", "IEX", "ABT", "CERN", "CAR", "CYH", "ALB", "FNFG", "UTHR", "MLM", "RAI", "PDCO", "BLL", "SO",
			"GWW", "SCTY", "CVS", "BCR", "NWL", "IP", "ICE", "TWX", "ECL", "TAHO", "HTS", "JCP", "ATR", "SYMC", "JOE",
			"TECD", "MFA", "NOC", "LGF", "EFX", "TSLA", "AGCO", "HXL", "CSC", "HOT", "ACGL", "PAYX", "BMRN", "TRIP",
			"AMAT", "CCL", "IT", "TER", "TPX", "ROK", "CMS", "COG", "VAR", "FDS", "GXP", "PHM", "CRI", "UHS", "UPS",
			"MUR", "AMD", "NTAP", "DFS", "MDRX", "JAH", "IHS", "MAS", "MAT", "BR", "AMGN", "H", "ARG", "LVS", "LNG",
			"LEG", "COL", "BAX", "MCO", "APD", "ORLY", "MHK", "HUM", "KMT", "HSY", "HPT", "MAA", "HRS", "SM", "NAV",
			"DGX", "DAL", "BHI", "CMA", "SIRI", "WM", "PNC", "SNDK", "CL", "HNT", "SCCO", "OSK", "PEP", "SKT", "DUK",
			"AAPL", "FULT", "DLB", "AMTD", "AVGO", "EXP", "AAP", "NFG", "SBUX", "OI", "MPC", "ATML", "BRO", "WRI",
			"LAMR", "SBH", "MJN", "K", "GRMN", "BPOP", "DDD", "CPRT", "TGT", "COP", "SNH", "CELG", "AXS", "MAN", "MCHP",
			"GE", "WSM", "PFE", "ASH", "NTRS", "BBY", "KRO", "OHI", "BRCD", "DVA", "CIE", "PCP", "RMD", "APH", "DRE",
			"JWN", "AIG", "DE", "SWI", "DSW", "VMC", "CAH", "FLS", "ADI", "RL", "ZION", "FIS", "JOY", "UAL", "KO",
			"FSLR", "O", "AVP", "CHK", "MCK", "LUV", "LAZ", "GAS", "CCK", "TRV", "ALKS", "ARW", "ESS", "WOOF", "FB",
			"GPN", "Q", "JCI", "NWSA", "LNT", "DLR", "OCN", "OIS", "UGI", "ED", "PCG", "RS", "SIX", "KEY", "BOH", "NEU",
			"IGT", "JKHY", "TECH", "CPA", "ROL", "BSX", "NEE", "PM", "DF", "NI", "PNRA", "SWKS", "ALR", "AJG", "VMI",
			"SLG", "HUN", "WLL", "ADS", "NNN", "CR", "GS", "EQIX", "CAM", "HLF", "GWR", "BRKR", "AMP", "AWK", "SLGN",
			"Y", "AVB", "RPM", "SWK", "MU", "RCL", "TSN", "L", "FOSL", "CPT", "MTD", "LVLT", "CMI", "DRQ", "KIM", "GEF",
			"ZBRA", "PX", "EMR", "TWO", "PRU", "P", "MO", "WCC", "WTW", "MSCI", "UHAL", "SIVB", "A", "CHRW", "LM",
			"CHTR", "URI", "MRK", "WCN", "DOW", "EIX", "MMM", "MSM", "ZNGA", "EW", "ABBV", "CBG", "VVC", "JPM", "AKAM",
			"V", "G", "APC", "VNO", "SEE", "TXT", "NLY", "EQR", "TSS", "MOS", "VAL", "TDG", "TKR", "LPI", "PNW", "PPS",
			"PTEN", "XRAY", "ATVI", "THC", "ALL", "ETN", "LUK", "CVX", "ODFL", "LECO", "UA", "WAB", "VMW", "GME", "HRL",
			"IPG", "TTC", "HON", "FRC", "ESRX", "LMCA", "LEA", "FL", "DDS", "LEN", "RTN", "XLNX", "DISH", "INGR", "EOG",
			"CCE", "SE", "MRO", "DPZ", "CLGX", "RGC", "HP", "CTL", "AFG", "SNA", "TIF", "SLAB", "DPS", "ATO", "HOG",
			"STLD", "AIZ", "XEL", "CLF", "KSS", "TGI", "GPC", "CBS", "TUP", "KRC", "OII", "NUAN", "CINF", "ORCL", "FTI",
			"EMN", "CAG", "LRCX", "SON", "WR", "MYGN", "GMCR", "DO", "NCR", "CVA", "OAS", "SWN", "SCI", "MET", "VFC",
			"DVN", "TRN", "MXIM", "CRL", "WAT", "IM", "PFG", "SPG", "ALK", "BMY", "RDC", "CTAS", "OXY", "HRC", "TRI",
			"RF", "INTC", "PCLN", "F", "RJF", "BWA", "ACM", "PRA", "CLX", "APA", "BXP", "GRA", "SPLS", "VLO", "DST",
			"AA", "LLL", "CB", "TCB", "EBAY", "R", "DECK", "DCI", "MBI", "N", "MDU", "AET", "INCY", "XEC", "CCI", "FCX",
			"CXW", "WMT", "CRS", "RYN", "VRTX", "ATW", "HSC", "AXP", "CHH", "CI", "FNF", "CFR", "EXPE", "APOL", "CME",
			"YHOO", "LBTYA", "DKS", "OKE", "BTU", "HAL", "MDVN", "CHS", "DG", "FE", "WU", "REGN", "MCY", "AIV", "CTXS",
			"PRE", "FLIR", "PSA", "MKC", "FMC", "JLL", "S", "TSCO", "HPQ", "GNW", "AHL", "ALXN", "MDR", "TE", "AMT",
			"IFF", "VR", "PANW", "CRM", "RHI", "SGEN", "DDR", "XYL", "PCAR", "VRSN", "BMS", "CNX", "SNV", "EWBC", "IBM",
			"CTSH", "CVI", "DV", "JAZZ", "WTR", "AGN", "XL", "AMZN", "POM", "MGM", "ROP", "GOOG", "SPN", "GGG", "WFM",
			"RNR", "LMT", "LPNT", "TEX", "CIT", "EV", "WPX", "GPS", "GIS", "KR", "SMG", "BRCM", "NUE", "CLI", "KOS",
			"JNJ", "AEO", "WBC", "REG", "GMT", "LSTR", "ITW", "FTNT", "FITB", "BEN", "INT", "ADM", "GM", "INTU", "VC",
			"DTE", "GRPN", "LNKD", "VTR", "SCG", "CAB", "CMCSA", "SRE", "DNR", "MNST", "CLH", "GLNG", "WYNN", "GD",
			"AMG", "STJ", "JEC", "SHLD", "SPLK", "MON", "EQT", "NUS", "COST", "CF", "FLR", "IPGP", "SFG", "WY", "IRM",
			"PKI", "CBL", "MTB", "SSYS", "OFC", "TXN", "TMO", "HRB", "EGN", "DIS", "NRG", "AN", "RGLD", "BG", "ROST",
			"IDXX", "CPN", "JBHT", "BEAV", "NYCB", "CMG", "HSIC", "CIM", "LLTC", "WYN", "HES", "NFLX", "YUM", "ACN",
			"BLK", "VSH", "ATI", "GGP", "MTW", "EXPD", "ISRG", "BBT", "JNPR", "CSL", "CMP", "OC", "FAST", "COO", "VZ",
			"HBAN", "BA", "HOLX", "ILMN", "DRI", "ORI", "DHR", "NVDA", "AON", "JBL", "AL", "MORN", "MS", "CSCO", "SLB",
			"NEM", "LNC", "MA", "NFX", "QGEN", "PII", "DOV", "LH", "ANF", "CREE", "CPB", "ETR", "ENH", "STRZA", "DD",
			"HBI", "BIIB", "DNB", "CHD", "CNP", "PSX", "SHW", "MSFT", "MSI", "AEP", "CBI", "WEC", "D", "NSM", "NBL",
			"PGR", "TRMB", "URBN", "PVH", "HAS", "AZO", "MAC", "CA", "ADBE", "MDT", "BK", "PAY", "TJX", "FDX", "XRX" };

}
