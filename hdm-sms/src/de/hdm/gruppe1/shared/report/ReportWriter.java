package de.hdm.gruppe1.shared.report;


/**
 * <p>
 * Diese Klasse wird benötigt, um auf dem Client die ihm vom Server zur
 * Verfügung gestellten <code>Report</code>-Objekte in ein menschenlesbares
 * Format zu Überführen.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat Überführten Information wird den Subklassen
 * Überlassen. In dieser Klasse werden die Signaturen der Methoden
 * deklariert, die für die Prozessierung der Quellinformation zuständig
 * sind.
 * </p>
 * 
 * @author Thies
 */
public abstract class ReportWriter {
 
    /**
     * Übersetzen eines <code>StuecklisteReport</code> in das Zielformat.
     * 
     * @param r
     *            der zu übersetzende Report
     */
   // public abstract void process(ReportStueckliste r);
 
    /**
     * Übersetzen eines <code>MaterialbedarfReport</code> in das
     * Zielformat.
     * 
     * @param r
     *            der zu übersetzende Report
     */
 //   public abstract void process(MaterialbedarfReport r);
 

}