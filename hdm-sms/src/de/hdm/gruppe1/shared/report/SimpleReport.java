package de.hdm.gruppe1.shared.report;


import java.util.Vector;
 
/**
 * <p>
 * Ein einfacher Report, der neben den Informationen der Superklasse <code>
 * Report</code> eine Tabelle mit "Positionsdaten" aufweist. Die Tabelle greift
 * auf zwei Hilfsklassen namens <code>Row</code> und <code>Column</code>
 * zur??ck.
 * </p>
 * <p>
 * Die Positionsdaten sind vergleichbar mit der Liste der Bestellpositionen
 * eines Bestellscheins. Dort werden in eine Tabelle zeilenweise Eintragung z.B.
 * bzgl. Artikelnummer, Artikelbezeichnung, Menge, Preis vorgenommen.
 * </p>
 * 
 * @see Row
 * @see Column
 * @author Thies
 */
public abstract class SimpleReport extends Report {
 
    /**
   * 
   */
    private static final long serialVersionUID = 1L;
 
    /**
     * Tabelle mit Positionsdaten. Die Tabelle wird zeilenweise in diesem
     * <code>Vector</code> abgelegt.
     */
    private Vector<Row> table = new Vector<Row>();
 
    /**
     * Hinzuf??gen einer Zeile.
     * 
     * @param r
     *            die hinzuzuf??gende Zeile
     */
    public void addRow(Row r) {
        this.table.addElement(r);
    }
 
    /**
     * Entfernen einer Zeile.
     * 
     * @param r
     *            die zu entfernende Zeile.
     */
    public void removeRow(Row r) {
        this.table.removeElement(r);
    }
 
    /**
     * Auslesen s??mtlicher Positionsdaten.
     * 
     * @return die Tabelle der Positionsdaten
     */
    public Vector<Row> getRows() {
        return this.table;
    }
}