package com.matheus.productfinder.database;

import java.util.ArrayList;
import java.util.Date;

import com.matheus.database.annotation.Column;
import com.matheus.database.model.DatabaseModel;
import com.matheus.database.model.Table;
import com.matheus.database.query.DatabaseFilter;
import com.matheus.database.query.Operator;
import com.matheus.productfinder.ProductFinder;
import com.matheus.productfinder.utils.ImageDownloader;

public class ProductModel extends DatabaseModel {

	private static Table<ProductModel> table = new Table<>(ProductFinder.getDatabase(), "ProductModel", ProductModel.class);
	private static long HOT_PRODUCTS_TIME = 172_800_000;
	static {
		try {
			table.create();
		} catch (Exception e) {
//			e.printStackTrace();
		}

	}

	public static ArrayList<ProductModel> getProductsByKeyword(int starts, String keywords) {

		String[] keyword = keywords.split(" ");
		DatabaseFilter filter = null;
		System.out.println(keywords);
		switch (keyword.length) {
		case 1:
			filter = new DatabaseFilter().where("ad_description", "%" + keyword[0] + "%", Operator.LIKE);
			break;
		case 2:
			filter = new DatabaseFilter().where("ad_description", "%" + keyword[0] + "%", Operator.LIKE).or("ad_description", "%" + keyword[1] + "%", Operator.LIKE);
			break;
		case 3:
			filter = new DatabaseFilter().where("ad_description", "%" + keyword[0] + "%", Operator.LIKE).or("ad_description", "%" + keyword[1] + "%", Operator.LIKE).or("ad_description", "%" + keyword[2] + "%", Operator.LIKE);
			break;
		default:
			return new ArrayList<ProductModel>();
		}
		return table.select().limit(starts * 15, 15).where(filter).execute();
	}

	public static ArrayList<ProductModel> getProducts(boolean hot, int starts) {
		if (hot) {
			DatabaseFilter filter = new DatabaseFilter().where("is_how", true).and("last_updated", new Date(System.currentTimeMillis() - HOT_PRODUCTS_TIME), Operator.GREATER);
			return table.select().where(filter).limit(starts * 15, 15).order("last_updated", "DESC").execute();
		}
		return table.select().limit(starts * 15, 15).order("last_updated", "DESC").execute();
	}

	public static void inserProductModel(long ad_id, String link_description, String link_title, String post_link, long comment_count, long reactions_count, long share_count, String store_link, String ad_description, String ad_thumb_url) {
		ProductModel inserted = table.select().where("ad_id", ad_id).execute().first();
		if (inserted != null) {
			executeUpdate(inserted, ad_id, comment_count, reactions_count, share_count);
			return;
		}

		String image_name = ImageDownloader.downloadFileFromURL(ad_thumb_url);
		table.insert(new ProductModel(ad_id, link_description, link_title, post_link, comment_count, reactions_count, share_count, store_link, ad_description, image_name)).execute();
	}

	private static void executeUpdate(ProductModel inserted, long ad_id, long comment_count, long reactions_count, long share_count) {

		long current_comment_count = inserted.getCommentCount();
		long current_reactions_count = inserted.getReactionsCount();
		long current_share_count = inserted.getShareCount();

		Date last_update_date = inserted.getLastUpdated();
		Date current_time = new Date(System.currentTimeMillis());

		double percentage_comment_count = (comment_count - current_comment_count) / current_comment_count;
		double percentage_reaction_count = (reactions_count - current_reactions_count) / current_reactions_count;
		double percentage_share_count = (share_count - current_share_count) / current_share_count;

		double average_change = (percentage_comment_count + percentage_reaction_count + percentage_share_count) / 3;

		double average_minute_change = average_change / (((current_time.getTime() - last_update_date.getTime()) / 1000) / 60);

		boolean is_hot = false;
		System.out.println(" ==========  ");
		System.out.println("Ad Id = " + ad_id);
		System.out.println("comment_count = " + comment_count);
		System.out.println("current_comment_count = " + current_comment_count);
		System.out.println("reactions_count = " + reactions_count);
		System.out.println("current_reactions_count = " + current_reactions_count);
		System.out.println("share_count = " + share_count);
		System.out.println("current_share_count = " + current_share_count);
		System.out.println("percentage_comment_count = " + percentage_comment_count);
		System.out.println("percentage_reaction_count = " + percentage_reaction_count);
		System.out.println("percentage_share_count = " + percentage_share_count);
		System.out.println("average_change = " + average_change);
		System.out.println("Time Betwenn = " + (((current_time.getTime() - last_update_date.getTime()) / 1000) / 60));
		System.out.println("Average Minute Change = " + average_minute_change);

		// Maior que 20% por minuto
		if (average_minute_change > 0.2) {
			is_hot = true;
		}

		table.update().where("ad_id", ad_id).set("last_updated", current_time).set("comment_count", comment_count).set("reactions_count", reactions_count).set("share_count", share_count).set("is_hot", is_hot).execute();
	}

	@Column(primaryKey = true)
	private long ad_id;

	@Column
	private String link_description;

	@Column
	private String link_title;

	@Column
	private String post_link;

	@Column
	private long comment_count;

	@Column
	private long reactions_count;

	@Column
	private long share_count;

	@Column
	private String store_link;

	@Column(type = "TEXT")
	private String ad_description;

	@Column
	private String ad_thumb_url;

	@Column
	private Date last_updated;

	@Column
	private boolean is_hot;

	public ProductModel() {
	}

	private ProductModel(long ad_id, String link_description, String link_title, String post_link, long comment_count,
																long reactions_count, long share_count,
																String store_link, String ad_description,
																String ad_thumb_url) {
		this.ad_id = ad_id;
		this.link_description = link_description;
		this.link_title = link_title;
		this.post_link = post_link;
		this.comment_count = comment_count;
		this.reactions_count = reactions_count;
		this.share_count = share_count;
		this.store_link = store_link;
		this.ad_description = ad_description;
		this.ad_thumb_url = ad_thumb_url;
		this.last_updated = new Date(System.currentTimeMillis());
		this.is_hot = false;
	}

	public static Table<ProductModel> getTable() {
		return table;
	}

	public long getAdId() {
		return ad_id;
	}

	public String getLinkDescription() {
		return link_description;
	}

	public String getLinkTitle() {
		return link_title;
	}

	public String getPostLink() {
		return post_link;
	}

	public long getCommentCount() {
		return comment_count;
	}

	public long getReactionsCount() {
		return reactions_count;
	}

	public long getShareCount() {
		return share_count;
	}

	public String getStoreLink() {
		return store_link;
	}

	public String getAdDescription() {
		return ad_description;
	}

	public String getAdThumbUrl() {
		return ad_thumb_url;
	}

	public Date getLastUpdated() {
		return last_updated;
	}

	public boolean ishot() {
		return is_hot;
	}

}
