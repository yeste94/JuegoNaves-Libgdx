package com.yeste.juegoLluvia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Puntuacion implements Screen {
	final Drop game;
	private OrthographicCamera camera;
	
	private Texture fondo;
	private Texture atras;
	
	private SpriteBatch batch;
	private Rectangle bordes;
	
	private BitmapFont fuente;
	
	protected float xMinima; //Estos atributos sirven para poner las coordenadas para pulsar el botón.
	protected float yMinima;
	protected float xMaxima;
	protected float yMaxima;
	
	private ArrayList<Integer> pun = new ArrayList<Integer>();
	
	public Puntuacion(final Drop gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		fondo = new Texture(Gdx.files.internal("imagenes/Fondo-de-pantalla-El-espacio.png"));
		atras = new Texture(Gdx.files.internal("imagenes/atras.png"));
		
		batch = new SpriteBatch();
		
		fuente = new BitmapFont(Gdx.files.internal("fuente.fnt"), Gdx.files.internal("fuente.png"), false);
		
		bordes = new Rectangle(0, (Gdx.graphics.getHeight()-64), atras.getWidth(), atras.getHeight());
		xMinima = bordes.x;
		yMaxima = Gdx.graphics.getHeight() - bordes.y;
		xMaxima = bordes.x + bordes.width;
		yMinima = Gdx.graphics.getHeight() - (bordes.y + bordes.height);
		
		String cadena;
		FileReader f;
		try { //Leemos linea a linea del archivo puntuacion y se va añadiendo al array con las puntuaciones
			f = new FileReader("puntuacion.txt");
		      BufferedReader b = new BufferedReader(f);
		      while((cadena = b.readLine())!=null) {
		    	  pun.add(Integer.parseInt(cadena));
		      }
		      b.close();
		      f.close();
		      
		      
		      
		      
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		batch.draw(fondo,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(atras,0, (Gdx.graphics.getHeight()-64));
		fuente.draw(batch,"Puntuaciones", Gdx.graphics.getWidth()/2-128,Gdx.graphics.getHeight()-15);
		
		//Ordenamos el Array de forma descendente
		Collections.sort(pun);
		Collections.reverse(pun);
		//Pintamos un maximo de 10 puntuaciones en la pantalla, cinco en una parte y 5 en otra.
		Iterator i=pun.iterator();
		int cont=1,heigth=100,width=128;
		while(i.hasNext()){
			int p=(Integer) i.next();
			if(cont>=6){
				if(cont==6) heigth=100; 
				width=256;
				fuente.draw(batch,(cont)+": "+p+"",Gdx.graphics.getWidth()-width,Gdx.graphics.getHeight()-heigth);
			}else{
				fuente.draw(batch,(cont)+": "+p+"",Gdx.graphics.getWidth()/3-width,Gdx.graphics.getHeight()-heigth);
			}
			cont++;
			heigth+=50;
			if(cont>10) break;
			
		}
		
		batch.end();
		// Si se pulsa el botón "escape" en PC o "Back" en android
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) 
			game.setScreen(new MainMenuScreen(game)); // Volvemos al menú principal
		//Comprobar si se pulsa el boton atras.
		if (Gdx.input.isTouched() ) {
			
			if(Gdx.input.getX() > xMinima && Gdx.input.getX() < xMaxima && // Devuelve true si se pulsa dentro de los límites
					   Gdx.input.getY() > yMinima && Gdx.input.getY() < yMaxima){
				game.setScreen(new MainMenuScreen(game));
			}
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
