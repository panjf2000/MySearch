package bin.web.service.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bin.search.web.bo.SearchRequest;
import bin.search.web.bo.SearchResults;
import bin.search.web.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring-*.xml") // 加载配置
public class SearchServiceTest {

	@Resource(name="searchService")
	private SearchService service;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		SearchRequest request = new SearchRequest();
		request.setQuery("迪士尼");request.setStartindex(1);
		SearchResults results = service.getSearchResults(request);
		ArrayList<String> list= results.getResults();
		System.out.println(list.size());
		for (String id : list) {
			System.out.println(id);
		}
		//		fail("Not yet implemented");
	}

}
