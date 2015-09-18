/*package com.zy.servlet31.ocsp.server;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;
import java.security.cert.CertStore;
import java.security.cert.X509CertSelector;
import java.security.cert.CertStoreException;
import java.util.Date;
import java.math.BigInteger;
import java.util.Collection;

import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.ocsp.OCSPResponseStatus;
import org.bouncycastle.asn1.ocsp.RevokedInfo;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;

*//**
This class is responsable for processing OCSP client responses and generating OCSP server responses. All OCSP information is processed and generated as raw
DER encoded data.
*//*
public class ServerOCSPDriver {
	private X509Certificate ServerCertificate=null;
	private PrivateKey ServerPrivateKey=null;
	private CertStore ClientCertStore=null;
	private CertStore AllCertStore=null;
	private CertStore RevokedCertStore=null;
	private Date date=null;
	
	private static boolean initialised=false;
	private static byte [][] badresponses=new byte[5][];
*//**
Identifier to generate a request response, that indicates, that the client OCSP request was not in a proper format.
@see #getBadResponse(int)
*//*
	public static final int MALFORMED_REQUEST=0;
*//**
Identifier to generate a request response, that indicates, that client authentification is required (always the case by this implementation).
@see #getBadResponse(int)
*//*
	public static final int SIGREQUIRED=1;
*//**
Identifier to generate a request response, that indicates, that the client was not able to authenticate itself properly.
@see #getBadResponse(int)
*//*
	public static final int UNAUTHORIZED=2;
*//**
Identifier to generate a request response, that indicates, that the server is too busy at the moment to serve the client.
@see #getBadResponse(int)
*//*
	public static final int TRY_LATER=3;
*//**
Identifier to generate a request response, that indicates, that an internal error occured in the server.
@see #getBadResponse(int)
*//*
	public static final int INTERNAL_ERROR=4;
*//**
Generates an OCSP failure response.
@param status Use one of the public fields of this class to specify the error.
@return A raw DER encoded OCSP server response.
*//*
	public static byte [] getBadResponse(int status) throws OCSPException,IOException {
		if (status<0||status>4)
			throw new OCSPException("Status was not in valid range!");
		if (!initialised) {
			OCSPRespGenerator rg=new OCSPRespGenerator();
			badresponses[MALFORMED_REQUEST]=rg.generate(OCSPRespStatus.MALFORMED_REQUEST,null).getEncoded();
			badresponses[SIGREQUIRED]=rg.generate(OCSPResponseStatus.SIGREQUIRED,null).getEncoded();
			badresponses[UNAUTHORIZED]=rg.generate(OCSPRespStatus.UNAUTHORIZED,null).getEncoded();
			badresponses[TRY_LATER]=rg.generate(OCSPRespStatus.TRY_LATER,null).getEncoded();
			badresponses[INTERNAL_ERROR]=rg.generate(OCSPRespStatus.INTERNAL_ERROR,null).getEncoded();
			initialised=true;
		}
		return badresponses[status];
	}

*//**
Constructs a ServerOCSPDriver object.
@param tmpServerCertificate Certificate, the server should use to authenticate itself to the OCSP client
@param tmpServerPrivateKey Private key, that belongs to the ServerCertificate
@param tmpClientCertStore A certstore, that contains all certificates, that are valid for OCSP clients to authenticate themselves to the OCSP server
@param tmpAllCertStore A certstore, that contains all certificates whose status can be requested by OCSP clients (certificates, that are requested by clients and do not appear here are automatically responded with an unknown status)
@param tmpRevokedCertStore A certstore, that contains all certificates, that are considered to be revoked by the Master CA
@param tmpdate The date, when this configuration data was updated the last time
*//*
	public ServerOCSPDriver(X509Certificate tmpServerCertificate, PrivateKey tmpServerPrivateKey, CertStore tmpClientCertStore, CertStore tmpAllCertStore, CertStore tmpRevokedCertStore,Date tmpdate) throws OCSPException {
		if (tmpServerCertificate==null||tmpServerPrivateKey==null||tmpClientCertStore==null||tmpAllCertStore==null||tmpRevokedCertStore==null||tmpdate==null)
			throw new OCSPException("At least one parameter of ServerOCSPDriver was uninitialised");
		date=tmpdate;
		ServerCertificate=tmpServerCertificate;
		ServerPrivateKey=tmpServerPrivateKey;
		ClientCertStore=tmpClientCertStore;
		AllCertStore=tmpAllCertStore;
		RevokedCertStore=tmpRevokedCertStore;
	}
*//**
Returns the date, when the configuration data for this object was last updated. This value will be enclosed in the generated OCSP server response.
@return date object, that was supplied to the constructor
*//*
	public Date getDate() {
		return date;
	}
*//**
Authenticates the client, finds out the status of the requested certificates and generates an OCSP server response.
@param data The raw DER encoded OCSP client request
@return The raw DER encoded OCSP server response, that is result of processing the supplied OCSP client request with the given configuration data supplied in the constructor of this class
*//*
	public byte [] processRequest(byte [] data) throws OCSPException, NoSuchProviderException,IOException,CertStoreException {
		return processRequest(data,"MD5WITHRSA","BC");
	}
	
*//**
Authenticates the client, finds out the status of the requested certificates and generates an OCSP server response.
@param data The raw DER encoded OCSP client request
@param signingalgorithm The algorithm used to sign the OCSP server responses, default is "MD5WITHRSA"
@param provider The provider used to authenticate the client and to sign the OCSP server responses, default is "BC" (Bouncy Castle)
@return The raw DER encoded OCSP server response, that is result of processing the supplied OCSP client request with the given configuration data supplied in the constructor of this class
*//*
	public byte [] processRequest(byte [] data,String signingalgorithm,String provider) throws OCSPException,NoSuchProviderException,IOException,CertStoreException {
		OCSPReq req;
		try {
			req=new OCSPReq(data);
		}
		catch (Exception e) {
			// data was not an ocsp - response
			return getBadResponse(MALFORMED_REQUEST);	
		}
		if (!req.isSigned())
			return getBadResponse(SIGREQUIRED);	
		X509Certificate [] clientcerts=req.getCerts(provider);
		if (clientcerts==null||clientcerts.length==0)
			return getBadResponse(UNAUTHORIZED);	
		X509CertSelector cs=new X509CertSelector();
		cs.setCertificate(clientcerts[0]);
		Collection matchedclientcerts=ClientCertStore.getCertificates(cs);
		if (matchedclientcerts.size()==0)
			return getBadResponse(UNAUTHORIZED);	
		GeneralName gn=req.getRequestorName();
		if (gn!=null) {
			if (gn.getTagNo()!=4) // tagno of X509DirectotyName
				return getBadResponse(UNAUTHORIZED);	
			X509Name name= new X509Name((ASN1Sequence)gn.getName());
			if (!name.equals(new X509Name(clientcerts[0].getSubjectX500Principal().getName())))
				return getBadResponse(UNAUTHORIZED);	
		}
		if (!req.verify(clientcerts[0].getPublicKey(),provider))
				return getBadResponse(UNAUTHORIZED);	
		Req [] requests= req.getRequestList();
		if (requests==null)
			return getBadResponse(MALFORMED_REQUEST);
		BasicOCSPRespGenerator bg = new BasicOCSPRespGenerator(new RespID(ServerCertificate.getSubjectX500Principal()));
		for (int i=0;i<requests.length;++i) {
			CertificateID certid=requests[i].getCertID();
			X509CertSelector selector=new X509CertSelector();
			BigInteger serialnumber=certid.getSerialNumber();
			selector.setSerialNumber(serialnumber);
			Collection matchedcerts=AllCertStore.getCertificates(selector);
			if (matchedcerts.size()==0) {
				// Certificate is unknown
				bg.addResponse(certid,new UnknownStatus());
				continue;
			}
			if (matchedcerts.size()>1)
				throw new OCSPException ("Found more than one certificate with the same serial number: "+serialnumber);
			if (!certid.equals(new CertificateID(CertificateID.HASH_SHA1,ServerCertificate,serialnumber))) {
				// Certificate is unknown
				bg.addResponse(certid,new UnknownStatus());
				continue;
			}
			X509Certificate proposedCert=(X509Certificate) matchedcerts.iterator().next();
			selector.setCertificate(proposedCert);
			if (RevokedCertStore.getCertificates(selector).size()==0) {
				// Certificate is good
				bg.addResponse(certid,null);
				continue;
			}
			// Certificate is revoked
			bg.addResponse(certid,new RevokedStatus(new RevokedInfo(ASN1GeneralizedTime.getInstance(date),null)));
		}
		// generate BasicOCSPResp
		BasicOCSPResp bresp=null;
		bresp=bg.generate(signingalgorithm,ServerPrivateKey,new X509Certificate[] {ServerCertificate},new Date(),provider);
		OCSPRespGenerator og=new OCSPRespGenerator();
		return og.generate(OCSPRespGenerator.SUCCESSFUL,bresp).getEncoded();
	}
}
*/