
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * Aktuális deviza árfolyamokat tartalmazó XML fájl feldolgozására szolgáló osztály.
 */
public class XMLFeldolgoz {
    private File         fajlnev;                           //A deviza árfolyamokat tartalmazó XML fájl.
    private List<String> arfolyamlista = new ArrayList<>(); //Ebbe a tömbbe kerül az XML fájl árfolyamokra vonatkozó része.
    private float        hanyFt1EUR;                        //Ebbe a változóba kerül 1 eurónak megfelelő forint összeg.

    public String getFajlnev() {
        return fajlnev.getAbsolutePath();
    }
    
    public File getFajl() {
        return fajlnev;
    }

    public void setFajlnev(File fajlnev) {
        this.fajlnev = fajlnev;
    }

    public float getHanyFt1EUR() {
        return hanyFt1EUR;
    }

    public void setHanyFt1EUR(float hanyFt1EUR) {
        this.hanyFt1EUR = hanyFt1EUR;
    }

    /*
    * Az alábbi metódus feltölti az arfolyamlista tömböt az XML fájl árfolyamokra vonatkozó részével: "VALUTAKÓD - összeg" formában,
    * valamint értéket ad a hanyFt1EUR változónak.
    */
    public List<String> getArfolyamlista(File arfolyamFajl) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(arfolyamFajl);
        doc.getDocumentElement().normalize();        
        NodeList nodeList = doc.getElementsByTagName("Cube");
        
        this.arfolyamlista.add("EUR - 1.0"); //Az EUR nem szerepel a listában, mert ahhoz viszonyít. Mivel forintra történő átváltás érdekel bennünket, ezért ezt is felvesszük a tömbbe.
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element egysor = (Element) nodeList.item(i);
            if ((egysor.getAttribute("currency") != null) && !(egysor.getAttribute("currency").isEmpty())) {
                this.arfolyamlista.add(egysor.getAttribute("currency") + " - " + egysor.getAttribute("rate"));
            }
            
            if ("HUF".equalsIgnoreCase(egysor.getAttribute("currency"))) {
               this.hanyFt1EUR = Float.valueOf(egysor.getAttribute("rate")); 
            }
        }
        return arfolyamlista;
    } 
}
