

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.hdm.gruppe1.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
// import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.ProvidesKey;
// import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.gruppe1.client.ClientsideSettings;
// import de.hdm.gruppe1.client.BusinessObjectCell;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.Sms;
import de.hdm.gruppe1.shared.bo.*;

import java.util.Vector;
import java.util.List;
// import java.util.Map;
import java.util.TreeMap;


public class BaumstrukturViewModel implements TreeViewModel {
	
	 /**
     * Hier instantiieren wir alle Klassen, auf die der Baum zugreift.
     */
    private BauteilGeneralView bauteilFormular;
    private BaugruppeGeneralView baugruppeFormular;
    private EnderzeugnisGeneralView enderzeugnisFormular;
    private StuecklisteGeneralView stuecklisteFormular;
    private ReportMaterialbedarf rm;
    private ReportStrukturstueckliste rss;
    
    //TODO 
    private CreateBauteil cb;
    private CreateBaugruppe cbg;
    private CreateEnderzeugnis ce;
    private CreateStueckliste cs;

    
    /**
     * Referenz auf das ausgewählte BusinessObjekt-Objekt.
     */

	private User selectedUser = null;
	private Stueckliste selectedStuecklise = null;
	private Baugruppe selectedBaugruppe = null;
	private Bauteil selectedBauteil = null;
	private Enderzeugnis selectedEnderzeugnis = null;
	
	
	 /**
     * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt 
     * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
     */
	
	//TODO
    private SmsAsync sms = GWT
            .create(Sms.class);
     

	
	/**
	 Bildet BusinessObjects auf eindeutige Zahlenobjekte ab, die als
	 * Schlüssel für Baumknoten dienen. Dadurch werden im Selektionsmodell
	 * alle Objekte mit derselben id selektiert, wenn eines davon selektiert
	 * wird. Der Schlüssel für Kundenobjekte ist eine positive, der für
	 * Kontenobjekte eine negative Zahl, die sich jeweils aus der id des
	 * Objektes ergibt. Dadurch ö¶nnen Kunden- von Kontenobjekten
	 * unterschieden werden, auch wenn sie dieselbe id haben.
	 */
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {
	@Override
	public Integer getKey(BusinessObject bo) {
	if (bo == null) {
	return null;
	}
	if (bo instanceof User) {
	return new Integer(bo.getId());
	} else {
	return new Integer(-bo.getId());
	}
	}
	};
	
	private BusinessObjectKeyProvider boKeyProvider = null;
	private SingleSelectionModel<BusinessObject> selectionModel = null;

	
	
	
	/*
	 * Im Konstruktor werden die für den Kunden- und Kontobaum wichtigen
	 * lokalen Variaben initialisiert.
	 */
	public BaumstrukturViewModel() {
		stuecklistenVerwaltung = ClientsideSettings.getStuecklistenVerwaltung();
		boKeyProvider = new BusinessObjectKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEventHandler());
		// accountDataProviders = new HashMap<Customer,
		// ListDataProvider<Account>>();
	}

//	void setUserForm(UserForm uf) {
	//	benutzerFormular = uf;
//	}
//	
//	void setReportMaterialbedarf(ReportMaterialbedarf rm) {
//		stuecklisteFormular = rm;
//	}
//	
//	void setBaugruppeGeneralView(BaugruppeFormular bgf) {
//		baugruppeFormular = bgf;
//	}
//	
//	void setBauteilFormular(BauteilFormular bf) {
//		bauteilFormular = bf;
//	}
	
//	void setEnderzeugnisGeneralView(EnderzeugnisFormular ef) {
//		enderzeugnisFormular = ef;
//	}
	
	User getSelectedUser() {
	return selectedUser;
	}

	 private ListDataProvider<Bauteil> bauteilDataProvider;
	    private ListDataProvider<Baugruppe> lvDataProvider;
	    private ListDataProvider<Enderzeugnis> raumDataProvider;
	    private ListDataProvider<Stueckliste> svDataProvider;
	    private ListDataProvider<String> stringDataProvider;
	 
	    /**
	     * Hier wird die EntryPoint Klasse instantiiert. 
	     */
	    private Sms sps;
	 
	    /**
	     * Hier wird das Selektionsverhalten definiert.
	     * Die Anwendung des {@link ProvidesKey} stellt den Key für die ListItems
	     * bereit, so dass Items, welche als eindeutige Items behandelt werden, eindeutige
	     * Key besitzen.
	     */
	//    private ProvidesKey<Object> boKeyProvider = new ProvidesKey<Object>() {
	         
	        /**
	         * Die Methode <code>getKey()</code> erhält den Key von einem ListItem.
	         * 
	         * @param object als ListItem
	         * @return den Key, welches das Item repräsentiert.
	         */
	        public Integer getKey(Object object) {
	 
	            if (object == null) {
	                return null;
	            }
	 
	            else if (object instanceof String) {
	                return new Integer(((String) object).hashCode());
	            }
	 
	            else if (object instanceof Bauteil) {
	                return new Integer(((Bauteil) object).getId());
	            }
	 
	            else if (object instanceof Baugruppe) {
	                return new Integer(
	                        ((Baugruppe) object).getId());
	            }
	 
	            else if (object instanceof Enderzeugnis) {
	                return new Integer(((Enderzeugnis) object).getId());
	            }
	 
	            else if (object instanceof Stueckliste) {
	                return new Integer(((Stueckliste) object).getId());
	            }
	 
	 
	            else
	                return null;
	        }
	 
	    ;



		@Override
		// Get the NodeInfo that provides the children of the specified value.
		public <T> NodeInfo<?> getNodeInfo(T value) {

			if (value.equals("Root")) {
				// Erzeugen eines ListDataproviders fÃƒÂ¼r Customerdaten
				ListDataProvider<BusinessObject> kategorieDatenProvider = new ListDataProvider<BusinessObject>();

				List<BusinessObject> kategorien = new Vector<BusinessObject>();
				kategorien.add(new Bauteil());
				kategorien.add(new Baugruppe());
				kategorien.add(new Enderzeugnis());
				kategorien.add(new Stueckliste());
				kategorien = kategorieDatenProvider.getList();

				// Return a node info that pairs the data with a cell.
				return new DefaultNodeInfo<BusinessObject>(kategorieDatenProvider,
						new CellTree(), selectionModel, null);
			}
		
			return null;
		}
		
		
/**
 * Check if the value is known to be a leaf node.
 * @return false
 */
@Override
public boolean isLeaf(Object value) {
    return false;
}
}

