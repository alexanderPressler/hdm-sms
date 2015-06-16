package de.hdm.gruppe1.shared.report;


	 
	/**
	 * <p>
	 * Diese Klasse wird benoetigt, um auf dem Client die ihm vom Server zur
	 * Verfuegung gestellten <code>Report</code>-Objekte in ein menschenlesbares
	 * Format zu ueberfuehren.
	 * </p>
	 * <p>
	 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
	 * das Zielformat ueberfuehrten Information wird den Subklassen
	 * ueberlassen. In dieser Klasse werden die Signaturen der Methoden
	 * deklariert, die fuer die Prozessierung der Quellinformation zustaendig
	 * sind.
	 * </p>
	 * 
	 * @author Thies
	 */
	public abstract class ReportWriter {
	 
	    /**
	     * Übersetzen eines <code>MaterialBedarfReport</code> in das Zielformat.
	     * 
	     * @param r
	     *            der zu übersetzende Report
	     */
	    public abstract void process( MaterialBedarfReport r);
	 
	    /**
	     * Übersetzen eines <code>StuecklisteReport</code> in das Zielformat.
	     * 
	     * @param r
	     *            der zu übersetzende Report
	     */
	    public abstract void process(StuecklisteReport r);
	 
	 
	}

