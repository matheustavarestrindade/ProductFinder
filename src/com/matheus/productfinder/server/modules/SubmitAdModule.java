package com.matheus.productfinder.server.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matheus.httpapi.HttpModule;
import com.matheus.httpapi.request.Request;
import com.matheus.httpapi.response.Response;
import com.matheus.httpapi.response.ResponseCode;
import com.matheus.productfinder.database.ProductModel;

public class SubmitAdModule extends HttpModule {

	public SubmitAdModule(String context) {
		super(context);
	}

	@Override
	public void post(Request req, Response res) {

		req.enableCORS();

		if (!req.existQueryParameter("ad_data")) {
			res.sendText("Invalid Informations", ResponseCode.BAD_REQUEST);
			return;
		}
		JsonObject data = new JsonParser().parse(req.getQueryParameter("ad_data")).getAsJsonObject();
		String post_link = data.get("postLink").getAsString();

		if (post_link.length() > 255) {
			res.sendCode(ResponseCode.SUCCESS);
			return;
		}
		String thumb_link = data.get("thumbLink").getAsString();
		if (thumb_link.length() > 255) {
			res.sendCode(ResponseCode.SUCCESS);
			return;
		}
		String store_link = data.get("storeLink").getAsString();
		if (store_link.length() > 255) {
			res.sendCode(ResponseCode.SUCCESS);
			return;
		}

		long ad_id = data.get("ad_id").getAsLong();
		long reactions_count = data.get("reactionsCount").isJsonNull() ? 0 : data.get("reactionsCount").getAsLong();
		long share_count = data.get("shareCount").isJsonNull() ? 0 : data.get("shareCount").getAsLong();
		long comment_count = data.get("commentCount").isJsonNull() ? 0 : data.get("commentCount").getAsLong();
		String link_description = data.get("link_description").isJsonNull() ? "" : data.get("link_description").getAsString();
		String link_title = data.get("link_title").isJsonNull() ? "" : data.get("link_title").getAsString();
		String text = data.get("text").isJsonNull() ? "" : data.get("text").getAsString();

		ProductModel.inserProductModel(ad_id, link_description, link_title, post_link, comment_count, reactions_count, share_count, store_link, text, thumb_link);

		res.sendCode(ResponseCode.SUCCESS);
	}

}
