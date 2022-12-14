package screens;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import main.Main;
import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import widgets.Label;
import widgets.Button;
import widgets.GUIComponent;

public class UserGuideScreenModes {

	//static variables
	public static String CUST_PG1 = "./res/CustUserGuide1.png";
	public static String CUST_PG2 = "./res/CustUserGuide2.png";
	public static String CUST_PG3 = "./res/CustUserGuide3.png";
	
	public static String GAME_PG1 = "./res/GameUserGuide1.png";
	public static String GAME_PG2 = "./res/GameUserGuide2.png";
	public static String GAME_PG3 = "./res/GameUserGuide3.png";
	
	public static String LESS_PG1 = "./res/LessUserGuide1.png";
	public static String LESS_PG2 = "./res/LessUserGuide2.png";
	
	// instance variables	
	private ArrayList<GUIComponent> guiComponents;
	private ArrayList<String> images;
	private Label label;
	private Loader loader;
	private Button left, right, close;
	private int page =0;
	private float z;
	public static final String LEFT_BUTTON_ENABLED_TEXTURE_FILE = "./res/leftButtonEnabled.png";
	public static final String LEFT_BUTTON_DISABLED_TEXTURE_FILE = "./res/leftButtonDisabled.png";
	public static final String RIGHT_BUTTON_ENABLED_TEXTURE_FILE = "./res/rightButtonEnabled.png";
	public static final String RIGHT_BUTTON_DISABLED_TEXTURE_FILE = "./res/rightButtonDisabled.png";
	public static final String CLOSE_BUTTON = "./res/closeButton.png";
	private long window;
	private float screenWidth;
	private float screenHeight;
	private int max;
	
