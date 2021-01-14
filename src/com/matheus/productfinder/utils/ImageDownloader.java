package com.matheus.productfinder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class ImageDownloader {

	private static String BASE_PATH = "./public/assets/thumbs/";

	public static String downloadFileFromURL(String search) {
		try {

			String file_ext = search.contains(".png") ? ".png" : search.contains(".jpeg") ? ".jpeg" : ".jpg";
			String file_name = UUID.randomUUID().toString() + file_ext;

			String path = BASE_PATH + file_name;

			// This will get input data from the server
			InputStream inputStream = null;

			// This will read the data from the server;
			OutputStream outputStream = null;

			// This will open a socket from client to server
			URL url = new URL(search);

			// This user agent is for if the server wants real humans to visit
			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

			// This socket type will allow to set user_agent
			URLConnection con = url.openConnection();

			// Setting the user agent
			con.setRequestProperty("User-Agent", USER_AGENT);

			// Getting content Length
			int contentLength = con.getContentLength();
			System.out.println("Downloading Thumb, Size: " + contentLength + " bytes");

			// Requesting input data from server
			inputStream = con.getInputStream();

			// Open local file writer

			File finalDestination = new File(path);
			if (!finalDestination.exists()) {
				finalDestination.getParentFile().mkdirs();
				finalDestination.createNewFile();
			}

			outputStream = new FileOutputStream(finalDestination);

			// Limiting byte written to file per loop
			byte[] buffer = new byte[2048];

			// Increments file size
			int length;

			// Looping until server finishes
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}

			outputStream.close();
			inputStream.close();
			return file_name;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
