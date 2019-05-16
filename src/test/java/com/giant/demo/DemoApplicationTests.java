package com.giant.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giant.demo.repositories.PrecinctRepository;
import com.giant.demo.returnreceivemodels.SimpleClusterGroups;
import com.giant.demo.services.Algorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {


	@TestConfiguration
	static class AlgorithmTestConfig{

		@Bean
		public Algorithm algorithmBean() {
			return new Algorithm();
		}
	}

	@Autowired
	private Algorithm algorithm;

	@Mock
	private PrecinctRepository precinctRepository;

	@Before
	public void setUp(){
		//Job job = new Job(0.5, 0.5, 0.5,0.5,0.5, 12); /*double demo, double comp, double cont, double pop, double party*/
		//algorithm.setJob(job); /*Store job in algorithm until phase II. */
		algorithm.initializeClusters();


	}

	@Test
	public void GraphPartisionTest(){

		//Job job = new Job(0.5, 0.5, 0.5,0.5,0.5, 12); /*double demo, double comp, double cont, double pop, double party*/
		//algorithm.setJob(job); /*Store job in algorithm until phase II. */
		algorithm.initializeClusters();
		SimpleClusterGroups simpleClusterGroups = algorithm.graphPartition(algorithm.getClusters());
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("testJson.json"), simpleClusterGroups);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void combineClusterTest() {

	}

	@Test
	public void findClusterPairsTest(){

	}


	@Test
	public void JoinabilityTest(){

	}

}
