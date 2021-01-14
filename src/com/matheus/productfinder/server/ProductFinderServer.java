package com.matheus.productfinder.server;

import java.io.File;

import com.matheus.httpapi.HttpRestServer;
import com.matheus.productfinder.server.modules.GetAdDataModule;
import com.matheus.productfinder.server.modules.SubmitAdModule;

public class ProductFinderServer {

	private HttpRestServer server;

	public ProductFinderServer(int port) {
		server = new HttpRestServer(port);
		server.registerPublicFileDirectory("/", new File("./public"));
		server.addModule(new SubmitAdModule("/submitad"));
		server.addModule(new GetAdDataModule("/api/getideias"));
	}

}
