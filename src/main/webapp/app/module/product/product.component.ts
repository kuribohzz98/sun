import { IProduct } from './../../shared/model/product.model';
import { ProductService } from './../../service/product.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
  styles: []
})
export class ProductComponent implements OnInit {
  private products: IProduct[] = [];
  constructor(private activateRoute: ActivatedRoute, private router: Router, private productSerivce: ProductService) {}

  ngOnInit() {
    // this.productSerivce.query({ page: 0, size: 15 })
  }
}
