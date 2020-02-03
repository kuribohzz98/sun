import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpecifications } from 'app/shared/model/specifications.model';

@Component({
  selector: 'jhi-specifications-detail',
  templateUrl: './specifications-detail.component.html'
})
export class SpecificationsDetailComponent implements OnInit {
  specifications: ISpecifications | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specifications }) => {
      this.specifications = specifications;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
