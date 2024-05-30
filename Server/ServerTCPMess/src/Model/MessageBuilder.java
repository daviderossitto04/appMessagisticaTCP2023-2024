package Model;

public class MessageBuilder {
	
	public static String generateMessageForRegister(String username, String password, String nome, String cognome, String email) {
		return username + "|£$%&" + password + "|£$%&" + nome + "|£$%&" + cognome + "|£$%&" + email + "|£$%&" + "\n";
	}

	public static String generateMessageForLogin(String username, String password) {
		return username + "|£$%&" + password + "|£$%&" + '\n';
	}

	public static String splitAndGenerateMessageForSend(Messaggio mx) {
		return mx.usernameMittente + "|£$%&" + mx.usernameDestinatario + "|£$%&" + mx.getContenuto() + "|£$%&" + mx.getIdMessaggio() + "|£$%&" + mx.getIdChat() + "|£$%&" + "\n";
	}

	public static Messaggio generateMessageForReceive(String requestToSplitForReceive) {
		String[] newString = requestToSplitForReceive.split("\\|£\\$%&");
		Messaggio mx = new Messaggio();
		mx.setUsernameMittente(newString[0]);
		mx.setUsernameDestinatario(newString[1]);
		mx.setContenuto(newString[2]);
		mx.setIdMessaggio(Integer.parseInt(newString[3]));
		mx.setIdChat(Integer.parseInt(newString[4]));
		return mx;
	}

	public static Messaggio fromSplitMessageToMex(String[] split) {
		Messaggio mx = new Messaggio();
		mx.setUsernameMittente(split[0]);
		mx.setUsernameDestinatario(split[1]);
		mx.setContenuto(split[2]);
		mx.setIdMessaggio(Integer.parseInt(split[3]));
		mx.setIdChat(Integer.parseInt(split[4]));
		return mx;
	}

	public static String[] onlySplitMessage(String messageForSplit) {
		return messageForSplit.split("\\|£\\$%&");
	}

	public static String generateForLoginCorrect(String nome, String cognome, String email, String username) {
		return "true" + "|£$%&" + username + "|£$%&" + email + "|£$%&" + nome + "|£$%&" + cognome + "|£$%&" + "\n";
	}

	public static String generateForLoginUncorrect() {
		return "false" + "\\|£\\$%&" + "\n";
	}

	public static String signUpCorrect() {
		return "true" + "\n";
	}

	public static String signUpIncorrect() {
		return "false" + "\n";
	}

	public static Utente generateUserFromInitialMessage(String[] initialMessage) {
		Utente user = new Utente(initialMessage[1]);
		user.setNome(initialMessage[3]);
		user.setCognome(initialMessage[4]);
		user.setEmail(initialMessage[2]);
		return user;
	}
	public static String requestForAskIfExistUsername(String usernameDestinatario, String myUsername) {
		return myUsername+"|£$%&"+usernameDestinatario+"|£$%&"+"\n";
	}
	public static String existUserTrue(String usernameDestinatario) {
		return "true"+"|£$%&"+usernameDestinatario+"|£$%&"+"\n";
	}
	public static String existUserFalse() {
		return "false"+"\n";
	}
}
