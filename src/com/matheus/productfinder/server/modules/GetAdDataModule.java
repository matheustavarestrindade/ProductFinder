package com.matheus.productfinder.server.modules;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matheus.httpapi.HttpModule;
import com.matheus.httpapi.request.Request;
import com.matheus.httpapi.response.Response;
import com.matheus.httpapi.response.ResponseEncoding;
import com.matheus.productfinder.database.ProductModel;

public class GetAdDataModule extends HttpModule {

	public GetAdDataModule(String context) {
		super(context);
	}

	@Override
	public void get(Request req, Response res) {

		req.enableCORS();
		int startOn = req.existQueryParameter("starts") ? Integer.parseInt(req.getQueryParameter("starts")) : 0;
		boolean hot = req.existMultipartParameter("hot") ? true : false;

		ArrayList<ProductModel> models;
		if (req.existQueryParameter("keywords")) {
			models = ProductModel.getProductsByKeyword(startOn, req.getQueryParameter("keywords"));
		} else {
			models = ProductModel.getProducts(hot, startOn);
		}

		JsonArray jsonModels = new JsonArray();

		models.forEach(model -> {
			JsonObject modelJson = new JsonObject();
			modelJson.addProperty("ad_description", model.getAdDescription());
			modelJson.addProperty("ad_id", model.getAdId());
			modelJson.addProperty("thumb_img_name", model.getAdThumbUrl());
			modelJson.addProperty("comment_count", model.getCommentCount());
			modelJson.addProperty("last_update", model.getLastUpdated().toString());
			long last_change = (new Date(System.currentTimeMillis()).getTime() - model.getLastUpdated().getTime()) / 1000;
			modelJson.addProperty("last_change", last_change);
			modelJson.addProperty("link_description", model.getLinkDescription());
			modelJson.addProperty("link_title", model.getLinkTitle());
			modelJson.addProperty("post_link", model.getPostLink());
			modelJson.addProperty("reactions_count", model.getReactionsCount());
			modelJson.addProperty("share_count", model.getShareCount());
			modelJson.addProperty("store_link", model.getStoreLink());
			jsonModels.add(modelJson);

		});
		res.setEncoding(ResponseEncoding.JSON_UTF_8);
		res.sendText(jsonModels.toString());

	}

}
