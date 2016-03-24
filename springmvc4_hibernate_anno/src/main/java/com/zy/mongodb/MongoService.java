package com.zy.mongodb;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

public class MongoService {

	@Autowired
	private GridFsOperations operations;
	
	public byte[] getProtocolImpl(String id) {
		byte[] ret = null;
		InputStream inputStream = operations.findOne(Query.query(GridFsCriteria.where("_id").is(id))).getInputStream();
		try {
			ret =  IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;	
	}
}
