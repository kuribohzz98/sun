import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-login-register',
  templateUrl: './login-register.component.html'
})
export class LoginRegisterComponent implements OnInit {
  @Input() idModel: string = '';
  private isLogin: boolean = true;

  constructor(private modal: NgbModal) {}

  ngOnInit(): void {}

  changTabSet(): void {
    this.isLogin = !this.isLogin;
  }
}
