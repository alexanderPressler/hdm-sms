package de.hdm.gruppe1.shared.report;
import java.io.Serializable;
import java.util.Vector;
 
/**
 * Zeile einer Tabelle eines <code>SimpleReport</code>-Objekts. <code>Row</code>
 * -Objekte implementieren das <code>Serializable</code>-Interface und k??nnen
 * daher als Kopie z.B. vom Server an den Client ??bertragen werden.
 * 
 * @see SimpleReport
 * @see Column
 * @author Thies
 */
public class Row implements Serializable {
 
    /**
   * 
   */
    private static final long serialVersionUID = 1L;
    /**
     * Speicherplatz f??r die Spalten der Zeile.
     */
    private Vector<Column> columns = new Vector<Column>();
 
    /**
     * Hinzuf??gen einer Spalte.
     * 
     * @param c
     *            das Spaltenobjekt
     */
    public void addColumn(Column c) {
        this.columns.addElement(c);
    }
 
    /**
     * Entfernen einer benannten Spalte
     * 
     * @param c
     *            das zu entfernende Spaltenobjekt
     */
    public void removeColumn(Column c) {
        this.columns.removeElement(c);
    }
 
    /**
     * Auslesen s??mtlicher Spalten.
     * 
     * @return <code>Vector</code>-Objekts mit s??mtlichen Spalten
     */
    public Vector<Column> getColumns() {
        return this.columns;
    }
 
    /**
     * Auslesen der Anzahl s??mtlicher Spalten.
     * 
     * @return int Anzahl der Spalten
     */
    public int getNumColumns() {
        return this.columns.size();
    }
 
    /**
     * Auslesen eines einzelnen Spalten-Objekts.
     * 
     * @param i
     *            der Index der auszulesenden Spalte (0 <= i < n), mit n =
     *            Anzahl der Spalten.
     * @return das gew??nschte Spaltenobjekt.
     */
    public Column getColumnAt(int i) {
        return this.columns.elementAt(i);
    }
}