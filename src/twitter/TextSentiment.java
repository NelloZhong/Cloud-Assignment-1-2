package twitter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextSentiment {
    public Double getScore(String text) {
    	double score = 0;
    	
		String s = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("python", "sentiment.py", "-t", text);
			Process p = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            in.readLine();
            s = in.readLine();
            s = s.replaceAll("[^0-9.-]+", "");
            score = Double.parseDouble(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return score;
    }
}
