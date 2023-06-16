package com.desay.sport.db;

import java.util.ArrayList;
import java.util.List;

public class entity_table
{
	private List<Integer> table_values = null;
	private int data1 ;
	private int data2 ;
	private int data3 ;
	
	public entity_table()
	{
		table_values = new ArrayList<Integer>();
		data1     = 0;
		data2     = 0;
		data3     = 0;
	}
	
	public void clearData()
	{
		table_values.clear();
		data1    = 0;
		data2    = 0;
		data3    = 0;	
	}
	
	public void setTableValue(List<Integer> list) {
		table_values = list;
	}
	public List<Integer> getTableValue() {
		return table_values;
	}
	
	public void setData1(int data1) {
		this.data1 = data1;
	}
	
	public int getData1() {
		return data1;
	}
	public void setData2(int data2) {
		this.data2 = data2;
	}
	
	public int getData2() {
		return data2;
	}
	public void setData3(int data3) {
		this.data3 = data3;
	}
	
	public int getData3() {
		return data3;
	}
}