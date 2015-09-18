/*package com.zy.servlet31.ocsp.server;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.*;
import java.util.Arrays;
import java.util.Vector;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPRequest;
import org.bouncycastle.asn1.ocsp.OCSPResponseStatus;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;

// OcspHelper
public class OcspHelper {
	public enum CertificateStatus {
		Good, Revoked, Unknown
	}

	class OcspHelperException extends Exception {
		public OcspHelperException(String msg) {
			super(msg);
		}
	}

	BigInteger m_serialNumber;
	X509Certificate m_issuerCert;

	byte[] m_nonce;
	OCSPResp m_response;

	// Constructor
	public OcspHelper(byte[] certData, byte[] issuerData) throws CertificateException, OCSPException, IOException {
		m_serialNumber = makeX509Cert(certData).getSerialNumber();
		m_issuerCert = makeX509Cert(issuerData);
	}

	public OcspHelper() {
		// TODO Auto-generated constructor stub
	}

	static private X509Certificate makeX509Cert(byte[] data) throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is = new ByteArrayInputStream(data);
		return (X509Certificate) cf.generateCertificate(is);
	}

	static private X509Extensions nonceExtensions() {
		BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
		Vector<DERObjectIdentifier> oids = new Vector<DERObjectIdentifier>();
		Vector<X509Extension> values = new Vector<X509Extension>();

		oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
		values.add(new X509Extension(false, new DEROctetString(nonce.toByteArray())));

		X509Extensions ret = new X509Extensions(oids, values);
		return ret;
	}

	static OCSPReq generateRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		OCSPReqGenerator gen = new OCSPReqGenerator();
		OCSPReqBuilder
		gen.addRequest(new CertificateID(CertificateID.HASH_SHA1, issuerCert, serialNumber));
		gen.setRequestExtensions(nonceExtensions());

		return gen.generate();
	}

	static SingleResp findResponse(SingleResp[] responses, CertificateID certID) {
		for (int i = 0; i != responses.length; i++) {
			if (responses[i].getCertID().equals(certID)) {
				return responses[i];
			}
		}
		return null;
	}

	static private boolean verifyChain(X509Certificate[] certs, X509Certificate rootCert) {
		assert (certs.length > 0);

		try {
			// Verify all the certificates in the chain except the last one
			for (int i = 0; i < certs.length - 1; i++) {
				X509Certificate issuer = certs[i + 1];
				certs[i].verify(issuer.getPublicKey());
			}
			// Verify last certificate against rootCert
			certs[certs.length - 1].verify(rootCert.getPublicKey());
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public CertificateStatus getCertificateStatus() throws OCSPException, NoSuchProviderException, OcspHelperException {
		BasicOCSPResp basicResponse = (BasicOCSPResp) m_response.getResponseObject();

		CertificateID certID = new CertificateID(CertificateID.HASH_SHA1, m_issuerCert, m_serialNumber);
		SingleResp response = findResponse(basicResponse.getResponses(), certID);
		if (response == null) {
			return CertificateStatus.Unknown;
		}

		Object status = response.getCertStatus();
		if (status instanceof org.bouncycastle.ocsp.RevokedStatus) {
			return CertificateStatus.Revoked;
		} else if (status instanceof org.bouncycastle.ocsp.UnknownStatus) {
			return CertificateStatus.Unknown;
		} else {
			return CertificateStatus.Good;
		}
	}

	private OCSPResp sendHttpRequest(URL url, OCSPReq req) throws OCSPException, IOException {
		// Save nonce for later verification
		m_nonce = req.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId());

		// Prepare HTTP connection
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Content-Type", "application/ocsp-request");
		con.setRequestProperty("Accept", "application/ocsp-response");
		con.setDoOutput(true);

		// Send the request
		DataOutputStream os = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
		os.write(req.getEncoded());
		os.flush();
		os.close();

		// Read back the response
		InputStream in = (InputStream) con.getContent();
		return new OCSPResp(in);
	}

	public void sendRequest(String responderUrl, OCSPReq req) throws OCSPException, IOException, OcspHelperException {
		URL url = new URL(responderUrl);
		String protocol = url.getProtocol();
		if (protocol.equals("http")) {
			m_response = sendHttpRequest(url, req);
		} else {
			throw new OcspHelperException("Protocol " + protocol + " not supported");
		}

		int ocspRespStatus = m_response.getStatus();
		if (ocspRespStatus != OCSPResponseStatus.SUCCESSFUL) {
			throw new OcspHelperException("OCSP response unsuccessful: " + ocspRespStatus);
		}
	}

	public void sendRequest(String responderUrl) throws OCSPException, IOException, OcspHelperException {
		// Generate request
		OCSPReq req = generateRequest(m_issuerCert, m_serialNumber);

		sendRequest(responderUrl, req);
	}

	public boolean verifyResponseWithResponderCert(byte[] responderCertData) throws CertificateException, NoSuchProviderException, OCSPException, OcspHelperException {
		X509Certificate responderCert = makeX509Cert(responderCertData);
		if (responderCert == null) {
			throw new OcspHelperException("Unable to verify OCSP server response: no responder certificate");
		}

		BasicOCSPResp basicResponse = (BasicOCSPResp) m_response.getResponseObject();

		// XXX: for reasons unknown nonce verification fails on ocsp.godaddy.com
		byte[] respNonce = basicResponse.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId());
		if (Arrays.equals(m_nonce, respNonce)) {
			System.err.println("nonce verified OK");
		} else {
			System.err.println("nonce verification failed");
		}

		if (!basicResponse.verify(responderCert.getPublicKey(), "BC")) {
			return false;
		}

		return true;
	}

	public boolean verifyResponseWithRootCert(byte[] rootCertData) throws CertificateException, NoSuchProviderException, OCSPException, OcspHelperException {
		X509Certificate rootCert = makeX509Cert(rootCertData);
		if (rootCert == null) {
			throw new OcspHelperException("Unable to verify OCSP server response: no root certificate");
		}

		BasicOCSPResp basicResponse = (BasicOCSPResp) m_response.getResponseObject();

		// XXX: for reasons unknown nonce verification fails on ocsp.godaddy.com
		byte[] respNonce = basicResponse.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId());
		if (Arrays.equals(m_nonce, respNonce)) {
			System.err.println("nonce verified OK");
		} else {
			System.err.println("nonce verification failed");
		}

		X509Certificate[] ocspCerts = basicResponse.getCerts("BC");
		if (ocspCerts.length == 0) {
			throw new OcspHelperException("Unable to verify OCSP server response: no certificates in response");
		}

		// Verify OCSP response signature
		X509Certificate responderCert = ocspCerts[0];
		if (!basicResponse.verify(responderCert.getPublicKey(), "BC")) {
			return false;
		}

		// Verify certificate chain from the response
		if (!verifyChain(ocspCerts, rootCert)) {
			return false;
		}

		return true;
	}

	public byte[] getResponseData() throws IOException {
		return m_response.getEncoded();
	}
}*/