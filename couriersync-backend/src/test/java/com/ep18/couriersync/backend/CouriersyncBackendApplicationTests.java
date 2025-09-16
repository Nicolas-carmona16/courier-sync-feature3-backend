package com.ep18.couriersync.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CouriersyncBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
