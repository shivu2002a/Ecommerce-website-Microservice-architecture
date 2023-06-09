package com.shivu.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	/* 
	@Bean
	public CommandLineRunner loadData(InventoryRepository invRepo){
		return args -> {
			Inventory inv1 = new Inventory();
			inv1.setQuantity(100);
			inv1.setSkuCode("Samsung_M21");

			Inventory inv2 = new Inventory();
			inv2.setQuantity(10);
			inv2.setSkuCode("MotoRola_G3");

			Inventory inv3 = new Inventory();
			inv3.setQuantity(50);
			inv3.setSkuCode("Redmi_Note8_Pro");

			invRepo.save(inv1);
			invRepo.save(inv2);
			invRepo.save(inv3);
		};
	}

	*/
}
