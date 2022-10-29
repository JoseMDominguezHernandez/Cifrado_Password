package es.florida.AE04.Basica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Servidor_AE04_Hilo implements Runnable {

	BufferedReader br;
	PrintWriter pw;
	Socket socket;

	//Constructor para el hilo
	public Servidor_AE04_Hilo(Socket socket) {
		this.socket = socket;
	}

	
	/* Method:	run()
	 * Action:	Instrucciones para ejecutar las tareas del Servidor. 
	 * 			(1) Env�a un objeto Password vacio a Cliente para que rellene s�lo la contrase�a con texto plano. 
	 * 			(2) Recibe de vuelta el objeto con la contrase�a escrita por le usuario.
	 * 			(3) Encripta la contrase�a con la funci�n: 'encriptar(contrasenya) y la sustituye en el objeto
	 * 			(4) Devuelve el objeto al Cliente para que lo presente por consola.
	 * Input:	Objeto con contrase�a en texto plano.
	 * Output:	Objeto vacio para que rellene usuario desde Cliente / Objeto completo con la contrase�a
	 * 			encriptada.*/
	@Override
	public void run() {

		try {
			//(1)
			ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
			Password contrasenya = new Password("Contrase�a", "Encriptado");
			outObjeto.writeObject(contrasenya);
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()  + " >>> Enviado objeto a CLIENTE: " + contrasenya.passwordToString());

			//(2)
			ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());	
			Password contrasenyaMod = (Password) inObjeto.readObject();						
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()  + " >>> Recibida contrase�a de CLIENTE: " + contrasenyaMod.getTextoPlano());
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()  + " >>> Encriptando...");
			Thread.sleep(2000);

			//(3)
			String contrasenyaEncriptada = encriptar(contrasenyaMod.textoPlano);
			contrasenyaMod.setEncrypt(contrasenyaEncriptada);
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()  + " >>> Encriptaci�n finalizada: " + contrasenyaEncriptada);
			Thread.sleep(1000);
			
			//(4)
			outObjeto = new ObjectOutputStream(socket.getOutputStream());
			outObjeto.writeObject(contrasenyaMod);
			System.err.println(	"SERVIDOR >>> " + Thread.currentThread().getName()  + " >>> Enviado objeto completo a CLIENTE: " + contrasenyaMod.passwordToString() );
			
			inObjeto.close();
			outObjeto.close();
			socket.close();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	
	/* Method:	encriptar()
	 * Action:	recibe una contrase�a y la encripta sumando un n�mero al c�digo ASCII de cada 
	 * 			'char' que la compone. Si es un caracter no imprimible (de 0 a 31) lo sustituye 
	 * 			por un '*' (DEC: 42) 
	 * Input:	String con la contrase�a introducida por le usuario
	 * Output:	String con la contrase�a encriptada*/
	public static String encriptar(String contrasenya) {

		ArrayList<String> passEncrypted = new ArrayList<String>();

		int letra = 0;
		for (int i = 0; i < contrasenya.length(); i++) {
			letra = contrasenya.charAt(i) + 1;
			if (letra >= 0 && letra <= 31) letra = 42;
			passEncrypted.add(String.valueOf((char) letra));
		}
		String paswordEncryted = String.join("", passEncrypted);
		return paswordEncryted;
	}

}
