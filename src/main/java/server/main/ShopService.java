package server.main;

import common.IShopService;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ShopService extends UnicastRemoteObject implements IShopService {
    protected ShopService() throws RemoteException {}
    protected ShopService(int port) throws RemoteException { super(port); }
    protected ShopService(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException { super(port, csf, ssf); }
}