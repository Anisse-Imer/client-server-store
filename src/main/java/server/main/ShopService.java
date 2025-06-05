package server.main;

import common.IShopService;
import common.tables.Invoice;
import common.tables.InvoiceDetail;
import common.tables.mainshop.CProduct;
import common.tables.mainshop.Shop;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ShopService extends UnicastRemoteObject implements IShopService {
    protected ShopService() throws RemoteException {}
    protected ShopService(int port) throws RemoteException { super(port); }
    protected ShopService(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException { super(port, csf, ssf); }

    @Override
    public boolean insertShop(Shop shop) {
        return false;
    }
    @Override
    public boolean insertInvoice(Shop shop, Invoice invoice, List<InvoiceDetail> invoiceDetails) {
        return false;
    }
    @Override
    public List<CProduct> getAllProducts() {
        return null;
    }
}