import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-login-register',
  templateUrl: './login-register.component.html'
})
export class LoginRegisterComponent {
  @Input() idModel: string = '';
  private isLogin: boolean = true;

  constructor(private modal: NgbModal) {}

  changTabSet() {
    this.isLogin = !this.isLogin;
  }
}
