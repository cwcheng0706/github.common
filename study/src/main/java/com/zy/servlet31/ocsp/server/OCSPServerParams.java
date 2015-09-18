package com.zy.servlet31.ocsp.server;
		
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.util.Date;

public interface OCSPServerParams extends Remote {
	public  X509Certificate getServerCertificate() throws RemoteException;
	public PrivateKey getServerPrivateKey() throws RemoteException;
	public Collection getClientCertificates() throws RemoteException;
	public Collection getAllCertificates() throws RemoteException;
	public Collection getRevokedCertificates() throws RemoteException;
	public Date getDate() throws RemoteException;
	public void setServerCertificate(X509Certificate ServerCertificate,String token) throws RemoteException;
	public void setServerPrivateKey(PrivateKey ServerPrivateKey,String token) throws RemoteException;
	public void setClientCertificates(Collection ClientCertificates,String token) throws RemoteException;
	public void setAllCertificates(Collection AllCertificates,String token) throws RemoteException;
	public void setRevokedCertificates(Collection RevokedCertificates,String token) throws RemoteException;
	public void setDate(Date date,String token) throws RemoteException;
}
