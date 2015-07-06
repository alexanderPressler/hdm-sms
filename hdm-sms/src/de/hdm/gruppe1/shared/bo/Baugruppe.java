package de.hdm.gruppe1.shared.bo;

/**
 * Realisierung einer exemplarischen Baugruppe. in diesem Fall wird die
 * Baugruppe durch eine Stueckliste erzeugt. Stücklisten stellen die Struktur
 * von Baugruppen dar. In der Stuecklisten Klasse {@Stueckliste } werden die Baugrupppen
 * in Form von eines BaugruppenPaar-Vektors erzeugt. Da eine Baugruppe aus
 * weiteren Komponenten besteht wie Baugruppen oder auch Bauteilen wird es in
 * unseren Fall als in der Baugruppe Klasse doe Sueckliste als Struktur erzeugt.
 * 
 * In eine Aggregation werden die Ganzes-Teile-Beziehungen abgebildet, was in
 * unseren Fall Baugruppen als ganzens betrachtet werden und Klasse Element als
 * Teile davon. Dabei bestehen Baugruppen aus Stücklisten, die ebenfalls von
 * Element erben.
 * 
 * 
 * @author Schmidt & Pressler 
 * 
 */
public class Baugruppe extends Element {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Deklaration der Attribute
	 * Stueckliste
	 * + Setter & Getter
	 */
	private Stueckliste stueckliste;

	public Stueckliste getStueckliste() {
		return this.stueckliste;
	}

	public void setStueckliste(Stueckliste stueckliste) {
		this.stueckliste = stueckliste;
	}  

}