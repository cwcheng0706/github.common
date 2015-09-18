/*package com.zy.servlet31.ocsp.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.cert.ocsp.OCSPException;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Collection;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.util.Collection;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;

public class OCSPServlet extends HttpServlet {

	private ServletContext ctx = null;
	private int cachesize = 0;
	private String rmiobject = null;
	private Date date = new Date(0);
	private boolean initialized = false;
	private PrivateKey ServerPrivateKey = null;
	private X509Certificate ServerCertificate = null;
	private OCSPServerParams op = null;
	private CertStore ClientCertStore = null;
	private CertStore AllCertStore = null;
	private CertStore RevokedCertStore = null;
	private boolean caching = true;
	private HashMap map = null;
	private int contentsize = 10000;

	private synchronized boolean getNewData() {
		try {
			CertStore tmpClientCertStore = null;
			CertStore tmpAllCertStore = null;
			CertStore tmpRevokedCertStore = null;
			PrivateKey tmpServerPrivateKey = null;
			X509Certificate tmpServerCertificate = null;
			Collection tmpAllCertificates = null;
			Collection tmpClientCertificates = null;
			Collection tmpRevokedCertificates = null;

			Date tmpdate = op.getDate();
			if (tmpdate == null) {
				ctx.log("Null pointer received as new date!");
				return false;
			}
			// check whether new data is avaliable
			if (date.before(tmpdate)) {
				tmpServerPrivateKey = op.getServerPrivateKey();
				tmpServerCertificate = op.getServerCertificate();
				tmpClientCertificates = op.getClientCertificates();
				tmpAllCertificates = op.getAllCertificates();
				tmpRevokedCertificates = op.getRevokedCertificates();
				// check consistency of new data
				if (tmpServerPrivateKey == null || tmpServerCertificate == null || tmpClientCertificates == null || tmpAllCertificates == null || tmpRevokedCertificates == null) {
					ctx.log("At least one obtained object from RMI-object " + rmiobject + " was a null pointer!");
					return false;
				}
				// create new CertStores
				try {
					tmpClientCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(tmpClientCertificates));
					tmpAllCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(tmpAllCertificates));
					tmpRevokedCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(tmpRevokedCertificates));
				} catch (Exception e) {
					ctx.log("An error occured during generating the new CertStores!");
					return false;
				}
				if (tmpClientCertStore == null || tmpAllCertStore == null || tmpRevokedCertStore == null) {
					ctx.log("An error occured during generating the new CertStores (null - pointers returned)!");
					return false;
				}
				if (caching) {
					ctx.log("New configuration data, clearing the cache ...");
					map.clear();
				}
				// assign values
				ClientCertStore = tmpClientCertStore;
				AllCertStore = tmpAllCertStore;
				RevokedCertStore = tmpRevokedCertStore;
				ServerCertificate = tmpServerCertificate;
				ServerPrivateKey = tmpServerPrivateKey;
				date = tmpdate;
			}
		} catch (Exception e) {
			ctx.log("An error occured during obtaining the data out of the RMI-object " + rmiobject + ": " + e);
			return false;
		}
		return true;
	}

	private synchronized ServerOCSPDriver getDriverObject() throws OCSPException {
		return new ServerOCSPDriver(ServerCertificate, ServerPrivateKey, ClientCertStore, AllCertStore, RevokedCertStore, date);
	}

	private synchronized boolean putinCache(RequestHash request, byte[] response, Date date) {
		if (!date.equals(this.date))
			return false;
		// put the data in the map
		map.put(request, response);
		return true;
	}

	private synchronized byte[] getoutCache(RequestHash request) {
		// get out of cache
		return (byte[]) map.get(request);
	}

	public void init() {
		ctx = getServletContext();
		rmiobject = ctx.getInitParameter("rmi-object");
		if (rmiobject == null) {
			ctx.log("Context - parameter <rmi-object> was not set in web.xml");
			return;
		}
		String CacheSize = ctx.getInitParameter("cache-size");
		if (CacheSize == null) {
			ctx.log("Context - parameter <cache-size> was not set in web.xml");
			return;
		}
		try {
			cachesize = Integer.parseInt(CacheSize);
		} catch (Exception x) {
			ctx.log("Context - parameter <cache-size> in web.xml was no valid number!");
			return;
		}
		if (cachesize < 1) {
			ctx.log("Caching disabled!");
			caching = false;
		} else
			map = new HashMap(cachesize);
		String ContentSize = ctx.getInitParameter("max-content-size");
		if (ContentSize == null) {
			ctx.log("Context - parameter <max-content-size> was not set in web.xml");
			return;
		}
		try {
			contentsize = Integer.parseInt(ContentSize);
		}

		catch (Exception x) {
			ctx.log("Context - parameter <max-content-size> in web.xml was no valid number!");
			return;
		}
		if (contentsize < 1) {
			ctx.log("Max-Content-Size was not in a valid range!");
			return;
		}

		try {
			System.setSecurityManager(new RMISecurityManager());
			op = (OCSPServerParams) Naming.lookup(rmiobject);
		} catch (Exception e) {
			ctx.log("An error occured during obtaining the RMI-object " + rmiobject + ": " + e);
			return;
		}
		initialized = getNewData();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<html><head><title>Novosec OCSPServer V 0.1</title></head>");
		out.println("<body><h1>Welcome to the Novosec OCSPServer V 0.1</h1><br>");
		if (!initialized) {
			out.println("<font color=\"red\"><u>Warning: Your server was not configured in a proper way!<br>Look in the logfile for details!</u></font></body></html>");
			return;
		}
		out.println("<font color=\"green\"><u>This server is configured in a proper way.</u></font><br>Note: You cannot get an OCSP-response via the GET Method!<br>Status: <br>");
		out.println("<br>RMI- object string: " + rmiobject);
		if (!getNewData()) {
			ctx.log("WARNING: An error occured while fetching new configuration data!");
			ctx.log("WARNING: Fall back to last working configuration.");
		}
		out.println("<br>Last update of configuration data: " + date);
		out.println("<br>Max number of bytes in ContentLength: " + contentsize);
		out.println("<br>Caching enabled: " + caching);
		if (caching) {
			out.println("<br>Cachesize (tablesize in internal hash map): " + cachesize);
			out.println("<br>Number of responses in cache: " + map.size());
		}
		out.println("</body></html>");

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		byte[] respdata;
		if (req.getContentType() == null || !req.getContentType().equals("application/ocsp-request")) {
			ctx.log("A non ocsp-request request was received via the POST method!");
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<html><head><title>Novosec OCSPServer V 0.1</title></head>");
			out.println("<body><font color=\"red\"><h1><u>The POST method is reserved only for OCSP - Clients!<br>(ContentType: application/ocsp-request)</u></h1></font><br>Please use the GET method to obtain human readable status information.</body></html>");
			return;
		}

		int len = req.getContentLength();
		if (len < 1 || len > contentsize) {
			ctx.log("Obtained ContentLength was not in valid range!");
			try {
				respdata = ServerOCSPDriver.getBadResponse(ServerOCSPDriver.MALFORMED_REQUEST);
			} catch (OCSPException e) {
				ctx.log("An OCSP-exception occured while generating MALFORMED_REQUEST error response: " + e);
				return;
			}
			writeData(respdata, resp);
			return;
		}

		ServletInputStream reader = req.getInputStream();
		byte[] ocspreqdata = new byte[len];
		int offset = 0;
		int bytes_read;
		while ((bytes_read = reader.read(ocspreqdata, offset, len - offset)) != -1) {
			offset += bytes_read;
			if (offset == len)
				break;
		}

		if (offset != len) {
			ctx.log("Obtained content was shorter than obtained ContentLength!");
			try {
				respdata = ServerOCSPDriver.getBadResponse(ServerOCSPDriver.MALFORMED_REQUEST);
			} catch (OCSPException e) {
				ctx.log("An OCSP-exception occured while generating MALFORMED_REQUEST error response: " + e);
				return;
			}
			writeData(respdata, resp);
			return;
		}
		
		 * This query is after reading out the obtained data, because otherwise,
		 * the Tomcat Server will reset the SSL connection when the first client
		 * attemps to connect
		 
		if (!initialized) {
			ctx.log("Could not process request from client because server is not configured properly!");
			try {
				respdata = ServerOCSPDriver.getBadResponse(ServerOCSPDriver.INTERNAL_ERROR);
			} catch (OCSPException e) {
				ctx.log("An OCSP-exception occured while generating INTERNAL_ERROR error response: " + e);
				return;
			}
			writeData(respdata, resp);
			return;
		}

		// refresh data
		if (!getNewData()) {
			ctx.log("WARNING: An error occured while fetching new configuration data!");
			ctx.log("WARNING: Fall back to last working configuration.");
		}
		// look into the cache
		if (caching) {
			respdata = getoutCache(new RequestHash(ocspreqdata));
			if (respdata == null)
				ctx.log("Response was not found in the cache.");
			else {
				ctx.log("Response was found in the cache.");
				writeData(respdata, resp);
				return;
			}
		}
		// now we have to do the hard work
		try {
			ServerOCSPDriver sd = getDriverObject();
			respdata = sd.processRequest(ocspreqdata);
			writeData(respdata, resp);
			// trying to save response in cache
			if (caching) {
				if (putinCache(new RequestHash(ocspreqdata), respdata, sd.getDate()))
					ctx.log("New response added in cache.");
				else
					ctx.log("Response was too old for cache.");
			}
			return;
		} catch (Exception unexpected) {
			ctx.log("During processing a request, an exception was thrown:" + unexpected);
			try {
				respdata = ServerOCSPDriver.getBadResponse(ServerOCSPDriver.INTERNAL_ERROR);
			} catch (OCSPException e) {
				ctx.log("An OCSP-exception occured while generating INTERNAL_ERROR error response: " + e);
				return;
			}
			writeData(respdata, resp);
			return;
		}
	}

	private void writeData(byte[] respdata, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/ocsp-response");
		resp.setContentLength(respdata.length);
		ServletOutputStream so = resp.getOutputStream();
		so.write(respdata);
	}
}
*/