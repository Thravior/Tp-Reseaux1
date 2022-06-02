package test;

public class Commandes {
	private static String[] commandes = {
	"cd",
	"ls",
	"upload",
	"mkdir",
	"download",
	"exit"};
	
	public static boolean estValide(String s) {
		for (int i = 0; i < 6; ++i) {
			if (s == commandes[i])
				return true;
		} 
		return false;
	}
}
