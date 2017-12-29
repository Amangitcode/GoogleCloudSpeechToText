package stt.aman.com;
// Imports the Google Cloud client library
import com.google.cloud.speech.v1.RecognitionAudio;

import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class sttMain {
public static void main(String... args) throws Exception {
 

    
	  // Instantiates a client
    SpeechClient speech = SpeechClient.create();

    // The path to the audio file to transcribe
    String fileName = "C:\\Users\\aman0\\Desktop\\ME\\SpeechToTxt\\Audio Samples\\FLAC\\new1.flac";
    //String fileName = "https://storage.googleapis.com/gstt-bucket/medical_spec_29dec2017_4.flac";

    // Reads the audio file into memory
    Path path = Paths.get(fileName);
    byte[] data = Files.readAllBytes(path);
    ByteString audioBytes = ByteString.copyFrom(data);
     
    // Builds the sync recognize request
    RecognitionConfig config = RecognitionConfig.newBuilder()
        .setEncoding(AudioEncoding.FLAC)
        .setSampleRateHertz(44100)
        .setLanguageCode("en-US")
        .build();
    RecognitionAudio audio = RecognitionAudio.newBuilder()
    		.setContent(audioBytes)
    		//.setUri(fileName)
        .build();

    // Performs speech recognition on the audio file
    RecognizeResponse response = speech.recognize(config, audio);
    List<SpeechRecognitionResult> results = response.getResultsList();
    
    String fullTranscription = "";

    for (SpeechRecognitionResult result: results) {
      // There can be several alternative transcripts for a given chunk of speech. Just use the
      // first (most likely) one here.
      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
      System.out.printf("Transcription: %s%n", alternative.getTranscript());
      
      fullTranscription += alternative.getTranscript();

    }
    
    try {
	    	BufferedWriter writer = new BufferedWriter (new FileWriter(".\\test5.txt"));
	    	writer.write(fullTranscription);
	    	writer.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    
    System.out.printf("Full Transcription: %s%n", fullTranscription);
    
    speech.close();
  }
  

}