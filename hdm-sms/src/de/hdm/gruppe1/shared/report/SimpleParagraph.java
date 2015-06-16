package de.hdm.gruppe1.shared.report;


import java.io.Serializable;
 
/**
 * Diese Klasse stellt einzelne AbsÃ¤tze dar. Der Absatzinhalt wird als String
 * gespeichert. Der Anwender sollte in diesem Strinig keinerlei
 * Formatierungssymbole einfÃ¼gen, da diese in der Regel zielformatspezifisch
 * sind.
 * 
 * @author Thies, Thiere
 */
public class SimpleParagraph extends Paragraph implements
        Serializable {
 
    /**
     * TODO
     */
    private static final long serialVersionUID = 1L;
 
    /**
     * Inhalt des Absatzes.
     */
    private String text = "";
 
    /**
     * <p>
     * Serialisierbare Klassen, die mittels GWT-RPC transportiert werden sollen,
     * mÃ¼ssen einen No-Argument-Konstruktor besitzen. Ist kein Konstruktor
     * explizit angegeben, so existiert ini Java-Klassen implizit der
     * Default-Konstruktor, der dem No-Argument-Konstruktor entspricht.
     * </p>
     * <p>
     * Besitzt eine Klasse mind. einen explizit implementierten Konstruktor, so
     * gelten nur diese explizit implementierten Konstruktoren. Der
     * Default-Konstruktor gilt dann nicht. Wenn wir in einer solchen Situation
     * aber dennoch einen No-Argument-Konstruktor benÃ¶tigen, mÃ¼ssen wir diesen
     * wie in diesem Beispiel explizit implementieren.
     * </p>
     * 
     * @see #SimpleParagraph(String)
     */
    public SimpleParagraph() {
    }
 
    /**
     * Dieser Konstruktor ermÃ¶glicht es, bereits bei Instantiierung von
     * <code>SimpleParagraph</code>-Objekten deren Inhalt angeben zu kÃ¶nnen.
     * 
     * @param value
     *            der Inhalt des Absatzes
     * @see #SimpleParagraph()
     */
    public SimpleParagraph(String value) {
        this.text = value;
    }
 
    /**
     * Auslesen des Inhalts.
     * 
     * @return Inhalt als String
     */
    public String getText() {
        return this.text;
    }
 
    /**
     * Ãœberschreiben des Inhalts.
     * 
     * @param text
     *            der neue Inhalt des Absatzes.
     */
    public void setText(String text) {
        this.text = text;
    }
 
    /**
     * Umwandeln des <code>SimpleParagraph</code>-Objekts in einen String.
     */
    public String toString() {
        return this.text;
    }
}