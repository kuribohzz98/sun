import { IProductType } from './../../shared/model/product-type.model';
import { ProductTypeService } from './../../service/product-type.service';
import { takeUntil, map, tap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { IPayment } from './../../shared/model/payment.model';
import { PaymentService } from './../../service/payment.service';
import { OnInit, Component, OnDestroy } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Label, Color } from 'ng2-charts';
import { FormControl } from '@angular/forms';
import * as _ from 'lodash';
import * as moment from 'moment';

@Component({
  selector: 'jhi-statistical',
  templateUrl: './statistical.component.html'
})
export class StatisticalComponent implements OnInit, OnDestroy {
  public lineChartData: ChartDataSets[] = [{ data: [], label: 'nghìn (VNĐ)' }];
  public lineChartLabels: Label[] = [];
  public lineChartOptions: ChartOptions = {
    responsive: true
  };
  public lineChartColors: Color[] = [
    {
      borderColor: 'black',
      backgroundColor: 'rgba(255,0,0,0.3)'
    }
  ];
  public lineChartLegend = true;
  public lineChartType: ChartType = 'bar';
  public lineChartPlugins = [];
  public months: any[] = [];
  public typeFilter: any[] = [];
  public typeFilterControl: FormControl = new FormControl('');
  public productTypeControl: FormControl = new FormControl('');
  public totalProfit: number = 0;
  public totalPayment: number = 0;
  public payments: IPayment[] = [];
  public productTypes: IProductType[] = [];

  private destroy$: Subject<boolean> = new Subject();

  constructor(private readonly paymentService: PaymentService, private readonly productTypeService: ProductTypeService) {}

  ngOnInit(): void {
    this.initTypeFilter();
    this.productTypeService.query().subscribe(res => {
      if (res.body) {
        this.productTypes = [{ id: 0, name: 'Không' }, ...res.body];
        this.productTypeControl.patchValue(this.productTypes[0].id);
      }
    });
    this.paymentService
      .query()
      .pipe(
        tap(res => {
          if (res.body)
            return res.body.map(payment => {
              if (payment.description) payment.description = payment.description.replace(/,}/g, '}');
              return payment;
            });
          return res;
        })
      )
      .subscribe(res => {
        this.payments = res.body || [];
        console.log(res);
        this.initNotMonth();
      });

    this.typeFilterControl.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(value => {
      this.initNotMonth();
    });

    this.productTypeControl.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(value => {
      this.initNotMonth();
    });
  }

  initNotMonth(): void {
    this.initDataChartLine();
    this.initMonths();
  }

  initMonths(): void {
    this.lineChartLabels = [];
    for (let i = 0; i < 13; i++) {
      i && this.lineChartLabels.push('Tháng ' + i);
      if (i && this.lineChartData[0].data && !this.lineChartData[0].data[i]) this.lineChartData[0].data[i] = 0;
    }
  }

  initTypeFilter(): void {
    this.typeFilter = [
      {
        name: 'Thu nhập',
        value: 0
      },
      {
        name: 'Lợi nhuận',
        value: 1
      },
      {
        name: 'Số lượng bán',
        value: 2
      }
    ];
    this.typeFilterControl.patchValue(this.typeFilter[0].value, { emitEvent: false });
  }

  initDataChartLine(): void {
    if (this.typeFilterControl.value != 2) this.lineChartData = [{ data: [], label: 'nghìn (VNĐ)' }];
    else this.lineChartData = [{ data: [], label: 'sản phẩm' }];

    const data = _(this.payments)
      .groupBy(x => moment(x.createdAt).month())
      .value();
    Object.keys(data).map(key => {
      if (this.lineChartData[0].data) {
        if (this.typeFilterControl.value == 0) {
          this.lineChartData[0].data[+key] = data[key].reduce((pre, current) => {
            const description = JSON.parse(current.description || '{}');
            if (this.productTypeControl.value != 0) {
              let total = 0;
              current.products &&
                current.products.map(product => {
                  if (product.id && product.productTypeId && product.productTypeId != this.productTypeControl.value) {
                    total += (product.salePrice ? product.salePrice : product.sellPrice || 0) * description[product.id];
                  }
                });
              return pre + Math.floor((current.price || 0) / 10 - total / 1000);
            }
            return pre + Math.floor((current.price || 0) / 10);
          }, 0);
        }
        if (this.typeFilterControl.value == 1) {
          this.lineChartData[0].data[+key] = data[key].reduce((pre, current) => {
            const description = JSON.parse(current.description || '{}');
            let total = 0;
            let total_remove = 0;
            current.products &&
              current.products.map(product => {
                if (product.id) total += (product.importPrice || 0) * description[product.id];
                if (this.productTypeControl.value != 0) {
                  if (product.id && product.productTypeId && product.productTypeId != this.productTypeControl.value) {
                    total_remove += (product.salePrice ? product.salePrice : product.sellPrice || 0) * description[product.id];
                    total -= (product.importPrice || 0) * description[product.id];
                  }
                }
              });
            return pre + Math.floor((current.price || 0) / 10 - (total + total_remove) / 1000);
          }, 0);
        }
        if (this.typeFilterControl.value == 2) {
          this.lineChartData[0].data[+key] = data[key].reduce((pre, current) => {
            const description = JSON.parse(current.description || '{}');
            let total = 0;
            if (this.productTypeControl.value == 0) {
              total = Object.keys(description).reduce<number>((pre1, key) => pre1 + +description[key], 0);
            } else {
              current.products &&
                current.products.map(product => {
                  if (product.id && product.productTypeId && product.productTypeId == this.productTypeControl.value)
                    total += +description[product.id];
                });
            }
            return pre + total;
          }, 0);
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
