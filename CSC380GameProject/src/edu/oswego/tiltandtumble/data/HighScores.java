package edu.oswego.tiltandtumble.data;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.file.FileHandle;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedOutputStream;
import java.lang.ClassNotFoundException;

public class HighScores implements Serializable{
	// TODO: implement this
	
	ArrayList<Integer> scores;

	public HighScore(){
		
		if(Gdx.file.local("Score.dat").exist()){
			try{
				HighScore temp = this.loadFromFile();
				scores = temp.scores;
			}
			catch(IOException e){
				e.printStackTrace();
			}
			catch(ClassNotFoundException e){				
				e.printStackTrace();
			}
			
		}
		else{
			scores = new ArrayList<Interger>();
			
		}
	}
	
	public void compareAndSave(int currentScore){
		
		
		if(scores.size() < 10){
			int j = 0;
			for(; j < scores.size(); j++){
				if(currentScore > scores.get(j)){
					scores.add(j, new Integer(currrentScore));
					j = 11;
				}				
			}
			if(j != 11){
				scores.add(scores.size(), new Integer(currentScore));
			}
			
		}
		else{
			if(currentScore > scores.get(0)){
				scores.add(0, new Integer(currentScore));
				scores.remove(10);
			}
			else{
				for(int i = 1; i < 10; i++){
					
					if(currentScore < scores.get(i-1) && currentScore > scores.get(i)){
						scores.add(i, Integer(currentScore));
						scores.remove(10);
					}
				}
			}
		}
	}
	
	public void saveToFile() throws IOException{
		FileHandle file = Gdx.files.local("Scores.dat");
		OutputStream out= null;
		file.writeBytes(serialize(this), false);
		if(out != null){
			out.close();
		}	
	}
	public HighScore loadFromFile()throws IOException, ClassNotFoundException{
		FileHandle file = Gdx.files.local("Scores.dat");
		return (HighScore) deserialize(file.readBytes());	
	}
		
	private byte[] serialize(Object o)throws IOException{
		
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ObjectOutputStream oOut = new ObjectOutputStream(bOut);
		oOut.writeObject(o);
		return bOut.toByteArray();
		
	}
		
	}
	private Object deserialize(byte[] b)throws IOException, ClassNotFoundException{
		ByteArrayInputStream bIn = new ByteArrayInputStream(b);
		ObjectInputStream oIn = new ObjectInputStream(bIn);
		return oIn.readObject();
	}
	
}

