package de.hdm.gruppe1.shared.report;

import java.io.Serializable;
import java.util.Vector;
 
/**
 * Diese Klasse stellt eine Menge einzelner Absätze (
 * <code>SimpleParagraph</code>-Objekte) dar. Diese werden als Unterabschnitte
 * in einem <code>Vector</code> abgelegt verwaltet.
 * 
 * @author Thies,Thiere
 */
public class CompositeParagraph extends Paragraph implements
        Serializable {
 
    /**
   * 
   */
    private static final long serialVersionUID = 1L;
 
    /**
     * Speicherort der Unterabschnitte.
     */
    private Vector<SimpleParagraph> subParagraphs = new Vector<SimpleParagraph>();
 
    /**
     * Einen Unterabschnitt hinzufügen.
     * 
     * @param p
     *            der hinzuzufügende Unterabschnitt.
     */
    public void addSubParagraph(SimpleParagraph p) {
        this.subParagraphs.addElement(p);
    }
 
    /**
     * Einen Unterabschnitt entfernen.
     * 
     * @param p
     *            der zu entfernende Unterabschnitt.
     */
    public void removeSubParagraph(SimpleParagraph p) {
        this.subParagraphs.removeElement(p);
    }
 
    /**
     * Auslesen s??mtlicher Unterabschnitte.
     * 
     * @return <code>Vector</code>, der s??mtliche Unterabschnitte enth??lt.
     */
    public Vector<SimpleParagraph> getSubParagraphs() {
        return this.subParagraphs;
    }
 
    /**
     * Auslesen der Anzahl der Unterabschnitte.
     * 
     * @return Anzahl der Unterabschnitte
     */
    public int getNumParagraphs() {
        return this.subParagraphs.size();
    }
 
    /**
     * Auslesen eines einzelnen Unterabschnitts.
     * 
     * @param i
     *            der Index des gew??nschten Unterabschnitts (0 <= i <n), mit n
     *            = Anzahl der Unterabschnitte.
     * 
     * @return der gew??nschte Unterabschnitt.
     */
    public SimpleParagraph getParagraphAt(int i) {
        return this.subParagraphs.elementAt(i);
    }
 
    /**
     * Umwandeln eines <code>CompositeParagraph</code> in einen
     * <code>String</code>.
     */
    public String toString() {
        /*
         * Wir legen einen leeren Buffer an, in den wir sukzessive s??mtliche
         * String-Repr??sentationen der Unterabschnitte eintragen.
         */
        StringBuffer result = new StringBuffer();
 
        // Schleife ??ber alle Unterabschnitte
        for (int i = 0; i < this.subParagraphs.size(); i++) {
            SimpleParagraph p = this.subParagraphs.elementAt(i);
 
            /*
             * den jew. Unterabschnitt in einen String wandeln und an den Buffer
             * h??ngen.
             */
            result.append(p.toString() + "\n");
        }
 
        /*
         * Schlie??lich wird der Buffer in einen String umgewandelt und
         * zur??ckgegeben.
         */
        return result.toString();
    }
}