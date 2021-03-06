package org.cognizant.product.controllers;

import java.util.ArrayList;
import java.util.List;

import org.cognizant.product.dto.CategoryDto;
import org.cognizant.product.dto.OfferDto;
import org.cognizant.product.dto.ProductDto;
import org.cognizant.product.entities.Category;
import org.cognizant.product.entities.Offer;
import org.cognizant.product.entities.Product;
import org.cognizant.product.exceptions.OfferAlreadyExistsException;
import org.cognizant.product.exceptions.OfferNotFoundException;
import org.cognizant.product.exceptions.ProductNotFoundException;
import org.cognizant.product.services.OfferService;
import org.cognizant.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offers")
public class OfferController {
	
	@Autowired
	private OfferService offerService;
	@Autowired
	private ProductService productService;
	@GetMapping
	public List<OfferDto> getAllOffers(){
		return convertOffersToOfferDtos(offerService.getAllOffers());
	}
	@GetMapping("/all")
	public List<OfferDto> getAllOffersAdmin() {
		return convertOffersToOfferDtos(offerService.getAllOffersAdmin());
	}
	@GetMapping("/{code}")
	public OfferDto getOfferByProduct(@PathVariable String code) {
		return convertOfferToOfferDto(offerService.getOfferByProduct(code));
	}
	
	@GetMapping("/today/{code}")
	public OfferDto getOfferByProductAndToday(@PathVariable String code) {
		try {
			return convertOfferToOfferDto(offerService.getOfferByProductAndToday(code));
		} catch (Exception e) {
			//e.printStackTrace();
			//System.err.println(e);
			return null;
		}
	}
	
	@PutMapping
	public OfferDto modifyOffer(@RequestBody OfferDto offer) throws ProductNotFoundException {
		return convertOfferToOfferDto(offerService.modifyOffer(convertOfferDtoToOffer(offer)));
	}
	
	@DeleteMapping("/{id}")
	public void deleteOffer(@PathVariable("id") int id) throws OfferNotFoundException {
		offerService.deleteOffer(id);
	}
	
	@PostMapping
	public OfferDto addOffer(@RequestBody OfferDto offer) throws OfferAlreadyExistsException, ProductNotFoundException {
		return convertOfferToOfferDto(offerService.addOffer(convertOfferDtoToOffer(offer)));
	}
	
	public Offer convertOfferDtoToOffer(OfferDto offerDto) throws ProductNotFoundException {
		Product product = productService.getProductById(offerDto.getProductCode());
		Offer offer=new Offer(offerDto.getOfferId(), offerDto.getOfferDate(), offerDto.getDiscountRate(), offerDto.getOfferName(), product);
		return offer;
	}
	
	public Product convertProductDtoToProduct(ProductDto productDto) {
		Category category=new Category(productDto.getCategory().getCategoryId(), productDto.getCategory().getCategoryName());
		Product product=new Product(productDto.getProductCode(), productDto.getProductName(), productDto.getBrand(), productDto.getRate(), productDto.getStockCount(), productDto.getAddDate(), productDto.getAisle(), productDto.getShelf(), productDto.getDateOfManufacture(), productDto.getDateOfExpiry(), productDto.getImage(), category);
		if(product.getQuantityType()!=null)
			productDto.setQuantityType(product.getQuantityType());
		return product;
	}
	
	public ProductDto convertProductToProductDto(Product product) {
		ProductDto productDto=new ProductDto(product.getProductCode(), product.getProductName(), product.getBrand(), product.getRate(),
				product.getStockCount(),product.getAddDate(),product.getAisle(),product.getShelf(),product.getDateOfManufacture(),product.getDateOfExpiry(),
				product.getImage(),null);
		if(product.getQuantityType()!=null)
			productDto.setQuantityType(product.getQuantityType());
		CategoryDto categoryDto=new CategoryDto(product.getCategory().getCategoryId(), product.getCategory().getCategoryName(),null);
		productDto.setCategory(categoryDto);
		return productDto;
	}
	
	public OfferDto convertOfferToOfferDto(Offer offer) {
		OfferDto offerDto=new OfferDto(offer.getOfferId(), offer.getOfferDate(), offer.getDiscountRate(), offer.getOfferName(), offer.getProduct().getProductCode(), offer.getProduct().getProductName());
		return offerDto;
	}
	public List<OfferDto> convertOffersToOfferDtos(List<Offer> offers) {
		List<OfferDto> offerList=new ArrayList<OfferDto>();
		for(Offer offer:offers) {
			OfferDto offerDto=convertOfferToOfferDto(offer);
			offerList.add(offerDto);
		}
		return offerList;
	}
}
