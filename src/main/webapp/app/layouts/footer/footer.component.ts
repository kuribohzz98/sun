import { AccountService } from 'app/core/auth/account.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {
  constructor(private accountService: AccountService) {}

  ngOnInit() {}

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }
}
