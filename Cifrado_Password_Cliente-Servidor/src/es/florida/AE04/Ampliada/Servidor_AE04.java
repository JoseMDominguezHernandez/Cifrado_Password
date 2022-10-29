package es.florida.AE04.Ampliada;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor_AE04 {
	
	/* Method:	main()
	 * Action:	Crea el objeto Servidor y lo deja a la escucha. Cuando recibe una petici�n
	 * 			desde un Cliente lanza un hilo con la respuesta. El bucle permite que tras 
	 * 			lanzar el hilo continue a la escucha hasta recibir otra petici�n de otro cliente.
	 * Input:	Asignamos n�mero de puerto por variable pero podr�amos pedir a usuario
	 * Output:	Lanza un hilo al cliente que ha hecho la solicitud. */
	public static void main(String[] args) throws IOException, InterruptedException {

		int numPuerto = 1234;
		ServerSocket socketEscucha = new ServerSocket(numPuerto);
		System.err.println("SERVIDOR >>> Arranca el servidor. Puerto " + numPuerto + " escuchando...");

		while (true) { 
			Socket conexion = socketEscucha.accept();
			Servidor_AE04_Hilo sc = new Servidor_AE04_Hilo(conexion);
			Thread hilo = new Thread(sc);
			String name = hilo.getName();
			System.err.println("\nSERVIDOR >>> �Conexi�n establecida! Lanzando hilo... " + name);
			hilo.start();
		}
	}
}
