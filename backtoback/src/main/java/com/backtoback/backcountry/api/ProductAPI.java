com.backtoback.backcountry.api;

import java.util.ArrayList;
import java.util.List;

import com.backtoback.entities.products.ProductEntity;

public class ProductAPI {
	
	public static List<ProductEntity> getProducts(){
		List<ProductEntity> products = new ArrayList<ProductEntity>();
		products.add(new ProductEntity("ODR0226", "Sun Runner Cap", "/outdoor-research-sun-runner-cap", "/images/items/medium/ODR/ODR0226/WH.jpg"));
		products.add(new ProductEntity("INJ0028", "Run Lightweight Coolmax No-Show Toe Sock", "/injinji-run-lightweight-coolmax-no-show-toe-sock", "/images/items/medium/INJ/INJ0028/NEOYEL.jpg"));
		products.add(new ProductEntity("KAN0012", "Hard Kore Sunglasses - Polarized", "/outdoor-research-sun-runner-cap", "/kaenon-hard-kore-sunglasses-polarized"));
		products.add(new ProductEntity("BUF0013", "UV Headband Buff", "/smith-touchstone-sunglasses-polarchromic", "/images/items/medium/BUF/BUF0013/MOBRINPK.jpg"));
		products.add(new ProductEntity("SMI0680", "Touchstone Polarchromic Sunglasses", "/outdoor-research-sun-runner-cap", "/images/items/medium/SMI/SMI0680/BLACOPMIR.jpg"));
		return products;
	}
}
