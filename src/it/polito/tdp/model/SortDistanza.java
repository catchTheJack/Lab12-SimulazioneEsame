package it.polito.tdp.model;

import java.util.Comparator;
//sort in ordine di distanza dal piu piccolo al piu grande
public class SortDistanza implements Comparator<Vicino>{

	@Override
	public int compare(Vicino o1, Vicino o2) {
		Vicino v1 = o1;
		Vicino v2 = o2;
		// TODO Auto-generated method stub
				return (int)(v1.getDistanza()-v2.getDistanza());
			}
	}
		
