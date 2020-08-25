package io.bingle.searchapp;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;
//import org.apache.commons.io.IOUtils;
//import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.google.api.client.util.Lists;
//import com.google.api.services.storage.Storage;
//import com.google.api.services.storage.model.Bucket;
import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.datastore.Datastore;
//import com.google.cloud.datastore.DatastoreOptions;
//import com.google.cloud.datastore.Entity;
//import com.google.cloud.datastore.Key;
//import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

//import com.google.cloud.vision.v1.AnnotateImageRequest;
//import com.google.cloud.vision.v1.AnnotateImageResponse;
//import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
//import com.google.cloud.vision.v1.EntityAnnotation;
//import com.google.cloud.vision.v1.Feature;
//import com.google.cloud.vision.v1.Feature.Type;
//import com.google.cloud.vision.v1.Image;
//import com.google.cloud.vision.v1.ImageAnnotatorClient;

//import com.google.protobuf.ByteString;
//import java.util.List;
import java.util.Scanner;
//import java.util.ArrayList;

@Service
public class GoogleOCR {
	// This url specifies which of the Vision APIs to target
	private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
	// This is the key value that allows us to use the API
	private static final String API_KEY = "key=AIzaSyCIKmjZujJh8oie0WenbyXh39Z1DoemhMk";

	// This will be used as a spring boot service so nothing needs to be done in the
	// constructor
	public GoogleOCR() {

	}

	/**
	 * This method is responsible for uploading images onto Google bucket. The
	 * bucket is essentially like cloud storage. The keyfile for this is located as
	 * a json in the project directory.
	 * 
	 * @return{void} This function returns nothing
	 */
	public void upload(MultipartFile file) {
		GoogleCredentials credentials;
		try {
			// Get the credentials information from the provided key file, and specifies for
			// which features to apply the credentials for
			credentials = GoogleCredentials.fromStream(new FileInputStream("CSCC01-Bingle-2c4624d9e1dc.json"))
					.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform",
							"https://www.googleapis.com/auth/cloud-vision"));
			// Opens the Bucket
			Storage storage = (Storage) StorageOptions.newBuilder().setCredentials(credentials).build().getService();

			// Can specify the bucket of choice (in this case we want our premade c01
			// bucket)
			Bucket bucket = storage.get("c01-bingle-bucket");

			// This blob represents the file to be uploaded onto the bucket
			Blob blob = bucket.create(file.getOriginalFilename(), file.getBytes(), file.getContentType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is responsible for uploading images retrieved from the crawler
	 * onto Google bucket. The bucket is essentially like cloud storage. The keyfile
	 * for this is located as a json in the project directory. This accepts slightly
	 * different parameters since that is how the crawler is configured
	 * 
	 * @return{void} This function returns nothing
	 */
	public void upload(byte[] file, String filename, String contentType) {
		GoogleCredentials credentials;
		try {
			credentials = GoogleCredentials.fromStream(new FileInputStream("CSCC01-Bingle-2c4624d9e1dc.json"))
					.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform",
							"https://www.googleapis.com/auth/cloud-vision"));
			Storage storage = (Storage) StorageOptions.newBuilder().setCredentials(credentials).build().getService();
			Bucket bucket = storage.get("c01-bingle-bucket");

			Blob blob = bucket.create(filename, file, contentType);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Retrives the annotation for the provided image. Only works on images that
	 * have already been uploaded to the Google Bucket
	 *
	 * @return{String} Returns the string representation of the API return value.
	 *                 String must be parsed to retrieve annotations
	 */
	public String getAnnotations(MultipartFile file) {
		URL serverUrl;
		try {
			// Need a url for the API call
			serverUrl = new URL(TARGET_URL + API_KEY);

			// Opens up a connection for the API call to take place
			URLConnection urlConnection = serverUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

			// Setting up the request information
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			httpConnection.setDoOutput(true);

			BufferedWriter httpRequestBodyWriter = new BufferedWriter(
					new OutputStreamWriter(httpConnection.getOutputStream()));

			httpRequestBodyWriter.write("{\"requests\":  [{ \"features\":  [ {\"type\": \"LABEL_DETECTION\""
					+ "}], \"image\": {\"source\": { \"gcsImageUri\":" + " \"gs://c01-bingle-bucket/"
					+ file.getOriginalFilename() + "\"}}}]}");
			httpRequestBodyWriter.close();

			String response = httpConnection.getResponseMessage();

			if (httpConnection.getInputStream() == null) {
				System.out.println("No stream");
				return "";
			}

			// Retrieve the output from the API call
			Scanner httpResponseScanner = new Scanner(httpConnection.getInputStream());
			String resp = "";
			while (httpResponseScanner.hasNext()) {
				String line = httpResponseScanner.nextLine();
				resp += line;
				System.out.println(line); // alternatively, print the line of response
			}
			httpResponseScanner.close();
			// Returns the result
			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Retrives the annotation for the provided image. Only works on images that
	 * have already been uploaded to the Google Bucket. This method is to be used
	 * with the crawler to extract tags/annotations
	 *
	 * @return{String} Returns the string representation of the API return value.
	 *                 String must be parsed to retrieve annotations
	 */
	public String getAnnotations(String filename) {
		URL serverUrl;
		try {
			serverUrl = new URL(TARGET_URL + API_KEY);
			URLConnection urlConnection = serverUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			httpConnection.setDoOutput(true);

			BufferedWriter httpRequestBodyWriter = new BufferedWriter(
					new OutputStreamWriter(httpConnection.getOutputStream()));

			httpRequestBodyWriter.write("{\"requests\":  [{ \"features\":  [ {\"type\": \"LABEL_DETECTION\""
					+ "}], \"image\": {\"source\": { \"gcsImageUri\":" + " \"gs://c01-bingle-bucket/" + filename
					+ "\"}}}]}");
			httpRequestBodyWriter.close();

			String response = httpConnection.getResponseMessage();

			if (httpConnection.getInputStream() == null) {
				System.out.println("No stream");
				return "";
			}

			Scanner httpResponseScanner = new Scanner(httpConnection.getInputStream());
			String resp = "";
			while (httpResponseScanner.hasNext()) {
				String line = httpResponseScanner.nextLine();
				resp += line;
				System.out.println(line); // alternatively, print the line of response
			}
			httpResponseScanner.close();

			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

}
