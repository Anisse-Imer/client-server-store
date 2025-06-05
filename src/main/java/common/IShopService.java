package common;

import common.tables.Invoice;
import common.tables.InvoiceDetail;
import common.tables.mainshop.Shop;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import common.tables.mainshop.CProduct;

// Remote Interface defining available methods
public interface IShopService extends Remote {
    boolean insertShop(Shop shop) throws RemoteException;
    boolean insertInvoice(Shop shop, Invoice invoice, List<InvoiceDetail> invoiceDetails) throws RemoteException;
    List<CProduct> getAllProducts() throws RemoteException;
}