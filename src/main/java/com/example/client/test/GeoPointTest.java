package com.example.client.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.example.client.config.Client;

public class GeoPointTest {

	public static void main(String[] args) {

		double lat = 39.929986;
		double lon = 116.395645;

		// 查询1000米内的hotel
		GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("location").point(lat, lon).distance(1000,
				DistanceUnit.METERS);

		// 排序
		GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("location", new GeoPoint(lat, lon))
				.unit(DistanceUnit.METERS).order(SortOrder.ASC);

		// hotel是索引, 查100个并排序
		SearchResponse response = Client.getClient().prepareSearch("hotel").setQuery(builder).addSort(sortBuilder)
				.setSize(100).get();
		
		 SearchHits hits = response.getHits();
			
		 for(SearchHit hit :hits) {
			 // 离查询地点的距离
			 System.out.println(hit.getSortValues()[0]);
		
		 } 
	}
}
