package levels;





import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import worldObjects.Block;
import worldObjects.Hole;

public class Levels {

	    private static final int    BLOCK           = 0x000000; // black
	    private static final int    EMPTY           = 0xffffff; // white
	    private static final int    START_POS       = 0x0000ff; // blue
	private int width;
	private int height;
	private Block[][] blocks;
	private Hole[][] holes; 

	

	public Levels(int levelNum) {
		this.loadLevel(levelNum);
		//loadDemoLevel();
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Block[][] getBlocks() {
		return blocks;
	}
	
	public Hole[][] getHoles(){
		return holes;
	}
//may have deleted something i needed on accident
	
	
	
	private void loadLevel(int number){
		

	        // Loading the png into a Pixmap
		Pixmap pixmap = new Pixmap(Gdx.files.internal( "data/"+ number + ".png"));

		// setting the size of the level based on the size of the pixmap
		this.setWidth(pixmap.getWidth());
		this.setHeight(pixmap.getHeight());

		blocks = new Block[this.getWidth()][this.getHeight()];
		holes = new Hole[this.getWidth()][this.getHeight()];
		
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				blocks[col][row] = null;
			}
		}
		
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				holes[col][row] = null;
			}
		}
		holes[3][4] = new Hole(new Vector2(3,4));

		for (int col = 0; col < width; col++) {
			blocks[col][0] = new Block(new Vector2(col, 0));
			blocks[col][height-1] = new Block(new Vector2(col, height-1));
			for(int row = 0; row < height;row++){
				if(col == 0){
					blocks[col][row] = new Block(new Vector2(col, row));
				}if(col == width-1){
					blocks[col][row] = new Block(new Vector2(col, row));
				}
			}
		}blocks[0][3] = null;

	}

	private void loadDemoLevel() {
		width = 20;
		height = 20;
		blocks = new Block[width][height];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				blocks[col][row] = null;
			}
		}

		for (int col = 0; col < 10; col++) {
			blocks[col][0] = new Block(new Vector2(col, 0));
			blocks[col][6] = new Block(new Vector2(col, 6));
		}
		blocks[9][2] = new Block(new Vector2(9, 2));
		blocks[9][3] = new Block(new Vector2(9, 3));
		blocks[9][4] = new Block(new Vector2(9, 4));
		blocks[9][5] = new Block(new Vector2(9, 5));
		
		blocks[0][2] = new Block(new Vector2(0, 2));
		blocks[0][3] = new Block(new Vector2(0, 3));
		blocks[0][4] = new Block(new Vector2(0, 4));
		blocks[0][5] = new Block(new Vector2(0, 5));

		
	}
}
	


