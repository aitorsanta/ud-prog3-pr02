package ud.prog3.pr02;

import java.util.ArrayList;

import javax.swing.JPanel;

/** "Mundo" del juego del coche.
 * Incluye las fÌsicas para el movimiento y los choques de objetos.
 * Representa un espacio 2D en el que se mueven el coche y los objetos de puntuaciÛn.
 * @author Andoni EguÌluz Mor·n
 * Facultad de IngenierÌa - Universidad de Deusto
 */

public class MundoJuego {
	private JPanel panel;  // panel visual del juego
	CocheJuego miCoche;    // Coche del juego
	boolean [] aBoolean = new boolean [4];	//Creamos array de booleanos:
		
	public void inicializarArrayFalse(boolean [] aBoolean){
		for(int i=0; i<4; i++){
			aBoolean[i]=false;
		}
	}
	
	
	/** Construye un mundo de juego
	 * @param panel	Panel visual del juego
	 */
	public MundoJuego( JPanel panel ) {
		this.panel = panel;
	}

	/** Crea un coche nuevo y lo aÒade al mundo y al panel visual
	 * @param posX	PosiciÛn X de pixel del nuevo coche
	 * @param posY	PosiciÛn Y de pÌxel del nuevo coche
	 */
	public void creaCoche( int posX, int posY ) {
		// Crear y aÒadir el coche a la ventana
		miCoche = new CocheJuego();
		miCoche.setPosicion( posX, posY );
		panel.add( miCoche.getGrafico() );  // AÒade al panel visual
		miCoche.getGrafico().repaint();  // Refresca el dibujado del coche
	}
	
	/** Devuelve el coche del mundo
	 * @return	Coche en el mundo. Si no lo hay, devuelve null
	 */
	public CocheJuego getCoche() {
		return miCoche;
	}

	/** Calcula si hay choque en horizontal con los lÌmites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posiciÛn actual
	 * @return	true si hay choque horizontal, false si no lo hay
	 */
	public boolean hayChoqueHorizontal( CocheJuego coche ) {
		return (coche.getPosX() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosX()>panel.getWidth()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}
	
	/** Calcula si hay choque en vertical con los lÌmites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posiciÛn actual
	 * @return	true si hay choque vertical, false si no lo hay
	 */
	public boolean hayChoqueVertical( CocheJuego coche ) {
		return (coche.getPosY() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosY()>panel.getHeight()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}

	/** Realiza un rebote en horizontal del objeto de juego indicado
	 * @param coche	Objeto que rebota en horizontal
	 */
	public void rebotaHorizontal( CocheJuego coche ) {
		// System.out.println( "Choca X");
		double dir = coche.getDireccionActual();
		dir = 180-dir;   // Rebote espejo sobre OY (complementario de 180)
		if (dir < 0) dir = 360+dir;  // CorrecciÛn para mantenerlo en [0,360)
		coche.setDireccionActual( dir );
	}
	
	/** Realiza un rebote en vertical del objeto de juego indicado
	 * @param coche	Objeto que rebota en vertical
	 */
	public void rebotaVertical( CocheJuego coche ) {
		// System.out.println( "Choca Y");
		double dir = miCoche.getDireccionActual();
		dir = 360 - dir;  // Rebote espejo sobre OX (complementario de 360)
		miCoche.setDireccionActual( dir );
	}
	
	/** Calcula y devuelve la posiciÛn X de un movimiento
	 * @param vel    	Velocidad del movimiento (en pÌxels por segundo)
	 * @param dir    	DirecciÛn del movimiento en grados (0∫ = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoX( double vel, double dir, double tiempo ) {
		return vel * Math.cos(dir/180.0*Math.PI) * tiempo;
	}
	
	/** Calcula y devuelve la posiciÛn X de un movimiento
	 * @param vel    	Velocidad del movimiento (en pÌxels por segundo)
	 * @param dir    	DirecciÛn del movimiento en grados (0∫ = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoY( double vel, double dir, double tiempo ) {
		return vel * -Math.sin(dir/180.0*Math.PI) * tiempo;
		// el negativo es porque en pantalla la Y crece hacia abajo y no hacia arriba
	}
	
	/** Calcula el cambio de velocidad en funciÛn de la aceleraciÛn
	 * @param vel		Velocidad original
	 * @param acel		AceleraciÛn aplicada (puede ser negativa) en pixels/sg2
	 * @param tiempo	Tiempo transcurrido en segundos
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidadConAceleracion( double vel, double acel, double tiempo ) {
		return vel + (acel*tiempo);
	}
	
	/**
	 * Si han pasado más de 1,2 segundos desde la última, crea una estrella nueva en una posición aleatoria
	 * y la añade al mundo y al panel visual
	 */
	protected long horaMilisec; //Atributo para guardar la hora en milisegundos
	
	public void creaEstrella(ArrayList <JLabelEstrella> aEstrellas){
		for(int i=0; i<100;i++){
			if(System.currentTimeMillis()>1.2){
			JLabelEstrella e = new JLabelEstrella(); //Creamos una nueva estrella
			aEstrellas.add(e); //Añadimos nueva estrella en el array
			horaMilisec = System.currentTimeMillis(); //Guardar el tiempo en milisegundos de la creación de la última estrella
			
			}
		}
	}
	
	/**
	 * Quita todas las estrellas que lleven en pantalla demasiado tiempo
	 * y rota 10 grados las que sigan estando
	 * @param maxTiempo		Tiempo máximo para que se mantengan las estrellas (msegs)
	 * @return Número de estrellas quitadas
	 */
	public int quitaYRotaEstrellas(long maxTiempo){
		int numEstrellasQuitadas=0; //Variable que recoge el número de estrellas quitadas
		maxTiempo=6; //Tiempo máximo para que se mantengan las estrellas
		
		
		return numEstrellasQuitadas;
	}
	
	/**
	 * Calcula si hay choques del coche con alguna estrella (o varias). Se considera el choque si
	 * se tocan las esferas lógicas del coche y las estrellas. Si es así, las elimina.
	 * @return Número de estrellas eliminadas
	 */
	public int choquesConEstrellas(){
		int numEstrellasEliminadas=0;
		
		
		return numEstrellasEliminadas;
	}
}
