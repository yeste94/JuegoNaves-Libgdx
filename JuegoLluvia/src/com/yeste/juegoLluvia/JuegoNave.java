package com.yeste.juegoLluvia;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class JuegoNave implements Screen {
	final Drop game;
	
	//Textura de las imagenes
	private Texture fondo;
	private Texture texRoca; 
	private Texture texNave;
	private Texture misil;
	//Sonidos
	private Sound sndGota;
	private Sound disparo;
	private Sound explosion;
	private Music musFondo;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Rectangle rectangulo;
	
	private Array<Rectangle> rocas;
	private long lastDropTime;
	
	private Array<Rectangle> disparos;
	
	private BitmapFont fuente;
	private int vidas=3;
	private int puntacion=0;
	
	private int time=0;
	private int espera=25;
	private int velocidad;
	private int conVelocidad;
	
	public JuegoNave(final Drop gam) {
		this.game = gam;
		velocidad=1000000000;
		//Cargamos la textura de las imagenes cubo y gota
		fondo = new Texture(Gdx.files.internal("imagenes/Fondo-de-pantalla-El-espacio.png"));
		texRoca = new Texture(Gdx.files.internal("imagenes/asteroid2.png"));
		texNave = new Texture(Gdx.files.internal("imagenes/bucket.png"));
		misil = new Texture(Gdx.files.internal("imagenes/fireball.png"));
		
		
		//Cargamos los sonido de la lluvia y la gota
		sndGota=Gdx.audio.newSound(Gdx.files.internal("sonidos/Delay.wav"));
		musFondo=Gdx.audio.newMusic(Gdx.files.internal("sonidos/Star_Wars.mp3"));
		disparo=Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.wav"));
		explosion=Gdx.audio.newSound(Gdx.files.internal("sonidos/explosion.wav"));
		
		//Activamos que se ejecute continuamente el sonido y lo ejecuta
		musFondo.setLooping(true);
		musFondo.play(); 
		
		//Crea la camara y el SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		batch = new SpriteBatch();
		
		rectangulo = new Rectangle();
		rectangulo.x=800/2 - 64/2;
		rectangulo.y=20;
		rectangulo.width=64;
		rectangulo.height=64;
		
		disparos=new Array<Rectangle>();
		
		rocas=new Array<Rectangle>();
		spawnRaindrop();
		
		fuente = new BitmapFont(Gdx.files.internal("fuente.fnt"), Gdx.files.internal("fuente.png"), false);
	}

	@Override
	public void dispose() {
		texRoca.dispose();
		texNave.dispose();
		sndGota.dispose();
		musFondo.dispose();
		batch.dispose();
	}


	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	private void spawnRaindrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x=MathUtils.random(0,800-64);
		raindrop.y=480;
		raindrop.width=64;
		raindrop.height=64;
		rocas.add(raindrop);
		lastDropTime=TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(fondo,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		
		
		fuente.draw(batch,"Puntuacion: "+puntacion, Gdx.graphics.getWidth()/6,Gdx.graphics.getHeight()-5);
		fuente.draw(batch, "vidas: "+vidas, Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight());
		batch.end();
		camera.update();
		
		//Pintamos todos los archivos
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texNave,rectangulo.x, rectangulo.y);
		batch.end();
		
		
		//Para cuando pulsas en la pantalla
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			rectangulo.x = touchPos.x - 64/2;
		}
		//Cuando pulsas la flecha izquierda
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			rectangulo.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		//Cuando pulsas la flecha derecha
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			rectangulo.x += 200 * Gdx.graphics.getDeltaTime();
		}
		
		//Cuando pulsas el espacio
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			//Controla que no dispare continuamente
			if(time>0){
				time-=espera;
			}
			if(time<=0){
				time=300;
				//Añadir al array de disparos un nuevo disparo
				Rectangle raindrop = new Rectangle();
				raindrop.x=rectangulo.x;
				raindrop.y=rectangulo.y+64;
				raindrop.width=64;
				raindrop.height=64;
				disparos.add(raindrop);
				disparo.play();
			}

		}
		//Pintar y mover los disparos
		Iterator<Rectangle> m= disparos.iterator();
		while(m.hasNext()){
			Rectangle r=m.next();
			r.y +=200*Gdx.graphics.getDeltaTime();
			//cuando llega al arriba se borra
			if(r.y>480){
				m.remove();
			}
		}
		
		batch.begin();
		for(Rectangle dis: disparos){
			batch.draw(misil,dis.x,dis.y);
		}
		batch.end();
		
		//Colision de disparios
		Iterator<Rectangle> roca=rocas.iterator();
		while(roca.hasNext()){
			Rectangle r=roca.next();
			
			Iterator<Rectangle> dis=disparos.iterator();
			while(dis.hasNext()){
				Rectangle d=dis.next();
				
				if( r.overlaps(d) ){
					roca.remove();
					dis.remove();
					break;
				}
			}
		}
		
		
		
		//Para controlar que no se pase por la pantalla ni por la derecha ni izquierda
		if(rectangulo.x < 0) rectangulo.x=0;
		if(rectangulo.x>800-64) rectangulo.x=800-64;
		
		//Pintar las rocas aleatoriamente
		if(TimeUtils.nanoTime() - lastDropTime > velocidad) spawnRaindrop();
		
		Iterator<Rectangle> iter = rocas.iterator();
		while(iter.hasNext()){
			Rectangle raindrop = iter.next();
			raindrop.y -= 200*Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64<0) iter.remove(); puntacion++; conVelocidad++;
			if(conVelocidad==1000){
				velocidad-=50000000; 
				conVelocidad=0;
			}
				
			//Cuando la roca choca con la nave
			if(raindrop.overlaps(rectangulo)){
				iter.remove();
				vidas--;
				explosion.play();
				if(vidas==0){
					dispose();
					game.setScreen(new MainMenuScreen(game,puntacion));
				}
			}
		
		}
		
		batch.begin();
		for(Rectangle raindrop: rocas){
			batch.draw(texRoca, raindrop.x, raindrop.y);

		}
		batch.end();
				
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	
	
}
