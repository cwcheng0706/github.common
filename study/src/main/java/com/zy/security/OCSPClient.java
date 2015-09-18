package com.zy.security;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.CertID;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;

public class OCSPClient {
//	public static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws Exception {
//		// Add provider BC
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//		// Generate the id for the certificate we are looking for
//		CertificateID id = new CertificateID(CertificateID.HASH_SHA1, issuerCert, serialNumber);
//
//		// basic request generation with nonce
//		OCSPReqGenerator gen = new OCSPReqGenerator();
//
//		gen.addRequest(id);
//
//		// create details for nonce extension
//		BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
//		Vector oids = new Vector();
//		Vector values = new Vector();
//
//		oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
//		values.add(new X509Extension(false, new DEROctetString(nonce.toByteArray())));
//
//		gen.setRequestExtensions(new X509Extensions(oids, values));
//
//		return gen.generate();
//	}

	public static void main(String[] args) throws Exception {

		// Read user Certificate
		InputStream inStream = new FileInputStream("C:/oscar/Proyectos/OCSP/veri_viabcp.cer");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate interCert = (X509Certificate) cf.generateCertificate(inStream);
		inStream.close();

		// Read CA Certificate
		InputStream inStreamRoot = new FileInputStream("C:/oscar/Proyectos/OCSP/veri_root.cer");
		X509Certificate rootCert = (X509Certificate) cf.generateCertificate(inStreamRoot);
		inStreamRoot.close();

		
		OCSPReqBuilder gen = new OCSPReqBuilder();
        CertID id = null ;
		CertificateID certId = new CertificateID(id );
		gen.addRequest(certId );

        OCSPReq request = gen.build();
//		OCSPReq request = generateOCSPRequest(rootCert, interCert.getSerialNumber());

		// Codificate request:
		byte[] array = request.getEncoded();

		// Send request:
		// serviceAddr URL OCSP service
		// String serviceAddr="http://ocsp.digsigtrust.com:80/";
		// String serviceAddr="http://ocsp.verisign.com";
		String serviceAddr = "http://onsite-ocsp.verisign.com";

		String hostAddr = "";
		if (serviceAddr != null) {
			hostAddr = serviceAddr;
			try {
				if (serviceAddr.startsWith("http")) {
					HttpURLConnection con = null;
					URL url = new URL((String) serviceAddr);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestProperty("Content-Type", "application/ocsp-request");
					con.setRequestProperty("Accept", "application/ocsp-response");
					con.setDoOutput(true);
					OutputStream out = con.getOutputStream();
					DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
					// Escribo el request
					dataOut.write(array);

					dataOut.flush();
					dataOut.close();

					// Check errors in response:
					if (con.getResponseCode() / 100 != 2) {
						throw new Exception("***Error***");
					}

					// Get Response
					InputStream in = (InputStream) con.getContent();
					org.bouncycastle.cert.ocsp.OCSPResp ocspResponse = new OCSPResp(in);

					/**
					 * ... DECODING THE RESPONSE [2] ...
					 */
					System.out.println(ocspResponse.getStatus());
					System.out.println("...");
				} else {
					// HTTPS
					// HttpsURLConnection
					// ...
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}
}