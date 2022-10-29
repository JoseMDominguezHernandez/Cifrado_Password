package es.florida.AE04.Ampliada;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente_AE04 {
	
	/* Method:	main()
	 * Action:	(1) Arranca el cliente y lanza una petici�n al Servidor a trav�s del host y puerto en el 
	 * 				que est� a la escucha.
	 * 			(2) Recibe del Servidor un objeto vac�o que el usuario debe rellenar.
	 * 			(3)	Usuario introduce contrase�a y elige el tipo de encriptado por consola.
	 * 			(4) Devuelve objeto al Servidor para encriptarlo. 
	 * 			(5) Recibe objeto completo y encriptado y lo presenta por pantalla.
	 * Input:	Host e IP del Servidor. Objetos desde el servidor. Por consola la contrase�a a encriptar.
	 * Output:	Muestra por pantalla la contrase�a en texto plano y encriptada*/
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		//(1)
		InetSocketAddress direccion = new InetSocketAddress("localhost", 1234);
		Socket socket = new Socket();
		socket.connect(direccion);
		System.out.println("CLIENTE >>> Arranca cliente. Enviando petici�n al servidor...");

		// (2)
		ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());
		Password pass = (Password) inObjeto.readObject();
		System.out.println("CLIENTE >>> Recibido objeto de SERVIDOR >>> " + pass.passwordToString());

		// (3)
		Scanner teclado = new Scanner(System.in);
		System.out.print("\nCLIENTE >>> Introduzca una contrase�a: ");
		String texto = teclado.nextLine();
		pass.setTextoPlano(texto);

		boolean flag = false;
		while (flag == false) {
			System.out.print("\nCLIENTE >>> Tipo de cifrado: \n	    - 1. Normal.\n	    - 2. MD5 \n 	      Elija una opci�n: ");
			texto = teclado.nextLine();
			if (texto.equals("1") || texto.equals("2")) flag = true;
		}
		pass.setTipo(texto);

		// (4)
		ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
		outObjeto.writeObject(pass);
		System.out.println("\nCLIENTE >>> Enviada informaci�n a SERVIDOR");

		// (5)
		String cifrado = "";
		inObjeto = new ObjectInputStream(socket.getInputStream());
		pass = (Password) inObjeto.readObject();
		String tipo = pass.getTipo();
		if (tipo.equals("2")) cifrado = "MD5";
		else cifrado = "Standard";
		System.out.println(
				"\nCLIENTE >>> Recibido objeto completo de SERVIDOR: \n	    - Contrase�a:	" + pass.getTextoPlano()
						+ "\n	    - Encriptado:	" + pass.getEncrypt() + "\n	    - Cifrado:		" + cifrado);

		inObjeto.close();
		outObjeto.close();
		socket.close();
		teclado.close();
	}
}
