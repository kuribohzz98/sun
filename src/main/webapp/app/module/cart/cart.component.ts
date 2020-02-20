import { FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { faPlus, faMinus, faTrashAlt, faAngleDoubleLeft } from '@fortawesome/free-solid-svg-icons';
import { Location } from '@angular/common';
// import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
// import { Observable, Subject, merge } from 'rxjs';
// import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';

// const states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado',
//     'Connecticut', 'Delaware', 'District Of Columbia', 'Federated States Of Micronesia', 'Florida', 'Georgia',
//     'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine',
//     'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
//     'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
//     'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island',
//     'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Islands', 'Virginia',
//     'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit {
  private faPlus = faPlus;
  private faMinus = faMinus;
  private faTrash = faTrashAlt;
  private faAngleDoubleLeft = faAngleDoubleLeft;
  private formHouse: any;
  private formSuperMarket: any;
  private products: any[] = [];
  private totalPrice: number = 0;
  // @ViewChild('instance', { static: true }) instance: any;
  // focus$ = new Subject<string>();
  // click$ = new Subject<string>();

  // model: any;

  constructor(private fb: FormBuilder, private _location: Location) {}

  ngOnInit(): void {
    this.formHouse = this.fb.group({
      name: ['', Validators.required],
      phone: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      ward: ['', Validators.required],
      address: ['', Validators.required],
      bill: [false, Validators.required]
    });
    this.formSuperMarket = this.fb.group({
      name: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      date: ['', Validators.required]
    });

    this.products = JSON.parse(localStorage.getItem('cart') || '[]');
    this.products.map((product: any, index) => {
      if (product.salePrice) return (this.totalPrice += product.salePrice);
      this.totalPrice += product.sellPrice;
    });
  }

  backPage() {
    this._location.back();
  }

  addAmount(index: number, amount: number) {
    const price = this.products[index].salePrice ? +this.products[index].salePrice : +this.products[index].sellPrice;
    this.totalPrice -= this.products[index].amount * price;
    this.products[index].amount = amount;
    this.totalPrice += this.products[index].amount * price;
  }

  removeItem(index: number) {
    this.addAmount(index, 0);
    this.products.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(this.products));
  }
  // search(text$: Observable<string>): Observable<string[]> {
  //     const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
  //     const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
  //     const inputFocus$ = this.focus$;

  //     return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
  //         map(term => (term === '' ? states
  //             : states.filter(v => v.toLowerCase().indexOf(term.toLowerCase()) > -1)).slice(0, 10))
  //     );
  // }
}
