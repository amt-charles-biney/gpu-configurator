package com.amalitech.gpuconfigurator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GpuConfiguratorApplicationTests {

	@Test
	void contextLoads() {
	}

	Calculator calc = new Calculator();
	@Test
	void addTwoNumbers(){
		//given
		int a = 5;
		int b = 10;

		//when
		int result = calc.add(a,b);

		//then
		assertThat(result).isEqualTo(15);

	}

}

class Calculator{
	int add(int a, int b){
		return a+b;
	}
}
