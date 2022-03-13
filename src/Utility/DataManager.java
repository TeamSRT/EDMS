/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;


import Model.CartItem;
import Model.Customer;
import Model.Product;
import Model.Supplier;
import UserInterface.ProductUIController;
import java.util.List;

/**
 *
 * @author ktoufiquee
 */
public class DataManager {
    public static Product product;
    public static Customer selected;
    public static Supplier selectedSupplier;
    public static String permission;
    
    public static boolean createProdIsEdit;
    public static int createProductId;
    public static String createProdType;
    public static boolean createProdIsView;
    
    public static ProductUIController prodController;
    public static Product orderSelect;
    
    public static List<CartItem> orderList;
    
}
