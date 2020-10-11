package retailStore;

import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class InventoryManager {
private HashMap <Integer,String> supplierList;
private ArrayList<Order> orderList;
private Inventory inventory;
public InventoryManager(String supplierFile,String itemFile) {
	supplierList=new HashMap<Integer, String>();
	orderList=new ArrayList<Order>();
	addSupplierList(supplierFile);
	inventory =new Inventory(supplierList);
	addItemsToInventory(itemFile);
}

public String generateOrder(ArrayList<String[]> lis) {
	for(int i=0;i<lis.size();i++) {
		orderList.add(new Order(lis.get(i)));
	}
	StringBuffer sb= new StringBuffer();
	for(int i=0;i<orderList.size();i++) {
		sb.append(orderList.get(i).printOrder()+"\n");
	}
	return sb.toString();
}

public void addSupplierList(String fileName) {
	try {
		BufferedReader reader=new BufferedReader(new FileReader(fileName));
		String line=reader.readLine();
		while (line!=null) {
			String temp[];
			temp=line.split("[;]");
			if (!supplierList.containsKey(Integer.parseInt(temp[0])))
			{
				supplierList.put(Integer.parseInt(temp[0]), temp[1]);
			}
			line=reader.readLine();
		}
		reader.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public void addItemsToInventory(String fileName)  {
	
	try {
		BufferedReader reader=new BufferedReader(new FileReader(fileName));
		String line=reader.readLine();
		while (line!=null) {
			String temp[];
			
			temp=line.split("[;]");
			inventory.addTool(Integer.parseInt(temp[0]), temp[1], Integer.parseInt(temp[2]), Double.parseDouble(temp[3]), Integer.parseInt(temp[4]));
			line=reader.readLine();
}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	public void writeFile(String textOrder) {
		try {
			File file =new File ("Order.txt");
			if(file.exists()) {
				System.err.println("file exists-copying previous order and appending new order");
				BufferedReader reader =new BufferedReader(new FileReader(file));
				String line=reader.readLine();
				String history = null;
				while (line!=null) {
					history+=line+"\n";
					line=reader.readLine();
				}
			FileWriter fw =new FileWriter(file);
			String updated =history +"/n"+textOrder;
			fw.write(updated);
			fw.close();
			}
			else {FileWriter fw =new FileWriter(file);
			fw.write(textOrder);
			fw.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void updateInventory() {
		ArrayList<String[]>temp =inventory.checkStock();
		String file=null;
		if(temp.size()>0) {
//		temp=inventory.checkStock();
		file=generateOrder(temp);
		//either print or generat txt file
//		System.err.println(file);
		}
		if (file!=null) {
			System.err.println("Creating new order, check Order.text file");
			writeFile(file);
		}
	}
	public String searchByName(String name) {
		return inventory.searchToolByName(name);
	}
	public String searchByID(int id) {
		return inventory.searchToolByID(id);
	}
	public void decreaseQuantity(int id,int amount) {
		inventory.decreaseQuantity( id, amount);
	}

	@Override
	public String toString() {
		return inventory.toString();
	}
	public String checkQuatity() {
		return inventory.checkQuantity();
	}
	
}
