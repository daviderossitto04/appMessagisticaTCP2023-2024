package Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Prova {

	public static void main(String[] args) {
		try (Scanner scan = new Scanner(System.in)) {
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Inserisci password:");
			String password = scan.nextLine();
			password = new String(digest.digest(password.getBytes()));
			System.out.println(password);
			
			String password2 = scan.nextLine();
			
			password2 = new String(digest.digest(password2.getBytes()));
			
			if(password.equals(password2)) {
				System.out.println("pippo");
			}
		}

	}

}