	/**
	 * Creates the personalized user guides for each screen
	 * 
	 * @param window			the window ID
	 * @param loader			the loader object
	 * @param screenWidth		the width of the screen
	 * @param screenHeight		the height of the screen
	 * @param z					the z-value of the components of the screen
	 */
	public UserGuideScreenModes(long window, Loader loader, float screenWidth, float screenHeight, float z) 
	{
		//picks the images that need to be displayed
		getImages();
		
		//sets the variables
		this.z = z;
		this.window = window;
		this.screenWidth = screenWidth;
		this.screenHeight= screenHeight;
		
		this.loader= loader;
		
		//creates the image that needs to be displayed
		float[] vertices = Entity.getVertices(screenWidth, screenHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		float labelX = 0;
		float labelY = 0;
		
		Vector3f position = new Vector3f(labelX, labelY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		
		int textureID = loader.loadTexture(images.get(0));
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		label = new Label(selectionASimModel, position, rotation, 1, screenWidth, screenHeight);
		guiComponents= new ArrayList<GUIComponent>();
		
		//calls on the method that creates the arrow keys
		arrowButtons();
		
		guiComponents.add(label);
		
		//sets the maximum number of pages
		if(Main.userType == 2)
			max=1;
		else
			max=2;
		
	}
	
	/**
	 * renders the objects in the user guide screen
	 * 
	 * @param renderer is the renderer 
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
	}
	
	/**
	 * Contains the logic for input handling
	 * 
	 * @param main				where the main loop is
	 * @param key				the key that was pressed
	 * @param leftClick			whether the left mouse button was pressed
	 * @param rightClick		whether the right mouse button was pressed
	 */
	public void input(Main main, int key, boolean leftClick) {
	
		// mouse input
		mouseInput(main, leftClick);
	}

	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param main				where the main loop is
	 * @param leftClick			whether the left mouse button was pressed
	 * @param rightClick		whether the right mouse button was pressed
	 */
	public void mouseInput(Main main, boolean leftClick) {

		// get cursor coordinate

		DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);

		glfwGetCursorPos(window, cursorPosX, cursorPosY);

		float x = (float) cursorPosX.get(0);
		float y = (float) cursorPosY.get(0);

		// convert cursor coordinate to OpenGL world coordinate
		x -= screenWidth/2;
		y *= -1;
		y += screenHeight/2;

			// if left mouse button was pressed
		if (leftClick) {

			if(right.getAabb().intersects(x, y)&&right.isEnabled())
			{
				incChangeTheBackground();
			}
			else if(left.getAabb().intersects(x,y)&& left.isEnabled())
			{
				decChangeTheBackground();
			}
			else if(close.getAabb().intersects(x,y))
			{
				main.setCurrScreen(Main.userType);
			}
		}
	}

	/**
	 * changes the background to the next one
	 *
	 */
	public void incChangeTheBackground()
	{
		//sets up the next page
		page++;
		label.getModel().setTextureID(images.get(page), loader);

		//checks if this is the last page
		if(page==max)
		{
			right.getModel().setTextureID(RIGHT_BUTTON_DISABLED_TEXTURE_FILE, loader);
			right.setEnabled(false);
			left.getModel().setTextureID(LEFT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			left.setEnabled(true);
		}
		else {
			left.getModel().setTextureID(LEFT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			right.getModel().setTextureID(RIGHT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			left.setEnabled(true);
			right.setEnabled(true);
		}
	}
	/**
	 * changes the background to the previous one
	 */
	public void decChangeTheBackground()
	{
		//sets up the page
		page--;
		label.getModel().setTextureID(images.get(page), loader);

		//checks if this is the first page
		if(page==0)
		{
			left.getModel().setTextureID(LEFT_BUTTON_DISABLED_TEXTURE_FILE, loader);
			left.setEnabled(false);

			right.getModel().setTextureID(RIGHT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			right.setEnabled(true);
		}
		else {
			left.getModel().setTextureID(LEFT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			right.getModel().setTextureID(RIGHT_BUTTON_ENABLED_TEXTURE_FILE, loader);
			left.setEnabled(true);
			right.setEnabled(true);
		}
	}

	/**
	 * creates the buttons used in the user guide
	 */
	public void arrowButtons()
	{
		float[] vertices = Entity.getVertices(50, 50, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();

		float labelX = -440;
		float labelY = -220;

		Vector3f position = new Vector3f(labelX, labelY, z);
		Vector3f rotation = new Vector3f(0,0,0);

		int textureID = loader.loadTexture(LEFT_BUTTON_DISABLED_TEXTURE_FILE);
		Model leftModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

		left = new Button(leftModel, position, rotation, 1, 50, 50);
		left.setEnabled(false);

		 labelX = 470;
		 labelY = -220;

		 position = new Vector3f(labelX, labelY, z);
		 rotation = new Vector3f(0,0,0);

		textureID = loader.loadTexture(RIGHT_BUTTON_ENABLED_TEXTURE_FILE);
		Model rightModel = loader.loadToVAO(vertices, texCoords, indices, textureID);


		right = new Button(rightModel, position, rotation, 1, 50, 50);

		labelX = -440;
		 labelY = 250;

		 position = new Vector3f(labelX, labelY, z);
		 rotation = new Vector3f(0,0,0);

		textureID = loader.loadTexture(CLOSE_BUTTON);
		Model closeModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

		close = new Button(closeModel, position, rotation, 1, 50, 50);

		guiComponents.add(left);
		guiComponents.add(right);
		guiComponents.add(close);
	}

	/**
	 * uses the correct images for the corresponding mode
	 */
	private void getImages()
	{
		images = new ArrayList<String>();

		switch(Main.userType) {

			//game
			case 1:
				images.add(GAME_PG1);
				images.add(GAME_PG2);
				images.add(GAME_PG3);
				break;

				//lesson
			case 2:
				images.add(LESS_PG1);
				images.add(LESS_PG2);
				break;

				//customized
			case 3:
				images.add(CUST_PG1);
				images.add(CUST_PG2);
				images.add(CUST_PG3);
				break;
		}
	}
}