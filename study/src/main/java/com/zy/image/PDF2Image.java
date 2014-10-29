package com.zy.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PDF2Image {

	public static void main(String[] args) throws Exception {
		PDDocument doc = PDDocument.load("c:\\t1.pdf");
		int pageCount = doc.getNumberOfPages();
		System.out.println(pageCount);
		List pages = doc.getDocumentCatalog().getAllPages();

		String formatName = "jpg";
		String[] pics;

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		pics = new String[pages.size()];

		for (int i = 0; i < pages.size(); i++) {
			System.out.println("-----------------");
			PDPage page = (PDPage) pages.get(i);
			BufferedImage image = page.convertToImage();

			ImageIO.write(image, formatName, output);
			System.out.println(output.size());

			Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
			ImageWriter writer = (ImageWriter) iter.next();
			File outFile = new File("C:/" + i + ".jpg");
			FileOutputStream out = new FileOutputStream(outFile);
			ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
			writer.setOutput(outImage);
			writer.write(new IIOImage(image, null, null));
			pics[i] = outFile.getPath();
		}

	}
}
