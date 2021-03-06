package alde.commons.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.LoggerFactory;

import alde.commons.util.text.StackTraceToString;

/**
 * @author Alde
 *
 * @param <T> object to deserialize on ObjectSerializer(String fileToSaveTo) and to get with get()
 * Always check if isNull() (if the file exists and is not empty) otherwise get() will return null
 */
public class ObjectSerializer<T extends Serializable> {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(FileEditor.class);

	private File file; //file to serialise object to

	private T t;

	public File getFile() {
		return file;
	}

	public void save() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(t);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			log.info("Error while serialising");
			ioe.printStackTrace();
		}
	}

	/**
	 * @param file path of file to create and save data to
	 */
	public ObjectSerializer(File file, T t) {
		this.file = file;
		this.t = t;
	}

	public ObjectSerializer(File file) {
		this.file = file;
	}

	public T get() {

		T t = null;

		if (file.exists() && !(file.length() == 0)) {
			try {
				FileInputStream fis = new FileInputStream(file);

				ObjectInputStream ois = new ObjectInputStream(fis);
				t = (T) ois.readObject();
				ois.close();
				fis.close();

			} catch (IOException | ClassNotFoundException e) {
				log.error(StackTraceToString.sTTS(e));
				e.printStackTrace();
			}
		} else {
			log.warn("File " + file.getAbsolutePath() + " is empty or does not exist!");
		}

		log.info("Returning : " + t);

		return t;

	}

}