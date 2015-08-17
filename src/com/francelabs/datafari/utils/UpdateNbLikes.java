package com.francelabs.datafari.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.francelabs.datafari.startup.LikesLauncher;

public class UpdateNbLikes {
	
	public  static UpdateNbLikes instance;
	public  static CustomProperties properties;
	private final static Logger LOGGER = Logger.getLogger(UpdateNbLikes.class
			.getName());
	public final static String configPropertiesFileName = "external_nbLikes";
	private  static String nbLikesFilePath;
	private static File configFile;
	private static Semaphore semaphore;

	
	public UpdateNbLikes()throws IOException {
        super();
        BasicConfigurator.configure();
        configFile = new File(System.getProperty("catalina.home") + File.separator + ".." + File.separator + "solr" + File.separator +"solr_home" +
        File.separator + "FileShare"+ File.separator + "data"+ File.separator + configPropertiesFileName);
		InputStream stream = new FileInputStream(configFile);
		properties = new CustomProperties();
		try {
			properties.load(stream);
		} catch (IOException e){
			LOGGER.error("Cannot read file : "+ configFile.getAbsolutePath(),e );
			throw e;
		} finally {
			stream.close();
		}
	}
	
	public static UpdateNbLikes getInstance() throws IOException{
		if (instance == null)
			return instance = new UpdateNbLikes();
		return instance;
	}
	
	/**
	 * increment the likes of a document
	 * @param document the id of the document that have to be which likes has to be incremented
	 */
	public static void increment(String document){
		try {
			String nbLikes = (String) UpdateNbLikes.getInstance().properties.get(document);
			if (nbLikes==null){
				UpdateNbLikes.getInstance().properties.setProperty(document,"1");
			}else{
				UpdateNbLikes.getInstance().properties.setProperty(document,String.valueOf(Integer.parseInt(nbLikes)+1));
			}
			saveProperty();
			LikesLauncher.saveChange();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * decrement the likes of a document
	 * @param document the id of the document that have to be which likes has to be decremented
	 */
	public static void decrement(String document){
		try {
			String nbLikes = (String) UpdateNbLikes.getInstance().properties.get(document);
			if (nbLikes==null || Integer.parseInt(nbLikes) <= 0){
				UpdateNbLikes.getInstance().properties.setProperty(document,"0");
			}else{
				UpdateNbLikes.getInstance().properties.setProperty(document,String.valueOf(Integer.parseInt(nbLikes)-1));
			}
			saveProperty();
			LikesLauncher.saveChange();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static void saveProperty(){
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(UpdateNbLikes.configFile);
			UpdateNbLikes.properties.store(fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
	}
	
}