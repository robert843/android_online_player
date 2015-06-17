package robert843.o2.pl.player;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class MovieParser {
    private String id;
    private String url;
    private int length;
    private String title;
    private String chanelName;
    private String albumUrl;
    JSONObject jObject;


    private void SaveString(String txt) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/json_yt");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "yt-" + n + ".json";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(txt);
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MovieParser(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void parse() throws ParserConfigurationException, JSONException, IOException {
       // System.out.println("Parsowanie filmu id : " + getId());
        try {

            Pattern p = Pattern.compile("ytplayer.config = (.*?);ytplayer.load =");
            Matcher m = p
                    .matcher(getPage(new URL(
                            ("https://www.youtube.com/watch?v=" + getId()))));

            if (m.find()) {
               // Log.d("Parser", m.group(1));
                jObject = new JSONObject(m.group(1));
                setLength(jObject.getJSONObject("args").getInt("length_seconds"));
                if (jObject.getJSONObject("args").has("iurlmaxres")) {
                    setAlbumUrl(jObject.getJSONObject("args").getString("iurlmaxres"));
                } else {
                    setAlbumUrl(jObject.getJSONObject("args").getString("iurl"));
                }
                setChanelName(jObject.getJSONObject("args").getString("author"));
                setTitle(jObject.getJSONObject("args").getString("title"));

                String dashurl = java.net.URLDecoder
                        .decode(jObject.getJSONObject("args").getString("dashmpd"), "UTF-8");
                String dashSrc = getPage(new URL(dashurl));
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc;

               // System.out.println("Pozyskiwanie adresu z YT");
                doc = db.parse(new InputSource(new StringReader(dashSrc)));
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();

                String expression = "//*[@codecs='vorbis']";

                NodeList nodeList = (NodeList) xpath.evaluate(expression, doc,
                        XPathConstants.NODESET);
                setUrl(nodeList.item(0)
                        .getTextContent());
            } else {
                throw new Exception("Brak linków na YT!");
            }
        } catch (Exception e) {
            String mp3Page = getPage(new URL(("http://d.youtube2mp3.cc/check.php?v=" + getId() + "&f=mp3")));
            jObject = new JSONObject(mp3Page);
            String hash = jObject.getString("hash");
            String mp3Url = getPage(new URL(("http://d.youtube2mp3.cc/progress.php?id=" + hash)));
            jObject = new JSONObject(mp3Url);
            setUrl("http://s" + jObject.getString("sid") + ".youtube2mp3.cc/download.php?id=" + hash);
        }
    }

    private String getPage(URL url) throws IOException {

        URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChanelName() {
        return chanelName;
    }

    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }
}
