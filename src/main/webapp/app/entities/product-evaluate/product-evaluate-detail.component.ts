import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductEvaluate } from 'app/shared/model/product-evaluate.model';

@Component({
  selector: 'jhi-product-evaluate-detail',
  templateUrl: './product-evaluate-detail.component.html'
})
export class ProductEvaluateDetailComponent implements OnInit {
  productEvaluate: IProductEvaluate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productEvaluate }) => {
      this.productEvaluate = productEvaluate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
