package de.hdm.gruppe1.shared.report;

import java.io.Serializable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeViewReport implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	Tree tree = new Tree();
	TreeItem rootTreeItem = new TreeItem();
	TreeItem previousTreeItem = new TreeItem();

	public TreeViewReport(Stueckliste treeViewStueckliste) {
		try {
			Window.alert(treeViewStueckliste.getName());
			treeRecursion(treeViewStueckliste);

			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			tree.addItem(rootTreeItem);

		} catch (Exception e) {
			// System.out.println(e.toString());
		}

	}

	private void treeRecursion(Element element) {
		if (element instanceof Stueckliste) {
			Stueckliste aktuellesStueckliste = (Stueckliste) element;
			rootTreeItem.setText(aktuellesStueckliste.getName());

			for (int i = 0; i < aktuellesStueckliste.getBaugruppenPaare()
					.size(); i++) {
				Element childBaugruppenElement = aktuellesStueckliste
						.getBaugruppenPaare().get(i).getElement();
				treeRecursion(childBaugruppenElement);
			}

			for (int i = 0; i < aktuellesStueckliste.getBauteilPaare().size(); i++) {
				Element childBauteilElement = aktuellesStueckliste
						.getBauteilPaare().get(i).getElement();
				treeRecursion(childBauteilElement);
			}

		}
		if (element instanceof Bauteil) {
			Bauteil aktuellesBauteil = (Bauteil) element;

			TreeItem bauteilTreeItem = new TreeItem();
			bauteilTreeItem.setText(aktuellesBauteil.getName());

		} else {
			// if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			for (int i = 0; i < aktuellesBaugruppe.getStueckliste()
					.getBaugruppenPaare().size(); i++) {

				TreeItem baugruppeTreeItem = new TreeItem();
				baugruppeTreeItem.setText(aktuellesBaugruppe.getStueckliste()
						.getBaugruppenPaare().get(i).getElement().getName());
				previousTreeItem.addItem(baugruppeTreeItem);

				previousTreeItem = baugruppeTreeItem;
				treeRecursion(aktuellesBaugruppe);

				// TODO implementieren
				// baugruppeTreeItem.setText(treeViewStueckliste.getBaugruppenPaare().get(i).getAnzahl()+" * Baugruppe: "+treeViewStueckliste.getBaugruppenPaare().get(i).getElement().getName());
			}
		}
	}

}
