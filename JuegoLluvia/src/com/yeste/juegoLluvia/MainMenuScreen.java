package com.yeste.juegoLluvia;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
	final Drop game;
	private OrthographicCamera camera;
	
	private Texture fondo;
	private Texture play;
	private Texture score;
	
	private SpriteBatch batch;
	private Rectangle bordesPlay;
	private Rectangle bordesScore;
	
	private BitmapFont fuente;
	
	protected float xMinimaP,xMinimaS; // Estos atributos sirven para poner las coordenadas para pulsar el botón.
	protected float yMinimaP,yMinimaS;
	protected float xMaximaP,xMaximaS;
	protected float yMaximaP,yMaximaS;
	
	private final ArrayList<Integer> pun = new ArrayList<Integer>();
	//Constructor
	public MainMenuScreen(final Drop gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		//Cargar la textura de las imagenes
		play = new Texture(Gdx.files.internal("imagenes/play.png"));
		score = new Texture(Gdx.files.internal("imagenes/score.png"));
		fondo = new Texture(Gdx.files.internal("imagenes/Fondo-de-pantalla-El-espacio.png"));
		
		batch = new SpriteBatch();
		//Cargamos la fuente a utilizar
		fuente = new BitmapFont(Gdx.files.internal("fuente.fnt"), Gdx.files.internal("fuente.png"), false);
		//Rectangulos a utilizar para los botones
		bordesPlay = new Rectangle(Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2)+50, play.getWidth(), play.getHeight());
		bordesScore = new Rectangle(Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2), score.getWidth(),score.getHeight());
		
		xMinimaP = bordesPlay.x;
		yMaximaP = Gdx.graphics.getHeight() - bordesPlay.y;
		xMaximaP = bordesPlay.x + bordesPlay.width;
		yMinimaP = Gdx.graphics.getHeight() - (bordesPlay.y + bordesPlay.height);
		
		xMinimaS = bordesScore.x;
		yMaximaS = Gdx.graphics.getHeight() - bordesScore.y;
		xMaximaS = bordesPlay.x + bordesScore.width;
		yMinimaS = Gdx.graphics.getHeight() - (bordesScore.y + bordesScore.height);
		
	}
	//Sobrecarga de constructor el cual le entra la puntuacion a guardar
	public MainMenuScreen(final Drop gam,int pun) {
		game = gam;
		this.pun.add(pun);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		play = new Texture(Gdx.files.internal("imagenes/play.png"));
		score = new Texture(Gdx.files.internal("imagenes/score.png"));
		fondo = new Texture(Gdx.files.internal("imagenes/Fondo-de-pantalla-El-espacio.png"));
		
		batch = new SpriteBatch();
		
		fuente = new BitmapFont(Gdx.files.internal("fuente.fnt"), Gdx.files.internal("fuente.png"), false);
		
		bordesPlay = new Rectangle(Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2)+50, play.getWidth(), play.getHeight());
		bordesScore = new Rectangle(Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2), score.getWidth(),score.getHeight());
		
		xMinimaP = bordesPlay.x;
		yMaximaP = Gdx.graphics.getHeight() - bordesPlay.y;
		xMaximaP = bordesPlay.x + bordesPlay.width;
		yMinimaP = Gdx.graphics.getHeight() - (bordesPlay.y + bordesPlay.height);
		
		xMinimaS = bordesScore.x;
		yMaximaS = Gdx.graphics.getHeight() - bordesScore.y;
		xMaximaS = bordesPlay.x + bordesScore.width;
		yMinimaS = Gdx.graphics.getHeight() - (bordesScore.y + bordesScore.height);
		
		GuardarPuntuacion(pun);
	}
	
	//Metodo que guarda la puntuacion en un archivo "txt"
	public void GuardarPuntuacion(int pun){
		try {
			FileWriter fw=new FileWriter("puntuacion.txt",true);
			String cad="";
			fw.write(pun+""+"\n");
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//Pinta el fondo
		batch.draw(fondo,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		
		fuente.draw(batch,"Juego Espacial", Gdx.graphics.getWidth()/2-128,Gdx.graphics.getHeight()-15);
		batch.end();
		//Pintamos los botones play y score
		batch.begin();
		batch.draw(play,Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2)+50);
		batch.draw(score,Gdx.graphics.getWidth()/2-64, (Gdx.graphics.getHeight()/2));
		batch.end();
		
		//Comprobamos cuando se pulsa algun boton
		if (Gdx.input.isTouched() ) {
			if(Gdx.input.getX() > xMinimaP && Gdx.input.getX() < xMaximaP && // Devuelve true si se pulsa dentro de los límites
					   Gdx.input.getY() > yMinimaP && Gdx.input.getY() < yMaximaP){
				game.setScreen(new JuegoNave(game));
			}
			if(Gdx.input.getX() > xMinimaS && Gdx.input.getX() < xMaximaS && // Devuelve true si se pulsa dentro de los límites
					   Gdx.input.getY() > yMinimaS && Gdx.input.getY() < yMaximaS){
				game.setScreen(new Puntuacion(game));
			}
			
			dispose();
			
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
