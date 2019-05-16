package com.giant.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giant.demo.entities.*;
import com.giant.demo.enums.StateE;
import com.giant.demo.jobObjects.AfricanAmerican;
import com.giant.demo.jobObjects.Asian;
import com.giant.demo.jobObjects.LatinAmerican;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

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
		/*Job job = new Job(null, 12, 0.5, 0.5, 0.5, 100, new AfricanAmerican(10, 60), new LatinAmerican(5, 60), new Asian(5, 60));
		algorithm.setJob(job);
		algorithm.initializeClusters();
		SimpleClusterGroups simpleClusterGroups = algorithm.graphPartition(algorithm.getClusters());
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("testJson.json"), simpleClusterGroups);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Test
	public void combineClusterTest() {
		//Demographics demographics, StateE state, int[] tempNs, String countyID
		Demographics d1 = new Demographics(0.23, 0.23, 0.13, .32, 0.02, 723);
		Demographics d2 = new Demographics(0.66, 0.14, 0.01, .12, 0.02, 662);
		Precinct p1 = new Precinct(1, "Will Town", 723, 492, 240, 205, null, d1, StateE.NJ, null, "001");
		Precinct p2 = new Precinct(2, "Joe Town", 662, 422, 190, 170, null, d2, StateE.NJ, null, "001");
		List<Precinct> l1 = new ArrayList<>();
		l1.add(p1);
		List<Precinct> l2 = new ArrayList<>();
		l2.add(p2);
		Cluster c1 = new Cluster(1, l1);
		Cluster c2 = new Cluster(2, l2);
		c1.combineCluster(c2);
		Cluster c3 = new Cluster(1, l1);
		c3.addPopulation(662);
		Demographics d3 = new Demographics(.44, .19, .07, .22, .02, c3.getPopulation());
		c3.setDemographics(d3);
		System.out.println(c1.getDemographics());
		Set<Cluster> clusterSet = algorithm.getClusters();
		assertEquals("Incorrect ID", c3.getClusterID(), c1.getClusterID());
		assertEquals("Incorrect Population", c3.getPopulation(), c1.getPopulation());
		d1 = c1.getDemographics();
		assertEquals("Incorrect African American Percent!", d3.getAfricanAmerican(), d1.getAfricanAmerican());
		assertEquals("Incorrect Asian Percent!", d3.getAsian(), d1.getAsian());
		assertEquals("Incorrect Latin American Percent!", d3.getLatinAmerican(), d1.getLatinAmerican());
		assertEquals("Incorrect White Percent!", d3.getWhite(), d1.getWhite());
		assertEquals("Incorrect Other Percent!", d3.getOther(), d1.getOther());
		Iterator<Cluster> iter = clusterSet.iterator();
		c1 = iter.next();
		List<ClusterEdge> edges = c1.getEdges();
		c2 = c1.getEdges().get(0).getCluster2();
		System.out.println("Before c1:\n" + c1.toString());
		System.out.println("Before c2:\n" + c2.toString());
		c3.setPopulation(c1.getPopulation() + c2.getPopulation());
		c3.setClusterID(c1.getClusterID());
		c1.combineCluster(c2);

		System.out.println("After:\n" + c1.toString());
		assertEquals("Incorrect ID", c3.getClusterID(), c1.getClusterID());
		assertEquals("Incorrect Population", c3.getPopulation(), c1.getPopulation());
	}


	@Test
	public void eligibleCluster(){
		State state = new State();
		state.setDistricts(algorithm.getClusters());
		Iterator<Cluster> iter = state.getDistricts().iterator();
		Cluster c1 = iter.next();
		Cluster c2 = state.eligibleCluster(c1);
		assertNotEquals("Null Cluster", null, c2);
		System.out.println("Min pop: " + c2.getPopulation());
		int min = 100000000;
		for(ClusterEdge e : c1.getEdges()){
			int pop = e.getCluster2().getPopulation();
			System.out.println("Pop: " + pop);
			if(min > pop)
				min = pop;
		}
		assertEquals("Incorrect Population: ", min, c2.getPopulation());
	}

	@Test
	public void findClusterPairsTest(){
		Set<Cluster> clusterSet = algorithm.getClusters();
		int total  = 0;
		for(Cluster c : clusterSet){
			total += c.getPopulation();
		}
		Iterator<Cluster> iter = clusterSet.iterator();
		Cluster c1 = iter.next();
		Job job = new Job(null, 12, 0.5, 0.5, 0.5, 100, new AfricanAmerican(2, 6), new LatinAmerican(3, 6), null);
		ClusterEdge edge = c1.findClusterPair(clusterSet.size(), total, job);
		assertNotEquals("Cluster is Null", null, edge);
		System.out.println(c1.toString());
		System.out.println(edge.getCluster2().toString());

	}

	@Test
	public void minPopulationTest(){
		State state = new State();
		state.setDistricts(algorithm.getClusters());
		Cluster c1 = state.minPopulation();
		assertNotEquals("Null Cluster", null, c1);
		System.out.println("Min Population: " + c1.getPopulation());
		System.out.println("Precincts: " + c1.getContainedPrecincts());
	}

}
