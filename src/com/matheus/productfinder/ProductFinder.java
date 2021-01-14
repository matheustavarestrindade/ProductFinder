package com.matheus.productfinder;

import com.matheus.database.database.Database;
import com.matheus.database.database.DatabaseConfig;
import com.matheus.productfinder.server.ProductFinderServer;

public class ProductFinder {

	private static Database db;

	private static String host = "localhost";
	private static String database = "productfinder";
	private static String username = "root";
	private static String password = "DB PASSWORd";

	public static void main(String[] args) {
		db = new Database(DatabaseConfig.builder(host, database, username, password).unicode(true).build());
		db.connect();
		new ProductFinderServer(4040);

	}

	public static Database getDatabase() {
		return db;
	}

}
