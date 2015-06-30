package de.hdm.gruppe1.client;

import java.util.ArrayDeque;
import java.util.Vector;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class Breitensuche {
	private Vector<ElementPaar> liste = new Vector<ElementPaar>();
	private ArrayDeque<ElementPaar> fifo1 = new ArrayDeque<ElementPaar>();
	private ArrayDeque<ElementPaar> fifo2 = new ArrayDeque<ElementPaar>();
	private Stueckliste wurzel = new Stueckliste();
	
	public Breitensuche (Stueckliste ursprung){
		wurzel=ursprung;
		for(int i=0; i<wurzel.getBaugruppenPaare().size();i++){
			fifo1.add(wurzel.getBaugruppenPaare().get(i));
		}
		this.Suchen();
	}
	private void Suchen(){
		while(!fifo1.isEmpty()){
			ElementPaar eP = new ElementPaar();
			eP=fifo1.removeFirst();
			liste.add(eP);
			if(eP.getElement().getClass()==Baugruppe.class){
				Baugruppe bG = (Baugruppe) eP.getElement();
				for(int i=0; i< bG.getStueckliste().getBaugruppenPaare().size(); i++){
					fifo2.add(bG.getStueckliste().getBaugruppenPaare().get(i));
				}
			}
			if(fifo1.isEmpty()){
				fifo1=fifo2;
				fifo2.clear();
			}
		}
	}
}
