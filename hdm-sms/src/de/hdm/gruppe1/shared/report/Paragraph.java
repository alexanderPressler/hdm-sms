package de.hdm.gruppe1.shared.report;


import java.io.Serializable;
 
/**
 * Reports benÃ¶tigen die MÃ–glichkeit, Text strukturiert abspeichern zu
 * kÃ¶nnen. Dieser Text kann spÃ¤ter durch <code>ReportWriter</code> in
 * verschiedene Zielformate konvertiert werden. Die Verwendung der Klasse
 * <code>String</code> reicht hier nicht aus, da allein das HinzufÃ¼gen eines
 * Zeilenumbruchs zur Markierung eines Absatzendes Kenntnisse Ã¼ber das
 * Zielformat voraussetzt. Im Falle einer rein textuellen Darstellung wÃ¼rde
 * hierzu ein doppeltes " <code>\n</code>" genÃ¼gen. Bei dem Zielformat HTML
 * mÃ¼sste jedoch der gesamte Absatz in entsprechendes Markup eingefÃ¼gt werden.
 * <p>
 * 
 * <code>Paragraph</code> ist <code>Serializable</code>, so das Objekte dieser
 * Klasse durch das Netzwerk Ã¼bertragbar sind.
 * 
 * @see Report
 * @author Thies
 */
public abstract class Paragraph implements Serializable {
 
    /**
   * 
   */
    private static final long serialVersionUID = 1L;
 
}