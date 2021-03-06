import { Component, OnInit, ViewChild } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { Product } from 'src/app/product/product.model';

@Component({
  selector: 'app-manage-products',
  templateUrl: './manage-products.component.html',
  styleUrls: ['./manage-products.component.css']
})
export class ManageProductsComponent implements OnInit {
  products: Product[];
  @ViewChild('modal', {static: false}) 
  modal: any;
  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getAllProducts().subscribe(products => this.products = products);
  }
  delete(productToDelete: Product) {
    this.productService.deleteProduct(productToDelete.productCode).subscribe(() => {
      this.products = this.products.filter(product => product!==productToDelete);
    });
  }
}
