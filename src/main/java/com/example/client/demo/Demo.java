package com.example.client.demo;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.config.Client;

@Service
public class Demo {

	public static final String INDEX = "hotel";

	public static final String TYPE = "hotel";

	/*public static void main(String[] args) throws Exception {
		termsQuery();
	}
*/
	/**
	 * 添加文档
	 * 
	 * @throws IOException
	 */
	public static void save() throws Exception {
		String index = "landmark";
		String type = "landmark";

		IndexResponse response = Client.getClient().prepareIndex(index, type, "i386")
				.setSource(XContentFactory.jsonBuilder().startObject().field("landmark_code", "i386")
						.field("landmark_name", "苏州街").field("landmark_type", "1").field("lng", "121.54")
						.field("lat", "38.96").endObject())
				.get();

		// 如果能正常出来version说明提交成功了
		System.out.println(response.getVersion());
	}

	/**
	 * 批量添加文档
	 * 
	 * @throws Exception
	 */

	public static void batchSave() throws Exception {
		String index = "landmark";
		String type = "landmark";

		BulkRequestBuilder builder = Client.getClient().prepareBulk();

		builder.add(Client.getClient().prepareIndex(index, type, "10086")
				.setSource(XContentFactory.jsonBuilder().startObject().field("landmark_code", "10086")
						.field("landmark_name", "南京路").field("landmark_type", "1").field("lng", "121.54")
						.field("lat", "38.96").endObject()));

		builder.add(Client.getClient().prepareIndex(index, type, "10087")
				.setSource(XContentFactory.jsonBuilder().startObject().field("landmark_code", "10087")
						.field("landmark_name", "四方街").field("landmark_type", "1").field("lng", "121.54")
						.field("lat", "38.96").endObject()));

		BulkResponse bulkResponse = builder.get();

		if (bulkResponse.hasFailures()) {
			// 如果出错这里可以做操作
		}
	}

	/**
	 * 读取文档
	 */
	public static void queryDoc() {

		GetResponse response = Client.getClient().prepareGet(INDEX, TYPE, "4_1000643").execute().actionGet();

		System.out.println(response);
		System.out.println("大家好，才是真的好！！");
	}

	/**
	 * 创建索引 并添加数据
	 * 
	 * @throws Exception
	 */
	public static void createIndex() throws Exception {

		// 创建索引
		CreateIndexRequestBuilder builder = Client.getClient().admin().indices().prepareCreate("book");

		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties")
						.startObject("author").field("type", "text").endObject()
						.startObject("author").field("type", "text").endObject()
						.startObject("author").field("type", "text").endObject()
						.startObject("author").field("type", "text").endObject()
						.startObject("author").field("type", "text").endObject()
					.endObject()
				.endObject();

		builder.addMapping("it", mapping);
		CreateIndexResponse response = builder.execute().actionGet();

		System.out.println("是否成功: " + response.isAcknowledged());

	}

	/**
	 * 删除索引
	 */
	public static void deleteIndex() {

		DeleteIndexResponse response = Client.getClient().admin().indices().prepareDelete("book").execute().actionGet();
		System.out.println("是否成功: " + response.isAcknowledged());
	}

	/**
	 * term查询
	 * @return 
	 */
	
	public static SearchResponse termQuery() {

		TermQueryBuilder builder = QueryBuilders.termQuery("business_area_name", "北京");
		SearchResponse response = Client.getClient().prepareSearch(INDEX).setQuery(builder).get();
		return response;
		/*System.out.println(response);
		System.out.println(response.getHits());*/
	}

	/**
	 * terms查询
	 */
	public static void termsQuery() {
		TermsQueryBuilder build = QueryBuilders.termsQuery("business_area_name", "北京", "上海", "天津");
		SearchResponse response = Client.getClient().prepareSearch(INDEX).setQuery(build).get();
		System.out.println(response);
		System.out.println(response.getHits());
	}

	/**
	 * RangeQuery范围查询
	 */
	public static void rangeQuery() {
		RangeQueryBuilder builder = QueryBuilders.rangeQuery("create_time").gte("2018-1-1 12:00:00")
				.lt("2018-12-20 12:00:00");
		SearchResponse response = Client.getClient().prepareSearch(INDEX).setQuery(builder).get();
		System.out.println(response);
		System.out.println(response.getHits());
	}

	/**
	 * multiMatchQuery
	 */
	public static void multiMatchQuery() {
		MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("北京", "hotel_name", "business_area_name");
		SearchResponse response = Client.getClient().prepareSearch(INDEX).setQuery(builder).setSize(100).get();
		System.out.println(response);
		System.out.println(response.getHits());
	}

	/**
	 * 高亮查询
	 */
	public static void highlightQuery() {
		TermQueryBuilder builder = QueryBuilders.termQuery("business_area_name", "北京");
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<h2 color='red'>");
		highlightBuilder.postTags("</h2>");
		highlightBuilder.field("business_area_name");
		SearchResponse response = Client.getClient().prepareSearch(INDEX).setQuery(builder)
				.highlighter(highlightBuilder).setSize(100).get();
		System.out.println(response);
		System.out.println(response.getHits());

	}

	/**
	 * 查看分词
	 */
	public static void queryAnalyze() {
		// index是索引
		String index = "hotel";
		AnalyzeRequest analyzeRequest = new AnalyzeRequest(index);
		// 添加要查看结果的词汇, 可以任意多个
		analyzeRequest.text("黄焖鸡", "迪斯尼");
		// 添加分词器
		analyzeRequest.analyzer("ik_max_word");

		List<AnalyzeToken> tokens = Client.getClient().admin().indices().analyze(analyzeRequest).actionGet()
				.getTokens();

		for (AnalyzeToken token : tokens) {
			System.out.println("分词结果: " + token.getTerm());
		}
	}

}
