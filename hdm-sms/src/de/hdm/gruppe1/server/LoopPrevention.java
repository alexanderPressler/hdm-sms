package de.hdm.gruppe1.server;
import de.hdm.gruppe1.shared.bo.*;

public class LoopPrevention {
	//Gibt true zur�ck, wenn ein Loop gefunden wird, ansonsten false
	public Boolean checkForLoop (Baugruppe source, Baugruppe toAdd){
		//Checken, ob die Quelle und die hinzuzuf�gende Baugruppe gleich sind
		if(source.getId()==toAdd.getId()){
			return true;
		}
		//Anderenfalls alle Baugruppen in der Stueckliste der hinzuzuf�genden Baugruppe checken
		else{
			for(int i=0; i<toAdd.getStueckliste().getBaugruppenPaare().size();i++){
				//Sich selbst aufrufen, um die Baugruppen der Stueckliste der hinzuzuf�genden Baugruppe
				//mit der Quelle zu vergleichen
				//Wenn true zur�ck kommt, true zur�ck geben, ansonsten weiter mit n�chster Baugruppe
				if(this.checkForLoop(source, (Baugruppe) toAdd.getStueckliste().getBaugruppenPaare().get(i).getElement())){
					return true;
				}
			}
			//Wenn alle Baugruppen gecheckt sind, dann kann es keinen Loop geben
			return false;
		}
	}
}
