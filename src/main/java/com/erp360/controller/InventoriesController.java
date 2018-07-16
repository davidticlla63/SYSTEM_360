package com.erp360.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Producto;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 * 
 */

@ManagedBean(name = "inventoriesController")
@ViewScoped
public class InventoriesController implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private @Inject AlmacenProductoDao inventoryFacade;
	private @Inject ProductoDao productFacade;
	private @Inject SessionMain sessionMain;

	private TreeNode rootProduct;
	private TreeNode rootwarehouses;
	
	@PostConstruct
	public void initInventoriesController() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
		List<Producto> prod= new ArrayList<>();
		List<AlmacenProducto> inv= new ArrayList<>();
		prod = productFacade.findAllOrderedByDescripcion();
		int i = 0;	
		List<EDProductTree> edProducTrees=new ArrayList<>();
		
		for (Producto prod2 : prod) {
			
			EDProductTree edProductTree= new EDProductTree();
			List<EDWarehouseTree> EDWarehouseTrees=new ArrayList<>();
			inv = inventoryFacade.findAllAlmacenProducto(sessionMain.getGestionLogin(),prod2);
			i = 0;	
			edProductTree.setName(prod2.getNombre());
			for (AlmacenProducto inventory : inv) {	
				if( i != inventory.getAlmacen().getId() ){
					EDWarehouseTree EDWarehouseTree=new EDWarehouseTree();
					List<EDInventory> edInventories=new ArrayList<>();
					i = inventory.getAlmacen().getId();
					List<AlmacenProducto> inv2 = filtrarPorAlmacen(i,inv);	
					EDWarehouseTree.setName(inv2.get(0).getAlmacen().getNombre());
					for (AlmacenProducto inventory2 : inv2) {
						EDInventory edInventory=new EDInventory();
						edInventory.setB_NameProduct(inventory2.getProducto().getNombre());
						edInventory.setC_Date((inventory2.getFechaRegistro()!=null)? String.valueOf(dateFormat.format( inventory2.getFechaRegistro())):"-");
						edInventory.setD_ExpireDate((inventory2.getFechaExpiracion()!=null)? String.valueOf(dateFormat.format(inventory2.getFechaExpiracion())) :"-");
						edInventory.setE_NroLote((inventory2.getNumeroLote()!=null)? inventory2.getNumeroLote():"-");
						edInventory.setE1_PhysicalLocation((inventory2.getUbicacionFisica()!=null)? inventory2.getUbicacionFisica():"-");
						edInventory.setG_OnHand((inventory2.getStock())!=0 ? String.valueOf(inventory2.getStock()):"0");
						edInventory.setA_NameParent("");
						edInventories.add(edInventory);
					}
					EDWarehouseTree.setInventories(edInventories);
					EDWarehouseTrees.add(EDWarehouseTree);
				}
			}
			edProductTree.setWarehouses(EDWarehouseTrees);
			edProducTrees.add(edProductTree);	
		}
		rootProduct=treeNonde(edProducTrees);
	}
	
	private List<AlmacenProducto> filtrarPorAlmacen(final int i,List<AlmacenProducto> inventarios) {
		//List<AlmacenProducto> inv2 = Stream.of(inventarios).filter(x -> x.getAlmacen().getId() == i).collect(Collectors.toList());	
		List<AlmacenProducto> inv2 = Stream.of(inventarios).filter(new 
				 Predicate<AlmacenProducto>() {
				@Override
				public boolean test(AlmacenProducto s) {
					return s.getAlmacen().getId() == i;
				}
			}).toList();
		
		/*for(AlmacenProducto inventario : inventarios){
			if(inventario.getAlmacen().getId() == i){
				inv2.add(inventario);
			}
		}*/
		return inv2;
	}

	public TreeNode treeNonde(List<EDProductTree> edProducTrees) {
		  rootProduct = new DefaultTreeNode(new EDInventory(), null);
		  for (EDProductTree edProductoTree : edProducTrees) {
			  TreeNode node=new DefaultTreeNode( new EDInventory(edProductoTree.getName(),"","","","","","",""), rootProduct);
				node.setExpanded(true);
				node.setSelected(false);
				for (EDWarehouseTree edWarehouse : edProductoTree.getWarehouses()) {
					TreeNode nodeChild=new DefaultTreeNode( new EDInventory(edWarehouse.getName(),"","","","","","",""), node);
					nodeChild.setExpanded(true);
					nodeChild.setSelected(false);
					for (EDInventory edInv : edWarehouse.getInventories()) {
						TreeNode nodeChild2=new DefaultTreeNode( new EDInventory(" ",edInv.getB_NameProduct(),edInv.getC_Date(),edInv.getD_ExpireDate(),
																					edInv.getE_NroLote(),edInv.getE1_PhysicalLocation(),edInv.getF_Reserved(),edInv.getG_OnHand()),nodeChild);
						nodeChild2.setExpanded(true);
						nodeChild2.setSelected(false);
					}
				}
		}
		  return rootProduct;
	}
	
	public TreeNode getRootProduct() {
		return rootProduct;
	}

	public void setRootProduct(TreeNode rootProduct) {
		this.rootProduct = rootProduct;
	}

	public TreeNode getRootwarehouses() {
		return rootwarehouses;
	}

	public void setRootwarehouses(TreeNode rootwarehouses) {
		this.rootwarehouses = rootwarehouses;
	}


	public class EDInventory implements Serializable{
		
		private String a_NameParent;
		private String b_NameProduct;
		private String c_Date;
		private String d_ExpireDate;
		private String e_NroLote;
		private String e1_PhysicalLocation;
		private String f_Reserved;
		private String g_OnHand;
		
		
		
		public EDInventory() {
		
		}



		public EDInventory(String a_NameParent, String b_NameProduct, String c_Date, String d_ExpireDate,
				String e_NroLote,String e1_PhysicalLocation, String f_Reserved, String g_OnHand) {
			super();
			this.a_NameParent = a_NameParent;
			this.b_NameProduct = b_NameProduct;
			this.c_Date = c_Date;
			this.d_ExpireDate = d_ExpireDate;
			this.e_NroLote = e_NroLote;
			this.e1_PhysicalLocation = e1_PhysicalLocation;
			this.f_Reserved = f_Reserved;
			this.g_OnHand = g_OnHand;
		}



		public String getA_NameParent() {
			return a_NameParent;
		}



		public void setA_NameParent(String a_NameParent) {
			this.a_NameParent = a_NameParent;
		}



		public String getB_NameProduct() {
			return b_NameProduct;
		}



		public void setB_NameProduct(String b_NameProduct) {
			this.b_NameProduct = b_NameProduct;
		}



		public String getC_Date() {
			return c_Date;
		}



		public void setC_Date(String c_Date) {
			this.c_Date = c_Date;
		}



		public String getD_ExpireDate() {
			return d_ExpireDate;
		}



		public void setD_ExpireDate(String d_ExpireDate) {
			this.d_ExpireDate = d_ExpireDate;
		}



		public String getE_NroLote() {
			return e_NroLote;
		}



		public void setE_NroLote(String e_NroLote) {
			this.e_NroLote = e_NroLote;
		}



		public String getF_Reserved() {
			return f_Reserved;
		}



		public void setF_Reserved(String f_Reserved) {
			this.f_Reserved = f_Reserved;
		}



		public String getG_OnHand() {
			return g_OnHand;
		}



		public void setG_OnHand(String g_OnHand) {
			this.g_OnHand = g_OnHand;
		}



		public String getE1_PhysicalLocation() {
			return e1_PhysicalLocation;
		}



		public void setE1_PhysicalLocation(String e1_PhysicalLocation) {
			this.e1_PhysicalLocation = e1_PhysicalLocation;
		}

		
		
	}
	
	public class EDProductTree implements Serializable{
		private String name;
		private List<EDWarehouseTree> warehouses;
		
		public EDProductTree() {
			
		}

		public EDProductTree(String nameProduct, List<EDWarehouseTree> warehouses) {
			super();
			this.name = nameProduct;
			this.warehouses = warehouses;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<EDWarehouseTree> getWarehouses() {
			return warehouses;
		}

		public void setWarehouses(List<EDWarehouseTree> warehouses) {
			this.warehouses = warehouses;
		}

		
	
	}

	public class EDWarehouseTree implements Serializable {

		private String name;
		private List<EDInventory> inventories;
		
		public EDWarehouseTree() {
		}

		

		public EDWarehouseTree(String nameWarehouse, List<EDInventory> inventories) {
			super();
			this.name = nameWarehouse;
			this.inventories = inventories;
		}



		public String getName() {
			return name;
		}



		public void setName(String name) {
			this.name = name;
		}



		public List<EDInventory> getInventories() {
			return inventories;
		}



		public void setInventories(List<EDInventory> inventories) {
			this.inventories = inventories;
		}


	}

}



